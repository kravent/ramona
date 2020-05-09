package component

import component.main.errorPage
import component.main.mainPage
import component.standups.standupCreatePage
import component.standups.standupListPage
import component.standups.standupViewPage
import react.RBuilder
import react.RProps
import react.dom.div
import react.router.dom.route
import react.router.dom.routeLink
import react.router.dom.switch

interface IdProps : RProps {
    val id: Int
}

fun RBuilder.appRouter() = switch {
    route("/", exact = true) {
        mainPage {
            div { routeLink("/standups") { +"Standups" } }
        }
    }
    route("/standups", exact = true) {
        mainPage {
            div { routeLink("/standups/create") { +"Create standup" } }
            div { routeLink("/standups/list") { +"View standups" } }
        }
    }
    route("/standups/create") { standupCreatePage() }
    route<IdProps>("/standups/view/:id") { props ->
        standupViewPage(props.match.params.id)
    }
    route("/standups/list") { standupListPage() }
    route("*") { errorPage() }
}
