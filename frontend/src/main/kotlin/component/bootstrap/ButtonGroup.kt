package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/ButtonGroup")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class ButtonGroupSize { sm, lg }

interface ButtonGroupProps : BootstrapRProps {
    var role: String
    var toggle: Boolean
    var vertical: Boolean
}
var ButtonGroupProps.size by EnumProperty(ButtonGroupSize.values())

@Suppress("UnsafeCastFromDynamic")
val ButtonGroup: RClass<ButtonGroupProps> = module.default
val StyledButtonGroup = styled(ButtonGroup)
