
package com.rf.fled.script.rpn2.util;

import java.nio.charset.Charset;

/**
 *
 * @author REx
 */
public class ByteCodeReader
{
    private ByteArray code;
    
    public ByteCodeReader(ByteArray code)
    {
        this.code = code;
    }
    
    public ByteCodeReader(byte[] code)
    {
        this.code = new ByteArray(code);
    }
    
    public byte getCode(int position)
    {
        return code.readByte(position);
    }
    
    public ByteArray getAllCode()
    {
        return code;
    }
    
    public int readValue(int position, Value value)
    {
        byte type = code.readByte(position);
        switch(ByteCodeOperation.getFromCode(type))
        {
            case TYPE_N:
                value.type = Value.ValueType.NULL;
                value.value = null;
                return 1;
                
            case TYPE_B:
                value.type = Value.ValueType.BOOLEAN;
                value.value = code.readBoolean(position + 1);
                return 2;
                
            case TYPE_I:
                value.type = Value.ValueType.INTEGER;
                value.value = code.readInt(position + 1);
                return 5;
                
            case TYPE_D:
                value.type = Value.ValueType.DOUBLE;
                value.value = Double.longBitsToDouble(
                        code.readLong(position + 1));
                return 9;
                
            case TYPE_S:
                value.type = Value.ValueType.STRING;
                int length = code.readInt(position + 1);
                byte[] bytes = new byte[length];
                System.arraycopy(code.array(), position + 5, bytes, 0, length);
                value.value = new String(bytes, Charset.forName("UTF-8"));
                return length + 5;
                
            default:
                throw new IllegalArgumentException(
                        value.type + " is an unknown type for reading");
        }
    }

    public int size ()
    {
        return code.compacityUsed();
    }
    
    public String dumpCode()
    {
        StringBuilder string = new StringBuilder();
        
        int line = 0;
        for(int i = 0; i < code.compacityUsed(); i++)
        {
            string.append(line++);
            string.append(": ");
            
            byte _code = this.code.readByte(i);
            if (_code > 0x7f || _code < 0)
            {
                string.append("illegal op code: ");
                string.append(_code);
                continue;
            }
            else
            {
                ByteCodeOperation op = ByteCodeOperation.getFromCode(_code);
                string.append(op.toString());
                if (op.getBytesAdd() == -1)
                {
                    int length = code.readInt(i + 1);
//                    byte[] bytes = new byte[length];
//                    System.arraycopy(code.array(), i + 5, bytes, 0, length);
//                    String value = new String(bytes, Charset.forName("UTF-8"));
//                    i += (length + 5);
                    string.append(" ");
//                    string.append(value);
                    for(int ii = 0; ii < length; ii++, i++)
                    {
                        string.append(code.readByte(i + 1));
                    }
                }
                else if (op.getBytesAdd() > 0)
                {
                    string.append(" ");
                    for(int ii = 0; ii < op.getBytesAdd(); ii++, i++)
                    {
                        string.append(code.readByte(i + 1));
                    }
                }
            }
            string.append("\n");
        }
        
        return string.toString();
    }
}
