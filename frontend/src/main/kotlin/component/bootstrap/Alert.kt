package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Alert")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class AlertVariant { primary, secondary, success, danger, warning, info, dark, light }

interface AlertProps : BootstrapRProps {
    var closeLabel: String
    var dismissible: Boolean
    var onClose: () -> Unit
    var show: Boolean
}
var AlertProps.variant by EnumProperty(AlertVariant.values())

@Suppress("UnsafeCastFromDynamic")
val Alert: RClass<AlertProps> = module.default
val StyledAlert = styled(Alert)
