package lt.tazkazz.bytecode_reader

interface BytePrinter {
    fun printBytes(title: String, bytes: List<Int>, suffixFn: ((Int) -> String?)? = null)
    fun printTitle(title: String, symbol: String = "-", count: Int = 3)
}

class VerboseBytePrinter : BytePrinter {
    private var index = 0

    override fun printBytes(title: String, bytes: List<Int>, suffixFn: ((Int) -> String?)?) {
        printTitle(title)
        bytes.forEach { byte -> printByte(byte, suffixFn?.let { it(byte) }) }
        println()
    }

    override fun printTitle(title: String, symbol: String, count: Int) {
        val line = symbol.repeat(count)
        println("$line $title $line")
    }

    private fun printByte(byte: Int, suffix: String? = null) {
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

class SilentBytePrinter : BytePrinter {
    override fun printBytes(title: String, bytes: List<Int>, suffixFn: ((Int) -> String?)?) {}
    override fun printTitle(title: String, symbol: String, count: Int) {}
}