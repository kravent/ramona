package component.bootstrap

import react.RClass
import react.ReactElement
import styled.styled

@JsModule("react-bootstrap/NavDropdown")
private external val module: dynamic

interface NavDropdownProps : BootstrapRProps {
    var active: Boolean
    var disabled: Boolean
    var id: String
    var menuRole: String
    var rootCloseEvent: String
    var title: ReactElement
}

@Suppress("UnsafeCastFromDynamic")
val NavDropdown: RClass<NavDropdownProps> = module.default
val StyledNavDropdown = styled(NavDropdown)
