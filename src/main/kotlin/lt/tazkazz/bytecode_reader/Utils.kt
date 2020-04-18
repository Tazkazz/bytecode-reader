package lt.tazkazz.bytecode_reader

fun Byte.to256Int(): Int = toInt() and 0xff
fun Int.toSafeString(): String = if (this in 32..126) toChar().toString() else "."
fun Int.hex(pad: Int = 2) = "0x" + toString(radix = 16).padStart(pad, '0')

fun List<Int>.toUtf8String(): String = String(map(Int::toByte).toByteArray())

fun List<Int>.from2ToShort(): Short = fromBytesToLong(2).toShort()
fun List<Int>.from4ToInt(): Int = fromBytesToLong(4).toInt()
fun List<Int>.from8ToLong(): Long = fromBytesToLong(8)

private fun List<Int>.fromBytesToLong(size: Int): Long {
    if (size != size || (size != 2 && size != 4 && size != 8)) {
        throw RuntimeException("Invalid size of bytes for a number: $size")
    }
    var result = 0L
    repeat(size) { index ->
        result = result shl 8
        result += get(index)
    }
    return result
}