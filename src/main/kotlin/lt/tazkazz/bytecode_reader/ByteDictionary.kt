package lt.tazkazz.bytecode_reader

private const val UNKNOWN = "?? <UNKNOWN>"

fun getBin(byte: Int) = byte.toString(radix = 2)
fun getHex(byte: Int) = byte.toString(radix = 16)
fun getInt(byte: Int) = byte.toString().padStart(3)

fun getConstantPoolTag(byte: Int) =
    when (byte) {
        7 -> "CONSTANT_Class"
        9 -> "CONSTANT_Fieldref"
        10 -> "CONSTANT_Methodref"
        11 -> "CONSTANT_InterfaceMethodref"
        8 -> "CONSTANT_String"
        3 -> "CONSTANT_Integer"
        4 -> "CONSTANT_Float"
        5 -> "CONSTANT_Long"
        6 -> "CONSTANT_Double"
        12 -> "CONSTANT_NameAndType"
        1 -> "CONSTANT_Utf8"
        15 -> "CONSTANT_MethodHandle"
        16 -> "CONSTANT_MethodType"
        18 -> "CONSTANT_InvokeDynamic"
        else -> UNKNOWN
    }

fun getInstruction(byte: Int) =
    when (byte) {
        0x2a -> "aload_0"
        0xb7 -> "invokespecial"
        0xb1 -> "return"
        0xb2 -> "getstatic"
        0xb6 -> "invokevirtual"
        0xbb -> "new"
        0x59 -> "dup"
        0x12 -> "ldc"
        0x01 -> "aconst_null"
        0xbf -> "athrow"
        else -> null
    }