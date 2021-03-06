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
public class Math_Sub implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer)
            throws ByteScriptSyntaxException
    {
        Value right = environment.pop();
        Value left = environment.pop();
        
        if (left.type == ValueType.INTEGER && 
                right.type == ValueType.INTEGER)
        {
            writer.writeOperation(ByteCodeOperation.SUB_I);
            environment.push(new Value(ValueType.INTEGER, null));
            return;
        }
        if (left.type == ValueType.DOUBLE && 
                right.type == ValueType.DOUBLE)
        {
            writer.writeOperation(ByteCodeOperation.SUB_D);
            environment.push(new Value(ValueType.DOUBLE, null));
            return;
        }
        if (left.type == ValueType.INTEGER  && 
                right.type == ValueType.DOUBLE)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 1);
            writer.writeOperation(ByteCodeOperation.SUB_D);
            environment.push(new Value(ValueType.DOUBLE, null));
            return;
        }
        if (left.type == ValueType.DOUBLE  && 
                right.type == ValueType.INTEGER)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 0);
            writer.writeOperation(ByteCodeOperation.SUB_D);
            environment.push(new Value(ValueType.DOUBLE, null));
            return;
        }
        throw new IllegalArgumentException("invalid datatypes for subtract");
    }
    

    @Override
    public int getPresedence ()
    {
        return 1;
    }

    @Override
    public ValueType getType ()
    {
        return ValueType.OPERATOR;
    }

    @Override
    public boolean isAlwaysLast ()
    {
        return false;
    }
    
}
