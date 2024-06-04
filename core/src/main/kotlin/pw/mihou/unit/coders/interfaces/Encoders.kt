package pw.mihou.unit.coders.interfaces

interface Encoder {
    fun encode(content: Any): String
}