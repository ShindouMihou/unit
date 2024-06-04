package pw.mihou.unit

import pw.mihou.unit.coders.TomlDecoder
import pw.mihou.unit.coders.TomlEncoder
import pw.mihou.unit.core.Unit

inline fun <reified T : Any> String.toml(): Unit<T> {
    return Unit(
        address = if(this.endsWith(".toml")) this else "$this.toml",
        charset = Charsets.UTF_8,
        decoder = TomlDecoder(T::class),
        encoder = TomlEncoder(T::class),
        allowsAppend = false
    )
}