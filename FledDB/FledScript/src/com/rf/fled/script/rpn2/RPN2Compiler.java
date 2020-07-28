
package com.rf.fled.script.rpn2;

import com.rf.fled.script.UndefinedVariableException;
import com.rf.fled.script.tokenizer.InfixToRPNConverter;
import com.rf.fled.script.ExpressionCompiler;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.FledScriptInitException;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.rpn2.environment.CompilingEnvironment;
import com.rf.fled.script.rpn2.util.ByteCodeReader;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;
import com.rf.fled.script.rpn2.environment.FunctionSet_Compiled;
import com.rf.fled.script.rpn2.environment.FunctionSet_Lang;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import com.rf.fled.script.tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class RPN2Compiler implements ExpressionCompiler
{
    private Properties  properties;
    
    private InfixToRPNConverter converter;
    
    private FunctionSet_Compiled code;
    
    private FunctionSet_Lang operations;

    @Override
    public void init(FunctionSet functions, Properties properties)
    {
        this.properties = properties;
        this.operations = new FunctionSet_Lang();
        this.converter  = new InfixToRPNConverter(this.operations);
        this.code       = new FunctionSet_Compiled();
    }

    @Override
    public ExpressionExecutor compile(String expression) 
            throws FledSyntaxException, UndefinedFunctionException
    {
        CompilingEnvironment enviornment = null;
        try
        {
            enviornment = new CompilingEnvironment();
        }
        catch(FledScriptInitException ex)
        {
            throw new FledSyntaxException("could not initialize environment", ex);
        }
        
        ArrayList<TokenPair> tokens = (new Tokenizer(operations)).tokenize(expression);
        
        ByteCodeWriter main = null;
        try
        {
            main = compile0(tokens, null, enviornment, 0);
        }
        catch(UndefinedVariableException ex)
        {
            throw new FledSyntaxException("could not compile main", ex);
        }
        
        code.setMain(new ByteCodeReader(main.finished()));
        
        return new RPN2InitExecutor(code, properties);
    }
    
    public ByteCodeWriter compile0(
            List<TokenPair> infix, 
            ByteCodeWriter writer,
            CompilingEnvironment enviornment,
            int position) 
            throws  FledSyntaxException, 
                    UndefinedFunctionException, 
                    UndefinedVariableException
    {
        if (writer == null)
        {
            writer = new ByteCodeWriter(converter);
        }
        LinkedList<TokenPair> statement = new LinkedList<TokenPair>();
        
        for( ; position < infix.size(); position++)
        {
            TokenPair token = infix.get(position);
            
            if (token.type == TokenType.SEPERATOR)
            {
                TokenPair assignment = null;
                for(TokenPair pair : statement)
                {
                    if (pair.type == TokenType.ASSIGNMENT)
                    {
                        assignment = pair;
                        break;
                    }
                }
                
                if (assignment!= null)
                {
                    writer.writeAssignment(statement, assignment, enviornment);
                }
                else
                {
                    writer.writeStatement(statement, enviornment);
                }
                statement = new LinkedList<TokenPair>();
            }
            else if (token.type == TokenType.DECLARATION)
            {
                if (!statement.isEmpty())
                {
                    throw new FledSyntaxException("illegal declaration placement");
                }
                
                position += writer.writeDeclaration(infix, position, this, enviornment) - 1;
            }
            else
            {
                statement.add(token);
            }
        }
        
        if (!statement.isEmpty())
        {
            throw new FledSyntaxException("illegal end of a statement");
        }
        
        return writer;
    }
    
}
