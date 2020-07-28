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
public class Math_Mod implements ICompilingOperation
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
            writer.writeOperation(ByteCodeOperation.MOD_I);
            environment.push(new Value(ValueType.INTEGER, null));
            return;
        }
        throw new IllegalArgumentException("invalid datatypes for divide");
    }
    

    @Override
    public int getPresedence ()
    {
        return 2;
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
