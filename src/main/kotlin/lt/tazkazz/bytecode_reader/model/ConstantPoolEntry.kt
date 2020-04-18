package lt.tazkazz.bytecode_reader.model

import lt.tazkazz.bytecode_reader.model.ConstantPoolTag.*

typealias ConstantPool = List<ResolvedConstantPoolEntry>

interface ConstantPoolEntry {
    val tag: ConstantPoolTag
}

sealed class UnresolvedConstantPoolEntry : ConstantPoolEntry
sealed class ResolvedConstantPoolEntry : ConstantPoolEntry

/* Unresolved entries */
data class ClassEntryUnresolved(val nameIndex: Short) : UnresolvedConstantPoolEntry() {
    override val tag = CLASS
}

data class FieldRefEntryUnresolved(
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = FIELD_REF
}

data class MethodRefEntryUnresolved(
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = METHOD_REF
}

data class InterfaceMethodRefEntryUnresolved(
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = INTERFACE_METHOD_REF
}

data class StringEntryUnresolved(val valueIndex: Short) : UnresolvedConstantPoolEntry() {
    override val tag = STRING
}

data class NameAndTypeEntryUnresolved(
    val nameIndex: Short,
    val descriptorIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = NAME_AND_TYPE
}

data class MethodHandleEntryUnresolved(
    val referenceKind: Int,
    val referenceIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = METHOD_HANDLE
}

data class MethodTypeEntryUnresolved(val descriptorIndex: Short) : UnresolvedConstantPoolEntry() {
    override val tag = METHOD_TYPE
}

data class InvokeDynamicEntryUnresolved(
    val bootstrapMethodAttributeIndex: Short,
    val nameAndTypeIndex: Short
) : UnresolvedConstantPoolEntry() {
    override val tag = INVOKE_DYNAMIC
}


/* Resolved entries */
data class ClassEntry(val name: String) : ResolvedConstantPoolEntry() {
    override val tag = CLASS
}

data class FieldRefEntry(
    val classEntry: ClassEntry,
    val nameAndType: NameAndTypeEntry
) : ResolvedConstantPoolEntry() {
    override val tag = FIELD_REF
}

data class MethodRefEntry(
    val classEntry: ClassEntry,
    val nameAndType: NameAndTypeEntry
) : ResolvedConstantPoolEntry() {
    override val tag = METHOD_REF
}

data class InterfaceMethodRefEntry(
    val classEntry: ClassEntry,
    val nameAndType: NameAndTypeEntry
) : ResolvedConstantPoolEntry() {
    override val tag = INTERFACE_METHOD_REF
}

data class StringEntry(val value: String) : ResolvedConstantPoolEntry() {
    override val tag = STRING
}

data class IntegerEntry(val value: Int) : ResolvedConstantPoolEntry() {
    override val tag = INTEGER
}

data class FloatEntry(val value: Float) : ResolvedConstantPoolEntry() {
    override val tag = FLOAT
}

data class LongEntry(val value: Long) : ResolvedConstantPoolEntry() {
    override val tag = LONG
}

data class DoubleEntry(val value: Double) : ResolvedConstantPoolEntry() {
    override val tag = DOUBLE
}

data class NameAndTypeEntry(
    val name: String,
    val descriptor: String
) : ResolvedConstantPoolEntry() {
    override val tag = NAME_AND_TYPE
}

data class Utf8Entry(val value: String) : ResolvedConstantPoolEntry() {
    override val tag = UTF8
}

data class MethodHandleEntry(
    val referenceKind: Int,
    val reference: ResolvedConstantPoolEntry
) : ResolvedConstantPoolEntry() {
    override val tag = METHOD_HANDLE
}

data class MethodTypeEntry(val descriptor: String) : ResolvedConstantPoolEntry() {
    override val tag = METHOD_TYPE
}

data class InvokeDynamicEntry(
    val bootstrapMethodAttributeIndex: Short,
    val nameAndType: NameAndTypeEntry
) : ResolvedConstantPoolEntry() {
    override val tag = INVOKE_DYNAMIC
}