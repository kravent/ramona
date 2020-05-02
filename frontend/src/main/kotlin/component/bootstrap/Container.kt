package component.bootstrap

import react.RClass
import styled.styled

@JsModule("react-bootstrap/Container")
private external val module: dynamic

@Suppress("EnumEntryName")
enum class ContainerFluid { sm, md, lg, xl }

interface ContainerProps : BootstrapRProps {
    var active: Boolean
    var block: Boolean
    var disabled: Boolean
    var href: String
}
fun ContainerProps.fluid(fluid: Boolean) = setRProp(this, "fluid", fluid)
fun ContainerProps.fluid(fluid: ContainerFluid) = setRProp(this, "fluid", fluid.toString())

@Suppress("UnsafeCastFromDynamic")
val Container: RClass<ContainerProps> = module.default
val StyledContainer = styled(Container)
