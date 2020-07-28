/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.lang;

import com.rf.bytescript.compiler.ICompiler;
import com.rf.bytescript.environment.CompilingEnvironment;
import com.rf.bytescript.environment.ICompilingDeclaration;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeOperation;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.util.CompileUtil;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
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
            List<Value> statement, 
            ICompiler compiler,
            int position) 
            throws ByteScriptSyntaxException
    {
        int start = position;
        
        /**
         * first check that there is actually an if
         */
        if (!statement.get(position).getValueAsString().equals("if"))
        {
            throw new ByteScriptSyntaxException(
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
            if (statement.get(position++).getValueAsString().equals("if"))
            {
                if (onElse)
                {
                    /**
                     * this means there is an if statement right after
                     * this statement, so we are done with these
                     */
                    break;
                }
                
                LinkedList<Value> params = new LinkedList<Value>();
                position += CompileUtil.getMatchingParas(position, statement, params, "(", ")");
                try
                {
                    writer.writeStatement(params, environment);
                }
                catch (Exception ex)
                {
                    throw new ByteScriptSyntaxException(
                            "unable to write logic in if statement", ex);
                }
                
                if (!onFirst)
                {
                    writer.resolveTag("go_to_next_else");
                }
                
                onFirst = false;
                
                writer.writeOperation(ByteCodeOperation.BRACH);
                Value if_result = environment.pop();
                if (if_result.type != ValueType.BOOLEAN)
                {
                    throw new ByteScriptSyntaxException(
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
            else if (statement.get(position).getValueAsString().equals("else"))
            {
                if (onFirst)
                {
                    /**
                     * i dont think this will ever happen, but 
                     * its more of a sanity check.
                     */
                    throw new ByteScriptSyntaxException(
                            "you cannot start an if statement with an if");
                }
                position++;
                if (statement.get(position).getValueAsString().equals("if"))
                {
                    writer.setTag_Temp("go_to_next_else");
                    continue;
                }
                
                if (onElse)
                {
                    throw new ByteScriptSyntaxException(
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
            List<Value> statement, 
            ICompiler compiler,
            int position) throws ByteScriptSyntaxException
    {
        int start = position;
        
        boolean needsScopePush = false;
        LinkedList<Value> params = new LinkedList<Value>();
        if (statement.get(position).getValueAsString().equals("{"))
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
            compiler.compile(params, writer, environment, 0);
        }
        catch (Exception ex)
        {
            throw new ByteScriptSyntaxException(
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
    public ValueType getType ()
    {
        return ValueType.DECLARATION;
    }
    
}
