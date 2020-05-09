package component.standups

import ajax.Api
import component.bootstrap.Alert
import component.bootstrap.AlertVariant
import component.bootstrap.variant
import component.main.mainPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.agaman.ramona.model.Standup
import me.agaman.ramona.route.ApiRoutes
import react.*
import react.dom.div
import react.dom.h3
import react.router.dom.routeLink

val StandupListPage = rFunction("StandupListPage") { _: RProps ->
    var loading by useState(false)
    var error by useState(null as String?)
    var standups by useState(emptyList<Standup>())

    useEffect(listOf()) {
        loading = true
        MainScope().launch {
            try {
                val response = Api.get(ApiRoutes.STANDUP_LIST)
                response.standups?.let { standups = it }
                error = response.error
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    mainPage {
        when {
            loading -> {
                h3 { +"Loading..." }
            }
            error != null -> {
                Alert {
                    attrs.variant = AlertVariant.danger
                    +"$error"
                }
            }
            standups.isEmpty() -> {
                Alert {
                    attrs.variant = AlertVariant.info
                    +"No standups found"
                }
            }
            else -> {
                standups.forEach { standup ->
                    div {
                        routeLink(to = "/standups/view/${standup.id}") {
                            +standup.name
                        }
                    }
                }
            }
        }

    }
}

fun RBuilder.standupListPage() = StandupListPage {}
