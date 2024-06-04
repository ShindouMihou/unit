package pw.mihou.unit

import pw.mihou.unit.coders.JsonDecoder
import pw.mihou.unit.coders.JsonEncoder
import pw.mihou.unit.core.Unit

inline fun <reified T : Any> String.json(): Unit<T> {
    return Unit(
        address = if(this.endsWith(".json")) this else "$this.json",
        charset = Charsets.UTF_8,
        decoder = JsonDecoder(T::class),
        encoder = JsonEncoder(T::class),
        allowsAppend = false
    )
}