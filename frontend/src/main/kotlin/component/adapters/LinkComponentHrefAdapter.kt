package component.adapters

import react.RProps
import react.rFunction
import react.router.dom.LinkComponent

interface LinkComponentHrefAdapterProps : RProps {
    var href: String
    var className: String?
}

val LinkComponentHrefAdapter = rFunction("LinkComponentHrefAdapter") { props: LinkComponentHrefAdapterProps ->
    child(LinkComponent::class) {
        attrs {
            to = props.href
            className = props.className
        }
        props.children()
    }
}
