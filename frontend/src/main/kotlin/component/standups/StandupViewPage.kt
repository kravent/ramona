package component.standups

import ajax.Api
import component.bootstrap.Alert
import component.bootstrap.AlertVariant
import component.bootstrap.variant
import component.main.mainPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.agaman.ramona.model.Standup
import me.agaman.ramona.model.StandupGetRequest
import me.agaman.ramona.route.ApiRoutes
import react.*
import react.dom.div
import react.dom.h3
import react.router.dom.routeLink

data class StandupViewPageRProps(val standupId: Int) : RProps

val StandupViewPage = functionalComponent { props: StandupViewPageRProps ->
    var loading by useState(false)
    var standup by useState(null as Standup?)
    var error by useState(null as String?)

    useEffect(listOf()) {
        loading = true
        MainScope().launch {
            try {
                val response = Api.get(ApiRoutes.STANDUP_GET.build(StandupGetRequest(props.standupId)))
                standup = response.standup
                error = response.error
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    mainPage {
        error?.let {
            Alert {
                attrs.variant = AlertVariant.danger
                +it
            }
        }

        if (loading) {
            h3 { +"Loading..." }
        }

        div { +"External ID: ${standup?.externalId}" }
        div { +"Name: ${standup?.name}" }
        div { +"Start hour: ${standup?.startHour}" }
        div { +"Finish hour: ${standup?.finishHour}" }
        div { +"Days: ${standup?.days?.joinToString(" ") { it.key }}" }
        (standup?.questions ?: listOf("")).forEachIndexed { index, question ->
            div { +"Question ${index + 1}: $question" }
        }
        standup?.externalId?.let {
            div { routeLink("/standups/fill/$it") { +"Fill standup" } }
        }
    }
}

fun RBuilder.standupViewPage(standupId: Int) = child(StandupViewPage, StandupViewPageRProps(standupId))
