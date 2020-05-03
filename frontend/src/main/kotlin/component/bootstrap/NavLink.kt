package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

@JsModule("react-bootstrap/NavLink")
private external val module: dynamic

interface NavLinkProps : BootstrapRProps {
    var active: Boolean
    var `as`: Any
    var eventKey: String
    var href: String
    var onSelect: (eventKey: String, event: Event?) -> Unit
    var role: String
}

@Suppress("UnsafeCastFromDynamic")
val NavLink: RClass<NavLinkProps> = module.default
val StyledNavLink = styled(NavLink)
