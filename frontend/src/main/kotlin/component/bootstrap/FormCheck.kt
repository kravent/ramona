package component.bootstrap

import react.RClass
import react.ReactElement
import styled.styled

@Suppress("EnumEntryName")
enum class FormCheckType { radio, checkbox, switch }

interface FormCheckProps : BootstrapRProps {
    var custom: Boolean
    var disabled: Boolean
    var feedback: ReactElement
    var id: String
    var inline: Boolean
    var isInvalid: Boolean
    var isValid: Boolean
    var label: ReactElement
    var title: String
}
var FormCheckProps.type by EnumProperty(FormCheckType.values())

@Suppress("UnsafeCastFromDynamic")
val FormCheck: RClass<FormCheckProps> = Form.asDynamic().Check
val StyledFormCheck = styled(FormCheck)
