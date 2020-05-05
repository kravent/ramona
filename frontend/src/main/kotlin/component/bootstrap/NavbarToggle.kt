package component.bootstrap

import react.RClass
import styled.styled

interface NavbarToggleProps : BootstrapRProps {
    var `as`: Any
    var label: String
}

@Suppress("UnsafeCastFromDynamic")
val NavbarToggle: RClass<NavbarToggleProps> = Navbar.asDynamic().Toggle
val StyledNavbarToggle = styled(NavbarToggle)
