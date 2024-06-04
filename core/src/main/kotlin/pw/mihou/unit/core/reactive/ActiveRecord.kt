package pw.mihou.unit.core.reactive

import kotlin.Unit
import kotlin.reflect.KProperty

class ActiveRecord<T>(
    private val get: () -> T,
    private val set: (value: T) -> Unit,
) {
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): T {
        return get()
    }
    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T
    ) {
        set(value)
    }
}