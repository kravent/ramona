package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

@JsModule("react-bootstrap/ToggleButton")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class ToggleButtonType { checkbox, radio }

interface ToggleButtonProps : BootstrapRProps {
    var checked: Boolean
    var disabled: Boolean
    var name: String
    var onChange: (Event) -> Unit
    var value: Any
}
var ToggleButtonProps.type by EnumProperty(ToggleButtonType.values())
var ToggleButtonProps.variant by EnumProperty(ButtonVariant.values())

@Suppress("UnsafeCastFromDynamic")
val ToggleButton: RClass<ToggleButtonProps> = module.default
val StyledToggleButton = styled(ToggleButton)
