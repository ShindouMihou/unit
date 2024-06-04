package pw.mihou.unit

import pw.mihou.unit.coders.YamlDecoder
import pw.mihou.unit.coders.YamlEncoder
import pw.mihou.unit.core.Unit

inline fun <reified T : Any> String.yaml(): Unit<T> {
    return Unit(
        address = if(this.endsWith(".yaml")) this else "$this.yaml",
        charset = Charsets.UTF_8,
        decoder = YamlDecoder(T::class),
        encoder = YamlEncoder(T::class),
        allowsAppend = false
    )
}

inline fun <reified T : Any> String.yml(): Unit<T> {
    return Unit(
        address = if(this.endsWith(".yml")) this else "$this.yml",
        charset = Charsets.UTF_8,
        decoder = YamlDecoder(T::class),
        encoder = YamlEncoder(T::class),
        allowsAppend = false
    )
}