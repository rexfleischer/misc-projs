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
public class Declare_Integer implements ICompilingDeclaration
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
            throw new FledSyntaxException("illegal delcaration of an Integer");
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
            throw new FledSyntaxException("illegal delcaration of an Integer");
        }
        
        TokenPair operator = statement.get(position++);
        if (operator.type != TokenType.ASSIGNMENT ||
                !operator.name.equals("="))
        {
            throw new FledSyntaxException("illegal delcaration of an Integer");
        }
        
        TokenPair value = statement.get(position++);
        if (value.type != TokenType.INTEGER &&
                value.type != TokenType.VARIABLE)
        {
            throw new FledSyntaxException("illegal value of an Integer");
        }
        
        if (statement.get(position++).type != TokenType.SEPERATOR)
        {
            throw new FledSyntaxException("statements must end with a ';'");
        }
        
        if (value.type == TokenType.INTEGER)
        {
            writer.writeOperation(ByteCodeOperation.PUSH_H);
            writer.writeInteger((Integer) value.value);
        }
        else
        {
            writer.writePushVariable(value.name);
        }
        if (isReference)
        {
            writer.writeOperation(ByteCodeOperation.DECLR_I);
        }
        else
        {
            writer.writeOperation(ByteCodeOperation.DECL_I);
        }
        writer.writeString(name.name);
        try
        {
            environment.declareVariable(
                    name.name, Value.ValueType.INTEGER, new Value(Value.ValueType.INTEGER, null));
        }
        catch (FledExecutionException ex)
        {
            throw new FledSyntaxException(
                    "error occurred while declaring a variable", ex);
        }
        
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
