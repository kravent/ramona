package component.bootstrap

import react.RClass
import styled.styled

interface FormGroupProps : BootstrapRProps {
    var controlId: String
}

@Suppress("UnsafeCastFromDynamic")
val FormGroup: RClass<FormGroupProps> = Form.asDynamic().Group
val StyledFormGroup = styled(FormGroup)
