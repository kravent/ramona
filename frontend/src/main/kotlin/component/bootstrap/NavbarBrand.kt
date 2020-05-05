package component.bootstrap

import react.RClass
import styled.styled

interface NavbarBrandProps : BootstrapRProps {
    var `as`: Any
    var href: Boolean
}

@Suppress("UnsafeCastFromDynamic")
val NavbarBrand: RClass<NavbarBrandProps> = Navbar.asDynamic().Brand
val StyledNavbarBrand = styled(NavbarBrand)
