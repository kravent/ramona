package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

@JsModule("react-bootstrap/ToggleButtonGroup")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class ToggleButtonGroupType { checkbox, radio }

interface ToggleButtonGroupProps : BootstrapRProps {
    var name: String
    var onChange: (Event) -> Unit
    var value: Any
}
var ToggleButtonGroupProps.type by EnumProperty(ToggleButtonGroupType.values())

@Suppress("UnsafeCastFromDynamic")
val ToggleButtonGroup: RClass<ToggleButtonGroupProps> = module.default
val StyledToggleButtonGroup = styled(ToggleButtonGroup)
