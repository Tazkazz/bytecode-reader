package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.*
import lt.tazkazz.bytecode_reader.model.*
import lt.tazkazz.bytecode_reader.model.ConstantPoolTag.*
import java.lang.Double.longBitsToDouble
import java.lang.Float.intBitsToFloat

private data class BytesWithTitleAndEntry(
    val bytes: List<Int>,
    val title: String,
    val entry: ConstantPoolEntry
)

fun JVMBytecodeReader.readConstantPoolEntry(index: Int): ConstantPoolEntry {
    val tagByte = reader.readByte()
    val tag = values().find { it.tag == tagByte } ?: throw RuntimeException("Unknown constant pool tag: $tagByte")
    val (bytes, data, entry) = when (tag) {
        CLASS -> readClassEntry()
        FIELD_REF -> readFieldRefEntry()
        METHOD_REF -> readMethodRefEntry()
        INTERFACE_METHOD_REF -> readInterfaceMethodRefEntry()
        STRING -> readStringEntry()
        INTEGER -> readIntegerEntry()
        FLOAT -> readFloatEntry()
        LONG -> readLongEntry()
        DOUBLE -> readDoubleEntry()
        NAME_AND_TYPE -> readNameAndTypeEntry()
        UTF8 -> readUtf8Entry()
        METHOD_HANDLE -> readMethodHandleEntry()
        METHOD_TYPE -> readMethodTypeEntry()
        INVOKE_DYNAMIC -> readInvokeDynamicEntry()
    }
    printer.printBytes("[$index] <$tagByte> ${tag.code} $data", listOf(tagByte) + bytes)
    return entry
}

private fun JVMBytecodeReader.readClassEntry(): BytesWithTitleAndEntry {
    val indexBytes = reader.readBytes(2)
    val index = indexBytes.from2ToShort()
    return BytesWithTitleAndEntry(indexBytes, "#$index", ClassEntryUnresolved(index))
}

private fun JVMBytecodeReader.readFieldRefEntry(): BytesWithTitleAndEntry {
    val classIndexBytes = reader.readBytes(2)
    val classIndex = classIndexBytes.from2ToShort()
    val natIndexBytes = reader.readBytes(2)
    val natIndex = natIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(
        classIndexBytes + natIndexBytes,
        "#$classIndex.#$natIndex",
        FieldRefEntryUnresolved(classIndex, natIndex)
    )
}

private fun JVMBytecodeReader.readMethodRefEntry(): BytesWithTitleAndEntry {
    val classIndexBytes = reader.readBytes(2)
    val classIndex = classIndexBytes.from2ToShort()
    val natIndexBytes = reader.readBytes(2)
    val natIndex = natIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(
        classIndexBytes + natIndexBytes,
        "#$classIndex.#$natIndex",
        MethodRefEntryUnresolved(classIndex, natIndex)
    )
}

private fun JVMBytecodeReader.readInterfaceMethodRefEntry(): BytesWithTitleAndEntry {
    val classIndexBytes = reader.readBytes(2)
    val classIndex = classIndexBytes.from2ToShort()
    val natIndexBytes = reader.readBytes(2)
    val natIndex = natIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(
        classIndexBytes + natIndexBytes,
        "#$classIndex.#$natIndex",
        InterfaceMethodRefEntryUnresolved(classIndex, natIndex)
    )
}

private fun JVMBytecodeReader.readStringEntry(): BytesWithTitleAndEntry {
    val indexBytes = reader.readBytes(2)
    val index = indexBytes.from2ToShort()
    return BytesWithTitleAndEntry(indexBytes, "#$index", StringEntryUnresolved(index))
}

private fun JVMBytecodeReader.readIntegerEntry(): BytesWithTitleAndEntry {
    val valueBytes = reader.readBytes(4)
    val value = valueBytes.from4ToInt()
    return BytesWithTitleAndEntry(valueBytes, value.toString(), IntegerEntry(value))
}

private fun JVMBytecodeReader.readFloatEntry(): BytesWithTitleAndEntry {
    val valueBytes = reader.readBytes(4)
    val value = intBitsToFloat(valueBytes.from4ToInt())
    return BytesWithTitleAndEntry(valueBytes, value.toString(), FloatEntry(value))
}

private fun JVMBytecodeReader.readLongEntry(): BytesWithTitleAndEntry {
    val valueBytes = reader.readBytes(8)
    val value = valueBytes.from8ToLong()
    return BytesWithTitleAndEntry(valueBytes, value.toString(), LongEntry(value))
}

private fun JVMBytecodeReader.readDoubleEntry(): BytesWithTitleAndEntry {
    val valueBytes = reader.readBytes(8)
    val value = longBitsToDouble(valueBytes.from8ToLong())
    return BytesWithTitleAndEntry(valueBytes, value.toString(), DoubleEntry(value))
}

private fun JVMBytecodeReader.readNameAndTypeEntry(): BytesWithTitleAndEntry {
    val nameIndexBytes = reader.readBytes(2)
    val nameIndex = nameIndexBytes.from2ToShort()
    val descriptorIndexBytes = reader.readBytes(2)
    val descriptorIndex = descriptorIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(
        nameIndexBytes + descriptorIndexBytes,
        "#$nameIndex.#$descriptorIndex",
        NameAndTypeEntryUnresolved(nameIndex, descriptorIndex)
    )
}

private fun JVMBytecodeReader.readUtf8Entry(): BytesWithTitleAndEntry {
    val lengthBytes = reader.readBytes(2)
    val length = lengthBytes.from2ToShort()
    val bytes = reader.readBytes(length.toInt())
    val value = bytes.toUtf8String()
    return BytesWithTitleAndEntry(lengthBytes + bytes, "*$length", Utf8Entry(value))
}

private fun JVMBytecodeReader.readMethodHandleEntry(): BytesWithTitleAndEntry {
    val kind = reader.readByte()
    val indexBytes = reader.readBytes(2)
    val index = indexBytes.from2ToShort()
    return BytesWithTitleAndEntry(listOf(kind) + indexBytes, "$kind #$index", MethodHandleEntryUnresolved(kind, index))
}

private fun JVMBytecodeReader.readMethodTypeEntry(): BytesWithTitleAndEntry {
    val descriptorIndexBytes = reader.readBytes(2)
    val descriptorIndex = descriptorIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(descriptorIndexBytes, "#$descriptorIndex", MethodTypeEntryUnresolved(descriptorIndex))
}

private fun JVMBytecodeReader.readInvokeDynamicEntry(): BytesWithTitleAndEntry {
    val bootstrapMethodAttributeIndexBytes = reader.readBytes(2)
    val bootstrapMethodAttributeIndex = bootstrapMethodAttributeIndexBytes.from2ToShort()
    val natIndexBytes = reader.readBytes(2)
    val natIndex = natIndexBytes.from2ToShort()
    return BytesWithTitleAndEntry(
        bootstrapMethodAttributeIndexBytes + natIndexBytes,
        "#$bootstrapMethodAttributeIndex.#$natIndex",
        InvokeDynamicEntryUnresolved(bootstrapMethodAttributeIndex, natIndex)
    )
}