package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/NavbarToggle")
private external val module: dynamic

interface NavbarToggleProps : BootstrapRProps {
    var `as`: Any
    var label: String
}

@Suppress("UnsafeCastFromDynamic")
val NavbarToggle: RClass<NavbarToggleProps> = module.default
val StyledNavbarToggle = styled(NavbarToggle)
