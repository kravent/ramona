package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/FormGroup")
private external val module: dynamic

interface FormGroupProps : BootstrapRProps {
    var controlId: String
}

@Suppress("UnsafeCastFromDynamic")
val FormGroup: RClass<FormGroupProps> = module.default
val StyledFormGroup = styled(FormGroup)
