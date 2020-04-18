package lt.tazkazz.bytecode_reader.reader_extensions

import lt.tazkazz.bytecode_reader.model.*

private typealias UnresolvedConstantPool = List<ConstantPoolEntry>

fun UnresolvedConstantPool.resolve(): ConstantPool {
    return ConstantPoolResolver(this).resolve()
}

class ConstantPoolResolver(private val pool: UnresolvedConstantPool) {
    private val resolvedByIndex = mutableMapOf<Int, ResolvedConstantPoolEntry>()

    fun resolve(): ConstantPool {
        return pool.mapIndexed { index, entry -> resolve(index, entry) }
    }

    private fun resolve(index: Int, entry: ConstantPoolEntry): ResolvedConstantPoolEntry {
        resolvedByIndex[index]?.let { return it }
        val newEntry = when (entry) {
            is ResolvedConstantPoolEntry -> entry
            is ClassEntryUnresolved -> ClassEntry(resolveUtf8ValueAt(entry.nameIndex))
            is FieldRefEntryUnresolved -> FieldRefEntry(
                resolveAt(entry.classIndex),
                resolveAt(entry.nameAndTypeIndex)
            )
            is MethodRefEntryUnresolved -> MethodRefEntry(
                resolveAt(entry.classIndex),
                resolveAt(entry.nameAndTypeIndex)
            )
            is InterfaceMethodRefEntryUnresolved -> InterfaceMethodRefEntry(
                resolveAt(entry.classIndex),
                resolveAt(entry.nameAndTypeIndex)
            )
            is StringEntryUnresolved -> StringEntry(resolveUtf8ValueAt(entry.valueIndex))
            is NameAndTypeEntryUnresolved -> NameAndTypeEntry(
                resolveUtf8ValueAt(entry.nameIndex),
                resolveUtf8ValueAt(entry.descriptorIndex)
            )
            is MethodHandleEntryUnresolved -> MethodHandleEntry(
                entry.referenceKind,
                resolveAt(entry.referenceIndex)
            )
            is MethodTypeEntryUnresolved -> MethodTypeEntry(resolveUtf8ValueAt(entry.descriptorIndex))
            is InvokeDynamicEntryUnresolved -> InvokeDynamicEntry(
                entry.bootstrapMethodAttributeIndex,
                resolveAt(entry.nameAndTypeIndex)
            )
            else -> throw RuntimeException("Unexpected unresolved entry: $entry")
        }
        return newEntry.also { resolvedByIndex[index] = it }
    }

    private fun resolveUtf8ValueAt(index: Short): String {
        return resolveAt<Utf8Entry>(index).value
    }

    private inline fun <reified T : ResolvedConstantPoolEntry> resolveAt(index: Short): T {
        val entry = resolve(index - 1, pool[index - 1])
        if (entry !is T) {
            throw RuntimeException("Expected constant pool entry #$index to be ${T::class.simpleName}, but got ${entry::class.simpleName}")
        }
        return entry
    }
}