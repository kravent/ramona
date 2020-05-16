package component.standups

import ajax.Api
import component.bootstrap.*
import component.main.mainPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.StandupCreateRequest
import me.agaman.ramona.model.WeekDay
import me.agaman.ramona.route.ApiRoutes
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.div
import react.dom.option
import react.router.dom.useHistory
import utils.withTarget

fun RBuilder.generateHourOptions() {
    for (hour in 0..23) {
        for (minutes in arrayOf(0, 30)) {
            option {
                attrs.value = (hour * 100 + minutes).toString()
                +"${hour.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
            }
        }
    }
}

private data class StandupCreationValidation(
    val name: Boolean = true,
    val days: Boolean = true,
    val questions: Map<Int, Boolean> = emptyMap()
) {
    fun validQuestion(index: Int): Boolean = questions[index] ?: true
    fun isValid(): Boolean = name && days && questions.values.all { it }
}

val StandupCreatePage = functionalComponent { _: RProps ->
    val history = useHistory()
    var loading by useState(false)
    var showError by useState(false)
    var errorMessage by useState("")
    var standup by useState(Standup(
        name = "",
        startHour = 900,
        finishHour = 1100,
        days = setOf(WeekDay.Monday, WeekDay.Tuesday, WeekDay.Wednesday, WeekDay.Thursday, WeekDay.Friday),
        questions = listOf("")
    ))
    var validation by useState(StandupCreationValidation())

    fun isStandupValid(): Boolean {
        var newValidation = StandupCreationValidation()
        if (standup.name.isEmpty()) {
            newValidation = newValidation.copy(name = false)
        }
        if (standup.days.isEmpty()) {
            newValidation = newValidation.copy(days = false)
        }
        standup.questions.forEachIndexed { index, question ->
            if (question.isEmpty()) {
                newValidation = newValidation.copy(
                    questions = newValidation.questions.toMutableMap().apply { this[index] = false }
                )
            }
        }
        validation = newValidation
        return newValidation.isValid()
    }

    fun saveStandup() {
        if (isStandupValid()) {
            showError = false
            loading = true
            MainScope().launch {
                try {
                    val response = Api.post(ApiRoutes.STANDUP_CREATE, StandupCreateRequest(standup))
                    response.standup?.let {
                        history.push("/standups/view/${it.id}")
                    }
                    response.error?.let {
                        showError = true
                        errorMessage = it
                    }
                } catch (e: Exception) {
                    showError = true
                    errorMessage = e.message ?: "Something went wrong"
                } finally {
                    loading = false
                }
            }
        }
    }

    mainPage {
        Alert {
            attrs {
                dismissible = true
                show = showError
                variant = AlertVariant.danger
                onClose = { showError = false }
            }
            +errorMessage
        }

        FormGroup {
            attrs.controlId = "standupNameField"
            FormLabel { +"Standup name" }
            FormControl {
                attrs {
                    type = FormControlType.text
                    value(standup.name)
                    isInvalid = !validation.name
                    disabled = loading
                    onChange = withTarget<HTMLInputElement> {
                        validation = validation.copy(name = true)
                        standup = standup.copy(name = it.value)
                    }
                }
            }
        }

        FormRow {
            FormGroup {
                attrs.`as` = Col
                attrs.controlId = "startHourField"
                FormLabel { +"Start hour" }
                FormControl {
                    attrs {
                        `as` = "select"
                        type = FormControlType.text
                        value(standup.startHour)
                        disabled = loading
                        onChange = withTarget<HTMLSelectElement> { standup = standup.copy(startHour = it.value.toInt()) }
                    }
                    generateHourOptions()
                }
            }
            FormGroup {
                attrs.`as` = Col
                attrs.controlId = "finishHourField"
                FormLabel { +"Finish hour" }
                FormControl {
                    attrs {
                        `as` = "select"
                        type = FormControlType.text
                        value(standup.finishHour)
                        disabled = loading
                        onChange = withTarget<HTMLSelectElement> { standup = standup.copy(finishHour = it.value.toInt()) }
                    }
                    generateHourOptions()
                }
            }
        }

        FormGroup {
            FormLabel { +"Days" }
            div {
                ButtonGroup {
                    WeekDay.values().forEach { weekDay ->
                        val isChecked = standup.days.contains(weekDay)
                        Button {
                            attrs {
                                variant = when {
                                    isChecked -> ButtonVariant.primary
                                    validation.days -> ButtonVariant.outline_secondary
                                    else -> ButtonVariant.outline_danger
                                }
                                disabled = loading
                                onClick = {
                                    validation = validation.copy(days = true)
                                    standup = standup.copy(
                                        days = standup.days.toMutableSet().apply {
                                            if (isChecked) {
                                                this.remove(weekDay)
                                            } else {
                                                this.add(weekDay)
                                            }
                                        }
                                    )
                                }
                            }
                            +weekDay.key
                        }
                    }
                }
            }
        }

        FormLabel { +"Questions" }
        for (question in standup.questions.withIndex()) {
            FormGroup {
                div("input-group") {
                    FormControl {
                        attrs {
                            type = FormControlType.text
                            placeholder = "Question ${question.index + 1}"
                            value(question.value)
                            isInvalid = !validation.validQuestion(question.index)
                            disabled = loading
                            onChange = withTarget<HTMLInputElement> {
                                validation = validation.copy(
                                    questions = validation.questions.toMutableMap()
                                        .apply { this.remove(question.index) }
                                )
                                standup = standup.copy(
                                    questions = standup.questions.toMutableList()
                                        .apply { this[question.index] = it.value }
                                )
                            }
                        }
                    }
                    if (question.index > 0) {
                        div("input-group-append") {
                            Button {
                                attrs {
                                    variant = ButtonVariant.outline_danger
                                    onClick = {
                                        standup = standup.copy(
                                            questions = standup.questions.toMutableList()
                                                .apply { this.removeAt(question.index) }
                                        )
                                    }
                                }
                                +"×"
                            }
                        }
                    }
                }
            }
        }
        if (standup.questions.size < 5) {
            Button {
                attrs {
                    variant = ButtonVariant.outline_secondary
                    size = ButtonSize.sm
                    onClick = {
                        standup = standup.copy(
                            questions = standup.questions.toMutableList()
                                .apply { this.add("") }
                        )
                    }
                }
                +"Add question"
            }
        }

        div("text-right") {
            Button {
                attrs {
                    type = ButtonType.submit
                    variant = ButtonVariant.primary
                    disabled = loading
                    onClick = { saveStandup() }
                }
                +"Save"
            }
        }
    }
}

fun RBuilder.standupCreatePage() = child(StandupCreatePage)
