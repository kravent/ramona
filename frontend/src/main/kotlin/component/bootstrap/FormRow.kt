package component.bootstrap

import react.RClass
import styled.styled

@Suppress("UnsafeCastFromDynamic")
val FormRow: RClass<BootstrapRProps> = Form.asDynamic().Row
val StyledFormRow = styled(FormRow)
