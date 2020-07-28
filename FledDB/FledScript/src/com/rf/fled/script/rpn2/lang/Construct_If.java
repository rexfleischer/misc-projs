/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.lang;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.rpn2.RPN2Compiler;
import com.rf.fled.script.rpn2.environment.CompilingEnvironment;
import com.rf.fled.script.rpn2.environment.ICompilingDeclaration;
import com.rf.fled.script.rpn2.util.ByteCodeOperation;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.util.CompileUtil;
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public class Construct_If implements ICompilingDeclaration
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
        int start = position;
        
        /**
         * first check that there is actually an if
         */
        if (!statement.get(position).name.equals("if"))
        {
            throw new FledSyntaxException(
                    "invalid beginning construct for an if statement");
        }
        
        /**
         * this will let us know if multiple else statements in a
         * row occur
         */
        boolean onElse = false;
        boolean onFirst = true;
        while(true)
        {
            if (statement.get(position++).name.equals("if"))
            {
                if (onElse)
                {
                    /**
                     * this means there is an if statement right after
                     * this statement, so we are done with these
                     */
                    break;
                }
                
                LinkedList<TokenPair> params = new LinkedList<TokenPair>();
                position += CompileUtil.getMatchingParas(position, statement, params, "(", ")");
                try
                {
                    writer.writeStatement(params, environment);
                }
                catch (Exception ex)
                {
                    throw new FledSyntaxException(
                            "unable to write logic in if statement", ex);
                }
                
                if (!onFirst)
                {
                    writer.resolveTag("go_to_next_else");
                }
                
                onFirst = false;
                
                writer.writeOperation(ByteCodeOperation.BRACH);
                Value if_result = environment.pop();
                if (if_result.type != Value.ValueType.BOOLEAN)
                {
                    throw new FledSyntaxException(
                            "if statement does not result to a boolean");
                }
                writer.setTag_Temp("if_statement_is_false");
                
                position += runInternal(environment, 
                                        writer, 
                                        statement, 
                                        compiler, 
                                        position) - 1;
                
                writer.setTag_Temp("finished_else_if");
                writer.resolveTag("if_statement_is_false");
            }
            else if (statement.get(position).name.equals("else"))
            {
                if (onFirst)
                {
                    /**
                     * i dont think this will ever happen, but 
                     * its more of a sanity check.
                     */
                    throw new FledSyntaxException(
                            "you cannot start an if statement with an if");
                }
                position++;
                if (statement.get(position).name.equals("if"))
                {
                    writer.setTag_Temp("go_to_next_else");
                    continue;
                }
                
                if (onElse)
                {
                    throw new FledSyntaxException(
                            "you cannot have multiple else statements");
                }
                onElse = true;
                
                position += runInternal(environment, 
                                        writer, 
                                        statement, 
                                        compiler, 
                                        position);
                
                writer.setTag_Temp("finished_else_if");
                
                break;
            }
            else
            {
                break;
            }
        }
        
        writer.resolveTag("finished_else_if");
        
        return position - start;
    }
    
    private int runInternal(
            CompilingEnvironment environment, 
            ByteCodeWriter writer, 
            List<TokenPair> statement, 
            RPN2Compiler compiler,
            int position) throws FledSyntaxException
    {
        int start = position;
        
        boolean needsScopePush = false;
        LinkedList<TokenPair> params = new LinkedList<TokenPair>();
        if (statement.get(position).name.equals("{"))
        {
            position += CompileUtil.getMatchingParas(
                    position, statement, params, "{", "}");
            needsScopePush = true;
        }
        else
        {
            position += CompileUtil.getUntilMatch(
                    position, statement, params, ";");
        }

        if (needsScopePush)
        {
            writer.writePushStack(false);
        }
        try
        {
            compiler.compile0(params, writer, environment, 0);
        }
        catch (Exception ex)
        {
            throw new FledSyntaxException(
                    "unable to compile sub scope of else statement", ex);
        }
        if (needsScopePush)
        {
            writer.writePopStack(false);
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
