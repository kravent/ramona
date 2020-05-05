package component.main

import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.div
import react.rFunction

val MainPage = rFunction("MainPage") { props: RProps ->
    div {
        navBar()
    }
    div {
        props.children()
    }
}

fun RBuilder.mainPage(
    handler: RHandler<RProps>
) = MainPage {
    handler()
}
