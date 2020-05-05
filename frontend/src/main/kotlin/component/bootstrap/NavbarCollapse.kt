package component.bootstrap

import react.RClass
import styled.styled

@Suppress("UnsafeCastFromDynamic")
val NavbarCollapse: RClass<BootstrapRProps> = Navbar.asDynamic().Collapse
val StyledNavbarCollapse = styled(NavbarCollapse)
