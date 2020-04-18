package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.JVMBytecodeReader
import lt.tazkazz.bytecode_reader.from2ToShort
import lt.tazkazz.bytecode_reader.getBin
import lt.tazkazz.bytecode_reader.getInt

fun JVMBytecodeReader.readMethodEntry(index: Int) {
    val accessFlags = reader.readBytes(2)
    val nameIndexBytes = reader.readBytes(2)
    val nameIndex = nameIndexBytes.from2ToShort()
    val descriptorIndexBytes = reader.readBytes(2)
    val descriptorIndex = descriptorIndexBytes.from2ToShort()

    println("===== [$index] Field #$nameIndex =====")
    printer.printBytes("Access flags", accessFlags, ::getBin)
    printer.printBytes("Name index #$nameIndex", nameIndexBytes)
    printer.printBytes("Descriptor index #$descriptorIndex", descriptorIndexBytes)

    val attributeCount = readShort("Attribute count", ::getInt)
    repeat(attributeCount.toInt()) { readAttributeEntry(it + 1) }
}