package lt.tazkazz.bytecode_reader

import lt.tazkazz.bytecode_reader.reader_extensions.readAttributeEntry
import lt.tazkazz.bytecode_reader.reader_extensions.readConstantPoolEntry
import lt.tazkazz.bytecode_reader.reader_extensions.readFieldEntry
import lt.tazkazz.bytecode_reader.reader_extensions.readMethodEntry

class JVMBytecodeReader(filename: String) {
    val reader = ByteReader(filename)
    val printer = BytePrinter()

    fun read() {
        printSuperLine("JVM Class Bytecode start")
        readBytes(4, "Magic number", ::getHex)
        readBytes(4, "Version", ::getInt)

        printSuperLine("Constant pool")
        val cpCount = readShort("Constant pool count", ::getInt)
        repeat(cpCount - 1) { readConstantPoolEntry(it + 1) }

        printSuperLine("Miscellaneous")
        readShort("Access flags", ::getBin)
        readShort("This class", ::getHex)
        readShort("Super class", ::getHex)

        printSuperLine("Interfaces")
        val interfaceCount = readShort("Interface count", ::getInt)
        repeat(interfaceCount.toInt()) { index ->
            val bytes = reader.readBytes(2)
            printer.printBytes("[${index + 1}] Interface #${bytes.from2ToShort()}", bytes)
        }

        printSuperLine("Fields")
        val fieldCount = readShort("Field count", ::getInt)
        repeat(fieldCount.toInt()) { readFieldEntry(it + 1) }

        printSuperLine("Methods")
        val methodCount = readShort("Method count", ::getInt)
        repeat(methodCount.toInt()) { readMethodEntry(it + 1) }

        printSuperLine("Attributes")
        val attributeCount = readShort("Attribute count", ::getInt)
        repeat(attributeCount.toInt()) { readAttributeEntry(it + 1) }

        printSuperLine("JVM Class Bytecode end")
    }

    fun readShort(title: String, suffixFn: (Int) -> String): Short {
        return readBytes(2, title, suffixFn).from2ToShort()
    }

    private fun readBytes(count: Int, title: String, suffixFn: (Int) -> String): List<Int> {
        val bytes = reader.readBytes(count)
        printer.printBytes(title, bytes, suffixFn)
        return bytes
    }

    private fun printSuperLine(title: String) {
        val line = "#".repeat(40)
        println("$line $title $line")
    }
}