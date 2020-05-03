package component

import component.main.mainPage
import react.RBuilder
import react.dom.h1
import react.router.dom.route
import react.router.dom.routeLink
import react.router.dom.switch

fun RBuilder.appRouter() = switch {
    route("/", exact = true) {
        mainPage(title = "Home") {
            routeLink("/standups") { +"Standups" }
        }
    }
    route("*") {
        mainPage(title = "Error") {
            h1 { +"ERROR: Page not found" }
        }
    }
}
