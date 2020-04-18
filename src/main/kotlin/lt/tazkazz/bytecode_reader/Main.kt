package lt.tazkazz.bytecode_reader

fun main(vararg args: String) {
    if (args.size != 1) {
        throw RuntimeException("Filename was not given!")
    }
    val filename = args[0]

    val bytecodeReader = JVMBytecodeReader(filename)
    bytecodeReader.read()
}