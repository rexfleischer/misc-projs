/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.environment.ICompilingOperation;
import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public class Function_Return implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment enviornment, ByteCodeWriter writer)
            throws FledSyntaxException
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
            throw new FledSyntaxException("cannot return multiple things");
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
    public TokenType getType ()
    {
        return TokenType.FUNCTION;
    }

    @Override
    public boolean isAlwaysLast ()
    {
        return true;
    }
    
}
