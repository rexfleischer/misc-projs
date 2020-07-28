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
public class Compare_LessThanEqual implements ICompilingOperation
{

    @Override
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer) 
            throws FledSyntaxException
    {
        Value right = environment.pop();
        Value left = environment.pop();
        
        
        if (left.type == Value.ValueType.UNKNOWN ||
                 right.type == Value.ValueType.UNKNOWN)
        {
            writer.writeOperation(ByteCodeOperation.LTE_U);
            environment.push(new Value(Value.ValueType.BOOLEAN, null));
        }
        else
        {
            switch(left.type)
            {
                case INTEGER:
                    if (right.type == Value.ValueType.INTEGER)
                    {
                         writer.writeOperation(ByteCodeOperation.LTE_I);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.DOUBLE)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_D, 1);
                        writer.writeOperation(ByteCodeOperation.LTE_D);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.STRING)
                    {
                        writer.writeConversion(ByteCodeOperation.S_TO_I, 0);
                        writer.writeOperation(ByteCodeOperation.LTE_I);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new FledSyntaxException(
                                "type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                case DOUBLE:
                    if (right.type == Value.ValueType.INTEGER)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_D, 0);
                        writer.writeOperation(ByteCodeOperation.LTE_D);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.DOUBLE)
                    {
                        writer.writeOperation(ByteCodeOperation.LTE_D);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.STRING)
                    {
                        writer.writeConversion(ByteCodeOperation.S_TO_D, 0);
                        writer.writeOperation(ByteCodeOperation.LTE_D);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new FledSyntaxException(
                                "greater than type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                case STRING:
                    if (right.type == Value.ValueType.STRING)
                    {
                        writer.writeOperation(ByteCodeOperation.LTE_S);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.INTEGER)
                    {
                        writer.writeConversion(ByteCodeOperation.I_TO_S, 0);
                        writer.writeOperation(ByteCodeOperation.LTE_S);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else if (right.type == Value.ValueType.DOUBLE)
                    {
                        writer.writeConversion(ByteCodeOperation.D_TO_S, 0);
                        writer.writeOperation(ByteCodeOperation.LTE_S);
                        environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    }
                    else 
                    {
                        throw new FledSyntaxException(
                                "greater than type mismatch: " + left.type + 
                                " cannot be compared with " + right.type);
                    }
                    break;
                default:
                    throw new FledSyntaxException(
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
    public TokenType getType ()
    {
        return TokenType.OPERATOR;
    }
    
}
