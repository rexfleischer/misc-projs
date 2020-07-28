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
public class Math_Div implements ICompilingOperation
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
            writer.writeOperation(ByteCodeOperation.DIV_I);
            environment.push(new Value(Value.ValueType.INTEGER, null));
            return;
        }
        if (left.type == Value.ValueType.DOUBLE && 
                right.type == Value.ValueType.DOUBLE)
        {
            writer.writeOperation(ByteCodeOperation.DIV_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
            return;
        }
        if (left.type == Value.ValueType.INTEGER  && 
                right.type == Value.ValueType.DOUBLE)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 1);
            writer.writeOperation(ByteCodeOperation.DIV_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
            return;
        }
        if (left.type == Value.ValueType.DOUBLE  && 
                right.type == Value.ValueType.INTEGER)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 0);
            writer.writeOperation(ByteCodeOperation.DIV_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
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
