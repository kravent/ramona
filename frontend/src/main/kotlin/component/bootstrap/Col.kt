package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Col")
private external val module: dynamic

interface ColProps : BootstrapRProps {
    var xs: Int
    var sm: Int
    var md: Int
    var lg: Int
    var xl: Int
}

@Suppress("UnsafeCastFromDynamic")
val Col: RClass<ColProps> = module.default
val StyledCol = styled(Col)
