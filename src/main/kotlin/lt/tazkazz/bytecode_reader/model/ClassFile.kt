package lt.tazkazz.bytecode_reader.model

data class ClassFile(
    val magicNumber: Int,
    val minorVersion: Short,
    val majorVersion: Short,
    val constantPool: ConstantPool,
    val accessFlags: Short,
    val thisClass: ClassEntry,
    val superClass: ClassEntry,
    val interfaces: List<ClassEntry>,
    val fields: List<Field>,
    val methods: List<Method>,
    val attributes: List<Attribute>
)