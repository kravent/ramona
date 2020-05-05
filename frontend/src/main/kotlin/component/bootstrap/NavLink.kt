package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

interface NavLinkProps : BootstrapRProps {
    var active: Boolean
    var `as`: Any
    var eventKey: String
    var href: String
    var onSelect: (eventKey: String, event: Event?) -> Unit
    var role: String
}

@Suppress("UnsafeCastFromDynamic")
val NavLink: RClass<NavLinkProps> = Nav.asDynamic().Link
val StyledNavLink = styled(NavLink)
