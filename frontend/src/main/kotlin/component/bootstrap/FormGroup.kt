package component.bootstrap

import react.RClass
import styled.styled

interface FormGroupProps : BootstrapRProps {
    var `as`: Any
    var controlId: String
}

@Suppress("UnsafeCastFromDynamic")
val FormGroup: RClass<FormGroupProps> = Form.asDynamic().Group
val StyledFormGroup = styled(FormGroup)
