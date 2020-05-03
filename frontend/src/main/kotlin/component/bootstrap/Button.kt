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
enum class ButtonVariant {
    primary, secondary, success, danger, warning, info, dark, light, link,
    outline_primary { override fun toString(): String = "outline-primary" },
    outline_secondary { override fun toString(): String = "outline-secondary" },
    outline_success { override fun toString(): String = "outline-success" },
    outline_danger { override fun toString(): String = "outline-danger" },
    outline_warning { override fun toString(): String = "outline-warning" },
    outline_info { override fun toString(): String = "outline-info" },
    outline_dark { override fun toString(): String = "outline-dark" },
    outline_light { override fun toString(): String = "outline-light" },
    outline_link { override fun toString(): String = "outline-link" }
}

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
