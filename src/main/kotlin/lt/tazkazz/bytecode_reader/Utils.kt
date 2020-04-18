package lt.tazkazz.bytecode_reader

import lt.tazkazz.bytecode_reader.model.ConstantPool
import lt.tazkazz.bytecode_reader.model.ResolvedConstantPoolEntry
import lt.tazkazz.bytecode_reader.model.Utf8Entry

fun Byte.to256Int(): Int = toInt() and 0xff
fun Int.toSafeString(): String = if (this in 32..126) toChar().toString() else "."
fun Int.hex(pad: Int = 2) = "0x" + toString(radix = 16).padStart(pad, '0')

fun List<Int>.toUtf8String(): String = String(toByteArray())
fun List<Int>.toByteArray(): ByteArray = map(Int::toByte).toByteArray()

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

inline fun <reified T : ResolvedConstantPoolEntry> ConstantPool.getConstant(index: Short): T {
    val entry = get(index - 1)
    if (entry !is T) {
        throw RuntimeException("Expected constant pool entry #$index to be ${T::class.simpleName}, but got ${entry::class.simpleName}")
    }
    return entry
}

fun ConstantPool.getUtf8Value(index: Short): String {
    return getConstant<Utf8Entry>(index).value
}