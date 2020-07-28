/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.environment.CompilingEnvironment;
import com.rf.fled.script.rpn2.environment.ICompilingAssignment;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import java.util.List;

/**
 *
 * @author REx
 */
public class Assignment_Equal implements ICompilingAssignment
{

    @Override
    public void compile (
            CompilingEnvironment enviornment, 
            ByteCodeWriter writer, 
            List<TokenPair> statement) 
            throws FledSyntaxException
    {
        TokenPair name = statement.get(0);
        if (name.type != TokenType.VARIABLE)
        {
            throw new FledSyntaxException("illegal assignment operation");
        }
        
        TokenPair operator = statement.get(1);
        if (operator.type != TokenType.ASSIGNMENT ||
                !operator.name.equals("="))
        {
            throw new FledSyntaxException("illegal delcaration of a boolean");
        }
        
        TokenPair assignee = statement.get(2);
        try
        {
            switch(assignee.type)
            {
                case NULL:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeNull();
                    enviornment.setVariable(name.name, new Value(Value.ValueType.NULL, null));
                    break;
                case BOOLEAN:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeBoolean((Boolean) assignee.value);
                    enviornment.setVariable(name.name, new Value(Value.ValueType.BOOLEAN, null));
                    break;
                case INTEGER:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeInteger((Integer) assignee.value);
                    enviornment.setVariable(name.name, new Value(Value.ValueType.INTEGER, null));
                    break;
                case DOUBLE:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeDouble((Double) assignee.value);
                    enviornment.setVariable(name.name, new Value(Value.ValueType.DOUBLE, null));
                    break;
                case STRING:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeString((String) assignee.value);
                    enviornment.setVariable(name.name, new Value(Value.ValueType.STRING, null));
                    break;

    //            case FUNCTION:
    //                
    //                break;

                case VARIABLE:
                    writer.writePushVariable(assignee.name);
                    enviornment.setVariable(name.name, enviornment.getVariable(assignee.name));
                    break;

                default:
                    throw new FledSyntaxException("cannot assign value");
            }
        }
        catch(Exception ex)
        {
            throw new FledSyntaxException("was unable to assign value", ex);
        }
        
        writer.writeSetVariable(name.name);
        
    }

    @Override
    public int getPresedence ()
    {
        return 0;
    }

    @Override
    public TokenType getType ()
    {
        return TokenType.ASSIGNMENT;
    }
    
}
