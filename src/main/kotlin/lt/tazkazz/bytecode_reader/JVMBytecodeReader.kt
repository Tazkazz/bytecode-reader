package lt.tazkazz.bytecode_reader

import lt.tazkazz.bytecode_reader.model.ClassEntry
import lt.tazkazz.bytecode_reader.model.ClassFile
import lt.tazkazz.bytecode_reader.reader_extensions.*

class JVMBytecodeReader(private val filename: String, verbose: Boolean = false) {
    val reader = ByteReader(filename)
    val printer = if (verbose) VerboseBytePrinter() else SilentBytePrinter()

    fun read(): ClassFile {
        printSuperLine("JVM Class Bytecode start")
        val magicNumber = readInt("Magic number", ::getHex)
        checkMagicNumber(filename, magicNumber)

        val majorVersion = readShort("Major version", ::getInt)
        val minorVersion = readShort("Minor version", ::getInt)

        printSuperLine("Constant pool")
        val cpCount = readShort("Constant pool count", ::getInt)
        val constantPool = (1 until cpCount).map { readConstantPoolEntry(it + 1) }.resolve()

        printSuperLine("Miscellaneous")
        val accessFlags = readShort("Access flags", ::getBin)
        val thisClass = readShort("This class", ::getHex)
        val superClass = readShort("Super class", ::getHex)

        printSuperLine("Interfaces")
        val interfaceCount = readShort("Interface count", ::getInt)
        val interfaces = (0 until interfaceCount).map { index ->
            val interfaceIndexBytes = reader.readBytes(2)
            val interfaceIndex = interfaceIndexBytes.from2ToShort()
            printer.printBytes("[${index + 1}] Interface #$interfaceIndex", interfaceIndexBytes)
            constantPool.getConstant<ClassEntry>(interfaceIndex)
        }

        printSuperLine("Fields")
        val fieldCount = readShort("Field count", ::getInt)
        val fields = (0 until fieldCount).map { readFieldEntry(it + 1, constantPool) }

        printSuperLine("Methods")
        val methodCount = readShort("Method count", ::getInt)
        val methods = (0 until methodCount).map { readMethodEntry(it + 1, constantPool) }

        printSuperLine("Attributes")
        val attributeCount = readShort("Attribute count", ::getInt)
        val attributes = (0 until attributeCount).map { readAttributeEntry(it + 1, constantPool) }

        printSuperLine("JVM Class Bytecode end")
        checkFileEnd()

        return ClassFile(
            magicNumber = magicNumber,
            minorVersion = minorVersion,
            majorVersion = majorVersion,
            constantPool = constantPool,
            accessFlags = accessFlags,
            thisClass = constantPool.getConstant(thisClass),
            superClass = constantPool.getConstant(superClass),
            interfaces = interfaces,
            fields = fields,
            methods = methods,
            attributes = attributes
        )
    }

    private fun printSuperLine(title: String) {
        printer.printTitle(title, symbol = "#", count = 40)
    }

    fun readInt(title: String, suffixFn: (Int) -> String): Int {
        return readBytes(4, title, suffixFn).from4ToInt()
    }

    fun readShort(title: String, suffixFn: (Int) -> String): Short {
        return readBytes(2, title, suffixFn).from2ToShort()
    }

    private fun readBytes(count: Int, title: String, suffixFn: (Int) -> String): List<Int> {
        val bytes = reader.readBytes(count)
        printer.printBytes(title, bytes, suffixFn)
        return bytes
    }

    private fun checkMagicNumber(filename: String, magicNumber: Int) {
        if (magicNumber != 0xcafebabe.toInt()) {
            throw RuntimeException("Given file is not JVM Class file: $filename")
        }
    }

    private fun checkFileEnd() {
        if (reader.available) {
            throw RuntimeException("There are still ${reader.unreadBytes} bytes left unread")
        }
    }
}