/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.lang;

import com.rf.bytescript.environment.ExecutionEnvironment;
import com.rf.bytescript.environment.ICompilingOperation;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeOperation;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;

/**
 *
 * @author REx
 */
public class Compare_LessThan implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer) 
            throws ByteScriptSyntaxException
    {
        Value right = environment.pop();
        Value left = environment.pop();
        
        
        if (left.type == ValueType.UNKNOWN ||
                 right.type == ValueType.UNKNOWN)
        {
            writer.writeOperation(ByteCodeOperation.LT_U);
            environment.push(new Value(ValueType.BOOLEAN, null));
        }
        else
        {
            switch(left.type)
            {
                case INTEGER:
                    if (right.type == ValueType.INTEGER)
                    {
                        writer.writeOperation(ByteCodeOperation.LT_I);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.DOUBLE)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_D, 1);
                        writer.writeOperation(ByteCodeOperation.LT_D);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.STRING)
                    {
                        writer.writeConversion(ByteCodeOperation.S_TO_I, 0);
                        writer.writeOperation(ByteCodeOperation.LT_I);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new ByteScriptSyntaxException(
                                "type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                case DOUBLE:
                    if (right.type == ValueType.INTEGER)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_D, 0);
                        writer.writeOperation(ByteCodeOperation.LT_D);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.DOUBLE)
                    {
                        writer.writeOperation(ByteCodeOperation.LT_D);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.STRING)
                    {
                        writer.writeConversion(ByteCodeOperation.S_TO_D, 0);
                        writer.writeOperation(ByteCodeOperation.LT_D);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new ByteScriptSyntaxException(
                                "greater than type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                case STRING:
                    if (right.type == ValueType.STRING)
                    {
                        writer.writeOperation(ByteCodeOperation.LT_S);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.INTEGER)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_S, 0);
                        writer.writeOperation(ByteCodeOperation.LT_S);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else if (right.type == ValueType.DOUBLE)
                    {
                        writer.writeConversion(ByteCodeOperation.D_TO_S, 0);
                        writer.writeOperation(ByteCodeOperation.LT_S);
                        environment.push(new Value(ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new ByteScriptSyntaxException(
                                "greater than type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                default:
                    throw new ByteScriptSyntaxException(
                                "greater than type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
            }
        }
    }

    @Override
    public boolean isAlwaysLast ()
    {
        return false;
    }

    @Override
    public int getPresedence ()
    {
        return 0;
    }

    @Override
    public ValueType getType ()
    {
        return ValueType.OPERATOR;
    }
    
}
