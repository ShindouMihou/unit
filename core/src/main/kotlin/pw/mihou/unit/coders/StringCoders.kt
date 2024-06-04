package pw.mihou.unit.coders

import pw.mihou.unit.coders.interfaces.BufferedReaderDecoder
import pw.mihou.unit.coders.interfaces.Encoder
import java.io.BufferedReader
import kotlin.streams.asSequence

object StringEncoder: Encoder {
    override fun encode(content: Any): String {
        if (content is String) {
            return content
        } else {
            throw IllegalArgumentException("Non-string content provided in a String encoder.")
        }
    }
}

object StringDecoder: BufferedReaderDecoder<String> {
    override fun decode(reader: BufferedReader): String {
        return reader.lines()
            .asSequence()
            .joinToString("\n")
    }
}