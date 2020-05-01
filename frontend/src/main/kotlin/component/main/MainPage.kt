package component.main

import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.div
import react.rFunction

interface MainPageProps : RProps {
    var title: String
    var backRoute: String?
}

val MainPage = rFunction("MainPage") { props: MainPageProps ->
    div {
        navBar(props.title, props.backRoute)
    }
    div {
        props.children()
    }
}

fun RBuilder.mainPage(
    title: String,
    backRoute: String? = null,
    handler: RHandler<MainPageProps>
) = MainPage {
    attrs.title = title
    attrs.backRoute = backRoute
    handler()
}
