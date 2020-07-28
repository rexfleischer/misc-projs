/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.environment.ICompilingOperation;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public class Math_Mod implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer)
            throws FledSyntaxException
    {
        Value right = environment.pop();
        Value left = environment.pop();
        
        if (left.type == Value.ValueType.INTEGER &&
                right.type == Value.ValueType.INTEGER)
        {
            writer.writeOperation(ByteCodeOperation.MOD_I);
            environment.push(new Value(Value.ValueType.INTEGER, null));
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
    public TokenType getType ()
    {
        return TokenType.OPERATOR;
    }

    @Override
    public boolean isAlwaysLast ()
    {
        return false;
    }
    
}
