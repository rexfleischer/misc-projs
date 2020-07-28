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
public class Compare_Negate implements ICompilingOperation
{
    
    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer) 
            throws ByteScriptSyntaxException
    {
        Value left = environment.pop();
        
        if (left.type == ValueType.BOOLEAN)
        {
            writer.writeOperation(ByteCodeOperation.NEG_B);
            environment.push(new Value(ValueType.BOOLEAN, null));
        }
        else
        {
            throw new ByteScriptSyntaxException(
                    "negation type mismatch: can only negate booleans");
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
