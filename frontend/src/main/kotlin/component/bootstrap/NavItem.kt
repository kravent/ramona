package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/NavItem")
private external val module: dynamic

interface NavItemProps : BootstrapRProps {
    var `as`: Any
    var role: String
}

@Suppress("UnsafeCastFromDynamic")
val NavItem: RClass<NavItemProps> = module.default
val StyledNavItem = styled(NavItem)
