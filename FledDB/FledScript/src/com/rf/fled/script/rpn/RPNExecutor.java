/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn;

import com.rf.fled.script.rpn.controls.RPNFunction;
import com.rf.fled.script.rpn.controls.RPNConstruct;
import com.rf.fled.script.rpn.controls.RPNStatementQueue;
import com.rf.fled.script.rpn.controls.RPNStatement;
import com.rf.fled.script.rpn.controls.RPNStatementConstruct;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author REx
 */
public class RPNExecutor implements ExpressionExecutor
{
    private FunctionSet functions;
    
    private RPNStatement[] statements;
    
    protected RPNExecutor(List<RPNStatement> statements, FunctionSet functions)
    {
        this.statements = new RPNStatement[statements.size()];
        statements.toArray(this.statements);
        this.functions  = functions;
    }

    @Override
    public TokenPair execute(
            List<Object> input, 
            Map<String, TokenPair> variables,
            boolean returnLast) 
            throws FledExecutionException 
    {
        if (variables == null)
        {
            variables = new HashMap<String, TokenPair>();
        }
        if (input == null)
        {
            input = new ArrayList<Object>();
        }
        RPNExecutionState state = new RPNExecutionState(variables, input, null);
        TokenPair last = null;
        for(int i = 0; i < statements.length; )
        {
            RPNStatement statement = statements[i];
            
            if (statement.isConstruct())
            {
                i = executeConstruct(statements, i, state, statement.getAsConstruct(), true);
            }
            else if (statement.isQueue())
            {
                last = execute(state, statement.getAsQueue());
                i++;
            }
            
            if (state.getResult() != null)
            {
                return state.getResult();
            }
        }
        if (returnLast)
        {
            return last;
        }
        return null;
    }
    
    public int executeConstruct(
            RPNStatement[] statements,
            Integer cursor,
            RPNExecutionState state, 
            RPNStatementConstruct statement,
            boolean firstIteration)
            throws FledExecutionException
    {
        TokenPair construct = statement.getConstructToken();
        if (construct.name.equals("{"))
        {
            state.pushVariableStack();
            return statement.getLocationIfTrue();
        }
        else if(construct.name.equals("}"))
        {
            state.popVariableStack();
            boolean iterative = (Boolean) construct.value;
            if (iterative)
            {
                return executeConstruct(
                        statements, 
                        cursor, 
                        state, 
                        statements[statement.getLocationIfTrue()].getAsConstruct(), 
                        false);
            }
            else
            {
                return statement.getLocationIfTrue();
            }
        }
        else
        {
            Function function = null;
            try 
            {
                function = functions.getFunction(construct.name);
            } 
            catch (UndefinedFunctionException ex) 
            {
                throw new FledExecutionException(
                        "unexpected error: couldn't find function", 
                        ex);
            }
            RPNConstruct rpnConstruct = (RPNConstruct) function;
            if (rpnConstruct.paramsInParathesis() == 0)
            {
                return statement.getLocationIfTrue();
            }
            if (rpnConstruct.resolvesTrue(statement.getParams(), state, this, firstIteration))
            {
                return statement.getLocationIfTrue();
            }
            return statement.getLocationIfFalse();
        }
    }
    
    public TokenPair execute(
            RPNExecutionState state, 
            RPNStatementQueue statement) 
            throws FledExecutionException
    {
        /**
         * variable stack
         */
        Stack<TokenPair> stack = new Stack<TokenPair>();
        
        for(TokenPair token : statement)
        {
            switch(token.type)
            {
                case STRING:
                case INTEGER:
                case DOUBLE:
                case VARIABLE:
                case NULL:
                {
                    stack.add(token);
                }
                break;
                    
                case OPERATOR:
                case FUNCTION:
                case HOOK:
                {
                    Function function = null;
                    try 
                    {
                        function = functions.getFunction(token.name);
                    } 
                    catch (UndefinedFunctionException ex) 
                    {
                        throw new FledExecutionException(
                                "unexpected error: couldn't find function: " + token, 
                                ex);
                    }

                    int paramCount = ((RPNFunction) function).numOfParams();
                    if (paramCount > stack.size())
                    {
                        throw new FledExecutionException(
                                "invalid amount of params");
                    }

                    TokenPair[] params = new TokenPair[paramCount];
                    for(int ii = params.length - 1; ii >= 0; ii--)
                    {
                        TokenPair stackToken = stack.pop();
                        params[ii] = stackToken;
                    }
                    TokenPair funcResult = ((RPNFunction)function)
                            .compute(params, state);
                    if (funcResult != null)
                    {
                        stack.add(funcResult);
                    }
                }
                break;
                    
                default:
                    throw new FledExecutionException("invalid token type: " + token.type);
            }
        }
        
        if (stack.isEmpty())
        {
            return null;
        }
        return stack.pop();
    }
    
    public String dumpCode()
    {
        StringBuilder string = new StringBuilder();
        int counter = 0;
        for(RPNStatement statement : statements)
        {
            string.append(counter++);
            string.append(": ");
            string.append(statement);
            string.append("\n");
        }
        return string.toString();
    }
}
