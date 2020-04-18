package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.*
import java.lang.Double.longBitsToDouble
import java.lang.Float.intBitsToFloat

private typealias BytesWithTitle = Pair<List<Int>, String>

fun JVMBytecodeReader.readConstantPoolEntry(index: Int) {
    val tag = reader.readByte()
    val tagName = getConstantPoolTag(tag)
    val (bytes, data) = when (tag) {
        7, 8, 16 -> readOneIndex()
        9, 10, 11, 12, 18 -> readTwoIndices()
        3 -> readConstantInt()
        4 -> readConstantFloat()
        5 -> readConstantLong()
        6 -> readConstantDouble()
        1 -> readConstantUtf8()
        15 -> readMethodHandle()
        else -> throw RuntimeException("Unknown constant pool tag $tag")
    }
    printer.printBytes("[$index] <$tag> $tagName $data", listOf(tag) + bytes)
}

private fun JVMBytecodeReader.readOneIndex(): BytesWithTitle {
    val index = reader.readBytes(2)
    return index to "#${index.from2ToShort()}"
}

private fun JVMBytecodeReader.readTwoIndices(): BytesWithTitle {
    val index1 = reader.readBytes(2)
    val index2 = reader.readBytes(2)
    return (index1 + index2) to "#${index1.from2ToShort()}.#${index2.from2ToShort()}"
}

private fun JVMBytecodeReader.readConstantInt(): BytesWithTitle {
    val int = reader.readBytes(4)
    return int to int.from4ToInt().toString()
}

private fun JVMBytecodeReader.readConstantFloat(): BytesWithTitle {
    val float = reader.readBytes(4)
    return float to intBitsToFloat(float.from4ToInt()).toString()
}

private fun JVMBytecodeReader.readConstantLong(): BytesWithTitle {
    val long = reader.readBytes(8)
    return long to long.from8ToLong().toString()
}

private fun JVMBytecodeReader.readConstantDouble(): BytesWithTitle {
    val double = reader.readBytes(8)
    return double to longBitsToDouble(double.from8ToLong()).toString()
}

private fun JVMBytecodeReader.readConstantUtf8(): BytesWithTitle {
    val lengthBytes = reader.readBytes(2)
    val length = lengthBytes.from2ToShort()
    val bytes = reader.readBytes(length.toInt())
    return (lengthBytes + bytes) to "*$length '${bytes.toUtf8String()}'"
}

private fun JVMBytecodeReader.readMethodHandle(): BytesWithTitle {
    val kind = reader.readBytes(1)
    val index = reader.readBytes(2)
    return (kind + index) to "${kind[0]} #${index.from2ToShort()}"
}