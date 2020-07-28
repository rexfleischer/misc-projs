/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.rpn2.environment.ICompilingOperation;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public class Compare_Negate implements ICompilingOperation
{
    
    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer) 
            throws FledSyntaxException
    {
        Value left = environment.pop();
        
        if (left.type == Value.ValueType.BOOLEAN)
        {
            writer.writeOperation(ByteCodeOperation.NEG_B);
            environment.push(new Value(Value.ValueType.BOOLEAN, null));
        }
        else
        {
            throw new FledSyntaxException(
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
    public TokenType getType ()
    {
        return TokenType.OPERATOR;
    }
}
