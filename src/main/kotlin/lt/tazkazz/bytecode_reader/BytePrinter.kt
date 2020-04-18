package lt.tazkazz.bytecode_reader

class BytePrinter {
    private var index = 0

    fun printBytes(title: String, bytes: List<Int>, suffixFn: (Int) -> String?) {
        printTitle(title)
        bytes.forEach { printByte(it, suffixFn) }
        println()
    }

    fun printBytes(title: String, bytes: List<Int>) {
        printTitle(title)
        bytes.forEach(::printByte)
        println()
    }

    fun printTitle(title: String) {
        println("--- $title ---")
    }

    fun printByte(byte: Int) {
        printByte(byte, null)
    }

    fun printByte(byte: Int, suffixFn: (Int) -> String?) {
        printByte(byte, suffixFn(byte))
    }

    fun printByte(byte: Int, suffix: String? = null) {
        val line = listOfNotNull(
            index++.hex(4),
            byte.hex(),
            byte.toSafeString(),
            byte.toString().padStart(3),
            suffix
        ).joinToString(separator = "  ")
        println(line)
    }
}