
package com.rf.fled.lang;

/**
 *
 * @author REx
 */
public enum ByteCodeOperation
{
    NO_OP(0x00, 0, "no operation"),
    
    
    
    
    TYPE_N(0x01, 0, "null type"),
    TYPE_B(0x02, 1, "boolean type"),
    TYPE_I(0x03, 4, "integer type"),
    TYPE_D(0x04, 8, "double type"),
    TYPE_S(0x05, -1, "string type"),
    TYPE_U(0x06, -1, "loose type"),
    TYPE_O(0x07, -1, "object type"),
    
    
    
    
    ADD_I(0x11, 0, "add int"),
    ADD_D(0x12, 0, "add double"),
    ADD_S(0x13, 0, "concat two strings"),
    ADD_U(0x14, 0, "adds two unknowns"),
    SUB_I(0x15, 0, "subtract int"),
    SUB_D(0x16, 0, "subtract double"),
    SUB_U(0x17, 0, "subtract two unknowns"),
    MUL_I(0x18, 0, "multiply int"),
    MUL_D(0x19, 0, "multiply double"),
    MUL_U(0x1a, 0, "multiply two unknowns"),
    DIV_I(0x1b, 0, "divide int"),
    DIV_D(0x1c, 0, "divide double"),
    DIV_U(0x1d, 0, "divide two unknowns"),
    MOD_I(0x1e, 0, "module int"),
    MOD_U(0x1f, 0, "module two unknowns"),
    
    
    
    
    NEG_B(0x20, 0, "negate boolean"),
    EQ_N(0x21,  0, "logical equal (null)"),
    EQ_B(0x22,  0, "logical equal (boolean)"),
    EQ_I(0x23,  0, "logical equal (integer)"),
    EQ_D(0x24,  0, "ilogicalf equal (double)"),
    EQ_S(0x25,  0, "logical equal (string)"),
    EQ_U(0x26,  0, "logical equal (unknown)"),
    EQ_O(0x27,  0, "logical equal (object)"),
    NEQ_N(0x28, 0, "logical not equal (null)"),
    NEQ_B(0x29, 0, "logical not equal (boolean)"),
    NEQ_I(0x2a, 0, "logical not equal (integer)"),
    NEQ_D(0x2b, 0, "logical not equal (double)"),
    NEQ_S(0x2c, 0, "logical not equal (string)"),
    NEQ_U(0x2d, 0, "logical not equal (unknown)"),
    NEQ_O(0x2e, 0, "logical not equal (object)"),
    LT_I(0x30,  0, "logical less than (integer)"),
    LT_D(0x31,  0, "logical less than (double)"),
    LT_S(0x32,  0, "logical less than (string)"),
    LT_U(0x33,  0, "logical less than (unknown)"),
    GT_I(0x34,  0, "logical greater than (integer)"),
    GT_D(0x35,  0, "logical greater than (double)"),
    GT_S(0x36,  0, "logical greater than (string)"),
    GT_U(0x37,  0, "logical greater than (unknown)"),
    LTE_I(0x38, 0, "logical less than or equal (integer)"),
    LTE_D(0x39, 0, "logical less than or equal (double)"),
    LTE_S(0x3a, 0, "logical less than or equal (string)"),
    LTE_U(0x3b, 0, "logical less than or equal (unknown)"),
    GTE_I(0x3c, 0, "logical greater than or equal (integer)"),
    GTE_D(0x3d, 0, "logical greater than or equal (double)"),
    GTE_S(0x3e, 0, "logical greater than or equal (string)"),
    GTE_U(0x3f, 0, "logical greater than or equal (unknown)"),
    
    
    
    
    I_TO_D(0x41, 1, "convert int to double"),
    I_TO_S(0x42, 1, "convert int to string"),
    D_TO_I(0x43, 1, "convert double to integer"),
    D_TO_S(0x44, 1, "convert double to string"),
    B_TO_S(0x45, 1, "convert bool to string"),
    O_TO_S(0x46, 1, "convert object to strin"),
    N_TO_S(0x47, 1, "convert null to string"),
    R_TO_S(0x48, 1, "convert reference to string"),
    S_TO_B(0x4d, 1, "convert string to boolean"),
    S_TO_I(0x4e, 1, "convert string to integer"),
    S_TO_D(0x4f, 1, "convert string to double"),
    
    
    
    
    BRACH(0x61,   0, "branch if top of stack is NOT true"),
    ASSIGN(0x62,  0, "assigns a value"),
    
    DECL_B(0x63,  0, "declare a boolean"),
    DECL_I(0x64,  0, "declare a int"),
    DECL_D(0x65,  0, "declare a double"),
    DECL_S(0x66,  0, "declare a string"),
    DECL_U(0x67,  0, "declare a value (loose type)"),
    DECL_O(0x68,  0, "declare a object"),
    
    DECLR_B(0x69, 0, "declare a boolean reference"),
    DECLR_I(0x6a, 0, "declare a int reference"),
    DECLR_D(0x6b, 0, "declare a double reference"),
    DECLR_S(0x6c, 0, "declare a string reference"),
    DECLR_U(0x6d, 0, "declare a value (loose type) reference"),
    DECLR_O(0x6f, 0, "declare a object reference"),
    
    
    
    
    ST_PUS(0x71,    0, "creates a local variable stack"),
    ST_POP(0x72,    0, "releases the top variable stack"),
    
    CALL_C(0x73,    -1, "call a compiled method"),
    CALL_O(0x74,    -1, "call an objects method"),
    CALL_S(0x75,    -1, "call a system method (java)"),
    
    GOTO(0x76,      4, "goes to the specified location"),
    
    PUSH_H(0x78,    0, "load a hardcoded value onto stack"),
    PUSH_V(0x79,    -1, "load a variable onto stack"),
    PUSH_R(0x7a,    -1, "load a reference onto stack"),
    
    RETURN_S(0x7e,  0, "return value at the top of the local stack"),
    RETURN_V(0x7f,  0, "return void");
    
    
    
    private ByteCodeOperation(int code, int bytesAdd, String statement)
    {
        if (code < 0 || 0xff < code)
        {
            throw new IllegalArgumentException("code must be a byte");
        }
        this.code       = (byte) code;
        this.statement  = statement;
        this.bytesAdd   = bytesAdd;
    }
    
    private int code;
    
    private String statement;
    
    private int bytesAdd;
    
    public int getBytesAdd()
    {
        return bytesAdd;
    }
    
    public final byte getCode()
    {
        return (byte) (code & 0xff);
    }
    
    @Override
    public String toString()
    {
        return "[" + name() + "-" + code + ": " + statement + "]";
    }
    
    private static ByteCodeOperation[] operations;
    
    static
    {
        operations = new ByteCodeOperation[0xff + 1];
        ByteCodeOperation[] values = values();
        for(ByteCodeOperation value : values)
        {
            operations[value.getCode()] = value;
        }
    }
    
    public static ByteCodeOperation getFromCode(byte code)
    {
        return operations[code];
    }
}
