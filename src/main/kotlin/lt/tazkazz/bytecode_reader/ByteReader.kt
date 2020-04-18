package lt.tazkazz.bytecode_reader

import java.io.File

class ByteReader(filename: String) {
    private val data = File(filename).readBytes()
    private var index = 0

    val available: Boolean
        get() = index < data.size

    fun readByte(): Int {
        return data[index++].to256Int()
    }

    fun readBytes(size: Int): List<Int> {
        return data.copyOfRange(index, index + size)
            .map(Byte::to256Int)
            .also { index += size }
    }
}