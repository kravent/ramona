package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Row")
private external val module: dynamic

interface RowProps : BootstrapRProps {
    var noGutters: Boolean
    var xs: Int
    var sm: Int
    var md: Int
    var lg: Int
    var xl: Int
}

@Suppress("UnsafeCastFromDynamic")
val Row: RClass<RowProps> = module.default
val StyledRow = styled(Row)
