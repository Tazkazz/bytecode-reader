package lt.tazkazz.bytecode_reader

private const val UNKNOWN = "?? <UNKNOWN>"

fun getBin(byte: Int) = byte.toString(radix = 2)
fun getHex(byte: Int) = byte.toString(radix = 16)
fun getInt(byte: Int) = byte.toString().padStart(3)

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