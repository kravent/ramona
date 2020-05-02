package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Form")
private external val module: dynamic

interface FormProps : BootstrapRProps {
    var inline: Boolean
    var validated: Boolean
}

@Suppress("UnsafeCastFromDynamic")
val Form: RClass<FormProps> = module.default
val StyledForm = styled(Form)
