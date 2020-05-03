package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/NavbarBrand")
private external val module: dynamic

interface NavbarBrandProps : BootstrapRProps {
    var `as`: Any
    var href: Boolean
}

@Suppress("UnsafeCastFromDynamic")
val NavbarBrand: RClass<NavbarBrandProps> = module.default
val StyledNavbarBrand = styled(NavbarBrand)
