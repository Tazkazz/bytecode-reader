package lt.tazkazz.bytecode_reader.model

data class Method(
    val accessFlags: Short,
    val name: String,
    val descriptor: String,
    val attributes: List<Attribute>
)