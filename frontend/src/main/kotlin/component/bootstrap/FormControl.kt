package component.bootstrap

import org.w3c.dom.events.Event
import react.RClass
import styled.styled

@JsModule("react-bootstrap/FormControl")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class FormControlSize { sm, lg }
@Suppress("EnumEntryName")
enum class FormControlType { button, checkbox, color, date, email, file, hidden, image, month, number, password, radio,
    range, reset, search, submit, tel, text, time, url, week }


interface FormControlProps : BootstrapRProps {
    var `as`: String
    var custom: Boolean
    var disabled: Boolean
    var id: String
    var isInvalid: Boolean
    var isValid: Boolean
    var onChange: (Event) -> Unit
    var plaintext: Boolean
    var readOnly: Boolean
}
var FormControlProps.size by EnumProperty(FormControlSize.values())
var FormControlProps.type by EnumProperty(FormControlType.values())
fun FormControlProps.value(value: String) = setRProp(this, "value", value)
fun FormControlProps.value(value: Array<Any>) = setRProp(this, "value", value)
fun FormControlProps.value(value: Number) = setRProp(this, "value", value)

@Suppress("UnsafeCastFromDynamic")
val FormControl: RClass<FormControlProps> = module.default
val StyledFormControl = styled(FormControl)
