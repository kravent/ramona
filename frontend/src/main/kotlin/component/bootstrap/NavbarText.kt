package component.bootstrap

import react.RClass
import styled.styled

@Suppress("UnsafeCastFromDynamic")
val NavbarText: RClass<BootstrapRProps> = Navbar.asDynamic().Text
val StyledNavbarText = styled(NavbarText)
