package component

import component.main.errorPage
import component.main.mainPage
import component.standups.standupCreatePage
import react.RBuilder
import react.router.dom.route
import react.router.dom.routeLink
import react.router.dom.switch

fun RBuilder.appRouter() = switch {
    route("/", exact = true) {
        mainPage {
            routeLink("/standups") { +"Standups" }
        }
    }
    route("/standups", exact = true) {
        mainPage {
            routeLink("/standups/create") { +"Create standup" }
        }
    }
    route("/standups/create") { standupCreatePage() }
    route("*") { errorPage() }
}
