package component.main

import react.RBuilder
import react.RProps
import react.dom.h1
import react.rFunction

val ErrorPage = rFunction("ErrorPage") { _: RProps ->
    mainPage {
        h1 { +"ERROR: Page not found" }
    }
}

fun RBuilder.errorPage() = ErrorPage {}
