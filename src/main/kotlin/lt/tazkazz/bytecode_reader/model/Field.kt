package lt.tazkazz.bytecode_reader.model

data class Field(
    val accessFlags: Short,
    val name: String,
    val descriptor: String,
    val attributes: List<Attribute>
)