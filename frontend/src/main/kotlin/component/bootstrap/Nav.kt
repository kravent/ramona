package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

@JsModule("react-bootstrap/Nav")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class NavVariant { tabs, pills }

interface NavProps : BootstrapRProps {
    var activeKey: String
    var fill: Boolean
    var justify: Boolean
    var navbar: Boolean
    var onSelect: (eventKey: String, event: Event?) -> Unit
    var role: String
}
var NavProps.variant by EnumProperty(NavVariant.values())

@Suppress("UnsafeCastFromDynamic")
val Nav: RClass<NavProps> = module.default
val StyledNav = styled(Nav)
