package component.bootstrap

import react.RClass
import styled.styled

@Suppress("EnumEntryName")
enum class FormLabelColumn { sm, lg }

interface FormLabelProps : BootstrapRProps {
    var htmlFor: String
}
fun FormLabelProps.column(column: Boolean) = setRProp(this, "column", column.toString())
fun FormLabelProps.column(column: FormLabelColumn) = setRProp(this, "column", column.toString())

@Suppress("UnsafeCastFromDynamic")
val FormLabel: RClass<FormLabelProps> = Form.asDynamic().Label
val StyledFormLabel = styled(FormLabel)
