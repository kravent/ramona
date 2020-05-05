package component.bootstrap

import react.RClass
import styled.styled

interface NavItemProps : BootstrapRProps {
    var `as`: Any
    var role: String
}

@Suppress("UnsafeCastFromDynamic")
val NavItem: RClass<NavItemProps> = Nav.asDynamic().Item
val StyledNavItem = styled(NavItem)
