package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/NavbarCollapse")
private external val module: dynamic

@Suppress("UnsafeCastFromDynamic")
val NavbarCollapse: RClass<BootstrapRProps> = module.default
val StyledNavbarCollapse = styled(NavbarCollapse)
