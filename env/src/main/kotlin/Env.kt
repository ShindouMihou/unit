import coders.EnvDecoder
import coders.EnvEncoder
import pw.mihou.unit.core.Unit

fun String.env(): Unit<Map<String, String>> {
    return Unit(
        address = if(this.endsWith(".env")) this else "$this.env",
        charset = Charsets.UTF_8,
        decoder = EnvDecoder,
        encoder = EnvEncoder,
        allowsAppend = true
    )
}