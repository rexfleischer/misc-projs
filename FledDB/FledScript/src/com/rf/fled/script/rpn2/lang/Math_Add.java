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
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public class Math_Add implements ICompilingOperation
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
            writer.writeOperation(ByteCodeOperation.ADD_I);
            environment.push(new Value(Value.ValueType.INTEGER, null));
            return;
        }
        if (left.type == Value.ValueType.DOUBLE && 
                right.type == Value.ValueType.DOUBLE)
        {
            writer.writeOperation(ByteCodeOperation.ADD_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
            return;
        }
        if (left.type == Value.ValueType.INTEGER  && 
                right.type == Value.ValueType.DOUBLE)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 1);
            writer.writeOperation(ByteCodeOperation.ADD_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
            return;
        }
        if (left.type == Value.ValueType.DOUBLE  && 
                right.type == Value.ValueType.INTEGER)
        {
            writer.writeConversion(ByteCodeOperation.I_TO_D, 0);
            writer.writeOperation(ByteCodeOperation.ADD_D);
            environment.push(new Value(Value.ValueType.DOUBLE, null));
            return;
        }
        if (left.type == Value.ValueType.STRING && 
                right.type == Value.ValueType.STRING)
        {
            writer.writeOperation(ByteCodeOperation.ADD_S);
            environment.push(new Value(Value.ValueType.STRING, null));
        }
        if (left.type == Value.ValueType.STRING)
        {
            switch(right.type)
            {
                case BOOLEAN:
                    writer.writeConversion(ByteCodeOperation.B_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case INTEGER:
                    writer.writeConversion(ByteCodeOperation.I_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case DOUBLE:
                    writer.writeConversion(ByteCodeOperation.D_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case NULL:
                    writer.writeConversion(ByteCodeOperation.N_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case OBJECT:
                    writer.writeConversion(ByteCodeOperation.O_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case REFERENCE:
                    writer.writeConversion(ByteCodeOperation.R_TO_S, 0);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
            }
        }
        if (right.type == Value.ValueType.STRING)
        {
            switch(left.type)
            {
                case BOOLEAN:
                    writer.writeConversion(ByteCodeOperation.B_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case INTEGER:
                    writer.writeConversion(ByteCodeOperation.I_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case DOUBLE:
                    writer.writeConversion(ByteCodeOperation.D_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case NULL:
                    writer.writeConversion(ByteCodeOperation.N_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case OBJECT:
                    writer.writeConversion(ByteCodeOperation.O_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
                    
                case REFERENCE:
                    writer.writeConversion(ByteCodeOperation.R_TO_S, 1);
                    writer.writeOperation(ByteCodeOperation.ADD_S);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    return; 
            }
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
