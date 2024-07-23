package coders

import pw.mihou.unit.coders.interfaces.Encoder
import pw.mihou.unit.coders.interfaces.FileDecoder
import java.io.File
import java.nio.file.Files

object EnvEncoder: Encoder {
    override fun encode(content: Any): String {
        if (content is Map<*, *>) {
            return content.map { "${it.key}=${it.value}" }.joinToString("\n")
        }
        if (content is Pair<*, *>) {
            return "${content.first}=${content.second}"
        } else {
            throw IllegalArgumentException("Non-map of key-value pairs content provided in a .env encoder.")
        }
    }
}

object EnvDecoder: FileDecoder<Map<String, String>> {
    override fun decode(file: File): Map<String, String> {
        val stream = Files.lines(file.toPath())

        // Inheriting from sister library Envi (https://github.com/ShindouMihou/envi)
        val map = mutableMapOf<String, String>()
        stream.forEach { line ->
            if (!line.contains("=") || line.startsWith("#")) return@forEach
            val keyValue = line.split("=", limit = 2)

            if (keyValue.size < 2 || keyValue[1].isEmpty()) {
                return@forEach
            }

            map[keyValue[0]] = keyValue[1]
        }
        return map
    }
}