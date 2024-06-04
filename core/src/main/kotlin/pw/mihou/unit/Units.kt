package pw.mihou.unit

import pw.mihou.unit.coders.StringDecoder
import pw.mihou.unit.coders.StringEncoder
import pw.mihou.unit.core.Unit

val String.txt get(): Unit<String> {
    return Unit(
        address = if(this.endsWith(".txt")) this else "$this.txt",
        charset = Charsets.UTF_8,
        decoder =  StringDecoder,
        encoder = StringEncoder,
        allowsAppend = true,
    )
}

val String.unit get(): Unit<String> {
    return Unit(
        address = this,
        charset = Charsets.UTF_8,
        decoder =  StringDecoder,
        encoder = StringEncoder,
        allowsAppend = true,
    )
}