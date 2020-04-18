package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.*
import lt.tazkazz.bytecode_reader.model.ConstantPool
import lt.tazkazz.bytecode_reader.model.Method

fun JVMBytecodeReader.readMethodEntry(index: Int, constantPool: ConstantPool): Method {
    val accessFlags = reader.readBytes(2)
    val nameIndexBytes = reader.readBytes(2)
    val nameIndex = nameIndexBytes.from2ToShort()
    val descriptorIndexBytes = reader.readBytes(2)
    val descriptorIndex = descriptorIndexBytes.from2ToShort()

    printer.printTitle("[$index] Method #$nameIndex", symbol = "=", count = 5)
    printer.printBytes("Access flags", accessFlags, ::getBin)
    printer.printBytes("Name index #$nameIndex", nameIndexBytes)
    printer.printBytes("Descriptor index #$descriptorIndex", descriptorIndexBytes)

    val attributeCount = readShort("Attribute count", ::getInt)
    val attributes = (0 until attributeCount).map { readAttributeEntry(it + 1, constantPool) }

    return Method(
        accessFlags = accessFlags.from2ToShort(),
        name = constantPool.getUtf8Value(nameIndex),
        descriptor = constantPool.getUtf8Value(descriptorIndex),
        attributes = attributes
    )
}