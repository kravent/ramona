package component.bootstrap

import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.RClass
import styled.styled

@JsModule("react-bootstrap/Navbar")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class NavbarExpand { sm, md, lg, xl }
@Suppress("EnumEntryName")
enum class NavbarPosition { top, bottom }
@Suppress("EnumEntryName")
enum class NavbarVariant { light, dark }

interface NavbarProps : BootstrapRProps {
    var bg: String
    var collapseOnSelect: Boolean
    var expanded: Boolean
    var onSelect: (eventKey: KeyboardEvent, event: Event?) -> Unit
    var onToggle: () -> Unit
    var show: Boolean
}
fun NavbarProps.expand(expand: Boolean) = setRProp(this, "expand", expand)
fun NavbarProps.expand(expand: NavbarExpand) = setRProp(this, "expand", expand.toString())
var NavbarProps.fixed by EnumProperty(NavbarPosition.values())
var NavbarProps.sticky by EnumProperty(NavbarPosition.values())
var NavbarProps.variant by EnumProperty(NavbarVariant.values())

@Suppress("UnsafeCastFromDynamic")
val Navbar: RClass<NavbarProps> = module.default
val StyledNavbar = styled(Navbar)
val NavbarText: RClass<BootstrapRProps> = module.default.Text
val StyledNavbarText = styled(NavbarText)
