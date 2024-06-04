package pw.mihou.unit.coders

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import pw.mihou.unit.coders.interfaces.BufferedReaderDecoder
import pw.mihou.unit.coders.interfaces.Encoder
import java.io.BufferedReader
import kotlin.reflect.KClass
import kotlin.streams.asSequence

class TomlEncoder<T : Any>(private val type: KClass<T>): Encoder {
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun encode(content: Any): String {
        return Toml.encodeToString(
            type.serializer(),
            content as T
        )
    }
}

class TomlDecoder<T : Any>(private val type: KClass<T>): BufferedReaderDecoder<T> {
    @OptIn(InternalSerializationApi::class)
    override fun decode(reader: BufferedReader): T {
        return Toml.decodeFromString(
            type.serializer(),
            reader.lines()
                .asSequence()
                .joinToString("\n")
        )
    }
}