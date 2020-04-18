package lt.tazkazz.bytecode_reader

import com.tylerthrailkill.helpers.prettyprint.pp

fun main(vararg args: String) {
    if (args.isEmpty()) {
        throw RuntimeException("Filename was not given!")
    }
    val filename = args[0]
    val verbose = args.drop(1).any { it == "--verbose" || it == "-v" }
    val bytecodeReader = JVMBytecodeReader(filename, verbose)
    val classFile = bytecodeReader.read()
    pp(classFile)
}