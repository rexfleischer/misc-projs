/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.RPN2Compiler;
import com.rf.fled.script.rpn2.environment.CompilingEnvironment;
import com.rf.fled.script.rpn2.environment.ICompilingDeclaration;
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
public class Declare_Unknown implements ICompilingDeclaration
{
    @Override
    public int compile (
            CompilingEnvironment environment, 
            ByteCodeWriter writer, 
            List<TokenPair> statement, 
            RPN2Compiler compiler,
            int position) 
            throws FledSyntaxException
    {
        if (statement.size() < position + 4)
        {
            throw new FledSyntaxException("unable to finish declaration");
        }
        int start = position;
        
        if (statement.get(position++).type != TokenType.DECLARATION)
        {
            throw new FledSyntaxException("illegal delcaration of a boolean");
        }
        
        boolean isReference = false;
        if (statement.get(position + 1).type == TokenType.OPERATOR &&
                statement.get(position + 1).name.equals("*"))
        {
            isReference = true;
            position++;
        }
        
        TokenPair name = statement.get(position++);
        if (name.type != TokenType.VARIABLE)
        {
            throw new FledSyntaxException("illegal delcaration of a boolean");
        }
        
        TokenPair operator = statement.get(position++);
        if (operator.type != TokenType.ASSIGNMENT ||
                !operator.name.equals("="))
        {
            throw new FledSyntaxException("illegal delcaration of a boolean");
        }
        
        TokenPair value = statement.get(position++);
        if (value.type != TokenType.NULL &&
            value.type != TokenType.BOOLEAN &&
            value.type != TokenType.INTEGER &&
            value.type != TokenType.DOUBLE &&
            value.type != TokenType.STRING &&
            value.type != TokenType.VARIABLE)
        {
            throw new FledSyntaxException("illegal value of a boolean");
        }
        
        if (statement.get(position++).type != TokenType.SEPERATOR)
        {
            throw new FledSyntaxException("statements must end with a ';'");
        }
        
        try
        {
            switch(value.type)
            {
                case NULL:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeNull();
                    environment.push(new Value(Value.ValueType.NULL, null));
                    break;
                case BOOLEAN:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeBoolean((Boolean) value.value);
                    environment.push(new Value(Value.ValueType.BOOLEAN, null));
                    break;
                case INTEGER:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeInteger((Integer) value.value);
                    environment.push(new Value(Value.ValueType.INTEGER, null));
                    break;
                case DOUBLE:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeDouble((Double) value.value);
                    environment.push(new Value(Value.ValueType.DOUBLE, null));
                    break;
                case STRING:
                    writer.writeOperation(ByteCodeOperation.PUSH_H);
                    writer.writeString((String) value.value);
                    environment.push(new Value(Value.ValueType.STRING, null));
                    break;

    //            case FUNCTION:
    //                
    //                break;

                case VARIABLE:
                    writer.writePushVariable(value.name);
                    environment.push(environment.getVariable(value.name));
                    break;

                default:
                    throw new FledSyntaxException("cannot assign value");
            }
        }
        catch(Exception ex)
        {
            throw new FledSyntaxException("was unable to assign value", ex);
        }
        
        try
        {
            if (isReference)
            {
                writer.writeOperation(ByteCodeOperation.DECLR_U);
                environment.declareReference(name.name, Value.ValueType.UNKNOWN, environment.pop());
            }
            else
            {
                writer.writeOperation(ByteCodeOperation.DECL_U);
                environment.declareVariable(name.name, Value.ValueType.UNKNOWN, environment.pop());
            }
        }
        catch(FledExecutionException ex)
        {
            throw new FledSyntaxException("could not declare value", ex);
        }
        
        
        writer.writeString(name.name);
        
        return position - start;
    }

    @Override
    public int getPresedence ()
    {
        return 0;
    }

    @Override
    public TokenType getType ()
    {
        return TokenType.DECLARATION;
    }
}
