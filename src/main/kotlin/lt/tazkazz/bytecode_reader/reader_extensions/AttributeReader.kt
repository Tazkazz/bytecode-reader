package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.*
import lt.tazkazz.bytecode_reader.model.Attribute
import lt.tazkazz.bytecode_reader.model.ConstantPool

fun JVMBytecodeReader.readAttributeEntry(index: Int, constantPool: ConstantPool): Attribute {
    val nameIndexBytes = reader.readBytes(2)
    val nameIndex = nameIndexBytes.from2ToShort()
    val lengthBytes = reader.readBytes(4)
    val length = lengthBytes.from4ToInt()
    val bytes = reader.readBytes(length)
    val value = bytes.joinToString(separator = "", transform = Int::toSafeString)

    printer.printTitle("[$index] Attribute #$nameIndex", symbol = "~", count = 5)
    printer.printBytes("Name index #$nameIndex", nameIndexBytes)
    printer.printBytes("Length *$length", lengthBytes)
    printer.printBytes("Info '$value'", bytes)

    return Attribute(
        name = constantPool.getUtf8Value(nameIndex),
        value = bytes.toByteArray()
    )
}