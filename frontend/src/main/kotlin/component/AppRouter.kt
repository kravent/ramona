package component

import component.main.errorPage
import component.main.mainPage
import component.standups.standupCreatePage
import component.standups.standupViewPage
import react.RBuilder
import react.RProps
import react.router.dom.route
import react.router.dom.routeLink
import react.router.dom.switch

interface IdProps : RProps {
    val id: Int
}

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
    route<IdProps>("/standups/view/:id") { props ->
        standupViewPage(props.match.params.id)
    }
    route("*") { errorPage() }
}
