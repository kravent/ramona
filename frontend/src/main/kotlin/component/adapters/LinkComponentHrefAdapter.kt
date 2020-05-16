package component.adapters

import react.RProps
import react.functionalComponent
import react.router.dom.LinkComponent

interface LinkComponentHrefAdapterProps : RProps {
    var href: String
    var className: String?
}

val LinkComponentHrefAdapter = functionalComponent { props: LinkComponentHrefAdapterProps ->
    child(LinkComponent::class) {
        attrs {
            to = props.href
            className = props.className
        }
        props.children()
    }
}
