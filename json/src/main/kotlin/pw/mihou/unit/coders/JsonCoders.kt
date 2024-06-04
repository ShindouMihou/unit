package pw.mihou.unit.coders

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import pw.mihou.unit.coders.interfaces.BufferedReaderDecoder
import pw.mihou.unit.coders.interfaces.Encoder
import java.io.BufferedReader
import kotlin.reflect.KClass
import kotlin.streams.asSequence

class JsonEncoder<T : Any>(private val type: KClass<T>): Encoder {
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun encode(content: Any): String {
        return Json.Default.encodeToString(
            type.serializer(),
            content as T
        )
    }
}

class JsonDecoder<T : Any>(private val type: KClass<T>): BufferedReaderDecoder<T> {
    @OptIn(InternalSerializationApi::class)
    override fun decode(reader: BufferedReader): T {
        return Json.Default.decodeFromString(
            type.serializer(),
            reader.lines()
                .asSequence()
                .joinToString("\n")
        )
    }
}