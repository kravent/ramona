package component.main

import component.bootstrap.Col
import component.bootstrap.Container
import component.bootstrap.Row
import react.RBuilder
import react.RHandler
import react.RProps
import react.rFunction

val MainPage = rFunction("MainPage") { props: RProps ->
    navBar()
    Container {
        attrs.className = "mt-3"
        Row {
            attrs.className = "justify-content-md-center"
            Col {
                attrs.sm = 8
                props.children()
            }
        }
    }
}

fun RBuilder.mainPage(
    handler: RHandler<RProps>
) = MainPage {
    handler()
}
