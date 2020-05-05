package component.bootstrap

import react.RClass
import react.ReactElement
import styled.styled

interface NavDropdownProps : BootstrapRProps {
    var active: Boolean
    var disabled: Boolean
    var id: String
    var menuRole: String
    var rootCloseEvent: String
    var title: ReactElement
}

@Suppress("UnsafeCastFromDynamic")
val NavDropdown: RClass<NavDropdownProps> = Nav.asDynamic().Dropdown
val StyledNavDropdown = styled(NavDropdown)
