package component.main

import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.functionalComponent

val ErrorPage = functionalComponent { _: RProps ->
    mainPage {
        h1 { +"ERROR: Page not found" }
    }
}

fun RBuilder.errorPage() = child(ErrorPage)
