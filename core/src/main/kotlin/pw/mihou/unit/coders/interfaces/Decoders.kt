package pw.mihou.unit.coders.interfaces

import java.io.BufferedReader
import java.io.File

interface Decoder<T>

typealias NextLine = () -> String
interface LineDecoder<T>: Decoder<T> {
    fun decode(next: NextLine): T?
}
interface BufferedReaderDecoder<T>: Decoder<T> {
    fun decode(reader: BufferedReader): T?
}
interface FileDecoder<T>: Decoder<T> {
    fun decode(file: File): T?
}