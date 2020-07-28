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
import com.rf.bytescript.value.ValueType;

/**
 *
 * @author REx
 */
public class Function_Return implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment enviornment, ByteCodeWriter writer)
            throws ByteScriptSyntaxException
    {
        /**
         * if return is the only thing in the statement, then we 
         */
        if (enviornment.peek() != null)
        {
            writer.writeOperation(ByteCodeOperation.RETURN_S);
            return;
        }
        enviornment.pop();
        if (enviornment.peek() != null)
        {
            throw new ByteScriptSyntaxException("cannot return multiple things");
        }
        writer.writeOperation(ByteCodeOperation.RETURN_V);
        return;
    }
    

    @Override
    public int getPresedence ()
    {
        return 1;
    }

    @Override
    public ValueType getType ()
    {
        return ValueType.FUNCTION;
    }

    @Override
    public boolean isAlwaysLast ()
    {
        return true;
    }
    
}
