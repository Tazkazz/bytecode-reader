package lt.tazkazz.bytecode_reader.model

enum class ConstantPoolTag(val tag: Int, val code: String) {
    CLASS(7, "CONSTANT_Class"),
    FIELD_REF(9, "CONSTANT_Fieldref"),
    METHOD_REF(10, "CONSTANT_Methodref"),
    INTERFACE_METHOD_REF(11, "CONSTANT_InterfaceMethodref"),
    STRING(8, "CONSTANT_String"),
    INTEGER(3, "CONSTANT_Integer"),
    FLOAT(4, "CONSTANT_Float"),
    LONG(5, "CONSTANT_Long"),
    DOUBLE(6, "CONSTANT_Double"),
    NAME_AND_TYPE(12, "CONSTANT_NameAndType"),
    UTF8(1, "CONSTANT_Utf8"),
    METHOD_HANDLE(15, "CONSTANT_MethodHandle"),
    METHOD_TYPE(16, "CONSTANT_MethodType"),
    INVOKE_DYNAMIC(18, "CONSTANT_InvokeDynamic")
}