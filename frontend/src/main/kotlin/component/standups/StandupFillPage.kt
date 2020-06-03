package component.standups

import ajax.Api
import component.bootstrap.*
import component.main.mainPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.StandupFillRequest
import me.agaman.ramona.model.StandupPublicGetRequest
import me.agaman.ramona.route.ApiRoutes
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.div
import react.dom.h3
import utils.withTarget

data class StandupFillPageRProps(
    val externalId: String
) : RProps

val StandupFillPage = functionalComponent { props: StandupFillPageRProps ->
    var loading by useState(false)
    var standup by useState(null as Standup?)
    var error by useState(null as String?)

    var responses by useState(emptyMap<Int, String>())

    var sended by useState(false)
    var sending by useState(false)
    var showSendError by useState(false)
    var sendError by useState("")

    useEffect(listOf()) {
        loading = true
        responses = emptyMap()
        MainScope().launch {
            try {
                val response = Api.get(ApiRoutes.STANDUP_PUBLIC_GET.build(StandupPublicGetRequest(props.externalId)))
                standup = response.standup
                error = response.error
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun sendResponses() {
        sending = true
        showSendError = false
        MainScope().launch {
            try {
                val response = Api.post(ApiRoutes.STANDUP_FILL, StandupFillRequest(props.externalId, responses))
                sended = response.saved
                response.error?.let {
                    showSendError = true
                    sendError = it
                }
            } catch (e: Exception) {
                showSendError = true
                sendError = e.message ?: "Something went wrong"
            } finally {
                sending = false
            }
        }
    }

    mainPage {
        if (sended) {
            Alert {
                attrs.variant = AlertVariant.success
                +"Standup responses saved!"
            }
        } else {
            error?.let {
                Alert {
                    attrs.variant = AlertVariant.danger
                    +it
                }
            }

            if (loading) {
                h3 { +"Loading..." }
            }

            standup?.let {
                h3 { +it.name }

                Alert {
                    attrs {
                        dismissible = true
                        show = showSendError
                        variant = AlertVariant.danger
                        onClose = { showSendError = false }
                    }
                    +sendError
                }

                it.questions.forEachIndexed { index, question ->
                    FormGroup {
                        attrs.controlId = "question${index}Field"
                        FormLabel { +question }
                        FormControl {
                            attrs {
                                type = FormControlType.text
                                value(responses[index] ?: "")
                                disabled = sending
                                onChange = withTarget<HTMLInputElement> {
                                    responses = responses.toMutableMap().apply { this[index] = it.value }
                                }
                            }
                        }
                    }
                }

                div("text-right") {
                    Button {
                        attrs {
                            type = ButtonType.submit
                            variant = ButtonVariant.primary
                            disabled = sending
                            onClick = { sendResponses() }
                        }
                        +"Send"
                    }
                }
            }
        }
    }
}

fun RBuilder.standupFillPage(externalId: String) = child(StandupFillPage, StandupFillPageRProps(externalId))
