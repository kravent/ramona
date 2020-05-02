package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Button")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class ButtonSize { sm, lg }
@Suppress("EnumEntryName")
enum class ButtonType { button, reset, submit }
@Suppress("EnumEntryName")
enum class ButtonVariant { primary, secondary, success, danger, warning, info, dark, light, link }

interface ButtonProps : BootstrapRProps {
    var active: Boolean
    var block: Boolean
    var disabled: Boolean
    var href: String
}
var ButtonProps.size by EnumProperty(ButtonSize.values())
var ButtonProps.type by NullableEnumProperty(ButtonType.values())
var ButtonProps.variant by EnumProperty(ButtonVariant.values())

@Suppress("UnsafeCastFromDynamic")
val Button: RClass<ButtonProps> = module.default
val StyledButton = styled(Button)
