package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.JVMBytecodeReader
import lt.tazkazz.bytecode_reader.from2ToShort
import lt.tazkazz.bytecode_reader.from4ToInt
import lt.tazkazz.bytecode_reader.toUtf8String

fun JVMBytecodeReader.readAttributeEntry(index: Int) {
    val nameIndexBytes = reader.readBytes(2)
    val nameIndex = nameIndexBytes.from2ToShort()
    val lengthBytes = reader.readBytes(4)
    val length = lengthBytes.from4ToInt()
    val bytes = reader.readBytes(length)

    println("~~~~~ [$index] Attribute #$nameIndex ~~~~~")
    printer.printBytes("Name index #$nameIndex", nameIndexBytes)
    printer.printBytes("Length *$length", lengthBytes)
    printer.printBytes("Info '${bytes.toUtf8String()}'", bytes)
}