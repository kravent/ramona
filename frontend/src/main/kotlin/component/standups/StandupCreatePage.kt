package component.standups

import component.bootstrap.*
import component.main.mainPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.div
import react.dom.option
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

val StandupCreatePage = rFunction("StandupCreatePage") { _: RProps ->
    var loading by useState(false)
    var standupName by useState("")
    var startHour by useState(900)
    var finishHour by useState(1100)
    var questions by useState(listOf(""))

    fun saveStandup() {
        loading = true
        MainScope().launch {
            delay(1000) // TODO remove debug delay
            TODO("Not yet implemented")
        }
    }

    mainPage {
        FormGroup {
            attrs.controlId = "standupNameField"
            FormLabel { +"Standup name" }
            FormControl {
                attrs {
                    type = FormControlType.text
                    value(standupName)
                    disabled = loading
                    onChange = withTarget<HTMLInputElement> { standupName = it.value }
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
                        value(startHour)
                        disabled = loading
                        onChange = withTarget<HTMLSelectElement> { startHour = it.value.toInt() }
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
                        value(finishHour)
                        disabled = loading
                        onChange = withTarget<HTMLSelectElement> { finishHour = it.value.toInt() }
                    }
                    generateHourOptions()
                }
            }
        }

        FormLabel { +"Questions" }
        for (question in questions.withIndex()) {
            FormGroup {
                div("input-group") {
                    FormControl {
                        attrs {
                            type = FormControlType.text
                            placeholder = "Question ${question.index + 1}"
                            value(question.value)
                            disabled = loading
                            onChange = withTarget<HTMLInputElement> {
                                questions = questions.toMutableList().apply { this[question.index] = it.value }
                            }
                        }
                    }
                    if (question.index > 0) {
                        div("input-group-append") {
                            Button {
                                attrs {
                                    variant = ButtonVariant.outline_danger
                                    onClick = {
                                        questions = questions.toMutableList().apply { this.removeAt(question.index) }
                                    }
                                }
                                +"×"
                            }
                        }
                    }
                }
            }
        }
        if (questions.size < 5) {
            Button {
                attrs {
                    variant = ButtonVariant.outline_secondary
                    size = ButtonSize.sm
                    onClick = {
                        questions = questions.toMutableList().apply { this.add("") }
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

fun RBuilder.standupCreatePage() = StandupCreatePage {}
