package component.bootstrap

import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import react.RProps
import react.dom.WithClassName
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface BootstrapRProps : WithClassName, RProps {
    var onClick: (MouseEvent) -> Unit
    var onDoubleClick: (MouseEvent) -> Unit
    var onKeyDown: (KeyboardEvent) -> Unit
    var onKeyPress: (KeyboardEvent) -> Unit
    var onKeyUp: (KeyboardEvent) -> Unit
    var onMouseDown: (MouseEvent) -> Unit
    var onMouseEnter: (MouseEvent) -> Unit
    var onMouseLeave: (MouseEvent) -> Unit
    var onMouseMove: (MouseEvent) -> Unit
    var onMouseOut: (MouseEvent) -> Unit
    var onMouseOver: (MouseEvent) -> Unit
    var onMouseUp: (MouseEvent) -> Unit
    var onSubmit: (FocusEvent) -> Unit
}

open class EnumProperty<T>(private val values: Array<T>) : ReadWriteProperty<RProps, T> {
    override operator fun getValue(thisRef: RProps, property: KProperty<*>): T {
        return thisRef.asDynamic()[property.name].toString()
            .let { stringValue -> values.first { it.toString() == stringValue } }
    }

    override operator fun setValue(thisRef: RProps, property: KProperty<*>, value: T) =
        setRProp(thisRef, property.name, value.toString())
}

class NullableEnumProperty<T>(private val values: Array<T>) : ReadWriteProperty<RProps, T?> {
    override operator fun getValue(thisRef: RProps, property: KProperty<*>): T? {
        return thisRef.asDynamic()[property.name]?.toString()
            ?.let { stringValue -> values.first { it.toString() == stringValue } }
    }

    override operator fun setValue(thisRef: RProps, property: KProperty<*>, value: T?) =
        setRProp(thisRef, property.name, value?.toString())
}

fun setRProp(props: RProps, propertyName: String, value: Any?) {
    props.asDynamic()[propertyName] = value
}
