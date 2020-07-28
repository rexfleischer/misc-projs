/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.sql;

import com.rf.fled.lang.FledExecutionException;
import com.rf.fled.lang.FledSyntaxException;
import com.rf.fled.lang.IFunction;
import com.rf.fled.lang.tokenizer.InfixToRPNConverter;
import com.rf.fled.lang.tokenizer.Token;
import com.rf.fled.lang.tokenizer.Tokenizer;
import com.rf.fled.lang.util.OrderedNamedValueArray;
import com.rf.fled.lang.util.UndefinedVariableException;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author REx
 */
public class SqlEvaluator
{
    private OrderedNamedValueArray<IFunction> functions;
    
    private Token[] rpnTokens;
    
    public SqlEvaluator(String expression)
            throws  FledSyntaxException, 
                    UndefinedVariableException
    {
        functions = SqlFunctionSet.get();
        List<Token> infix = (new Tokenizer(functions)).tokenize(expression);
        List<Token> rpn = (new InfixToRPNConverter(functions)).convert(infix);
        
        
    }
    
    public SqlEvaluator(List<Token> infix)
            throws  FledSyntaxException, 
                    UndefinedVariableException
    {
        List<Token> rpn = (new InfixToRPNConverter(functions)).convert(infix);
    }
    
    public Object execute(Object[] input)
            throws FledExecutionException
    {
        /**
         * variable stack
         */
        Stack<SqlValue> stack = new Stack<SqlValue>();
        
        for(Token token : rpnTokens)
        {
            switch(token.type)
            {
                case BOOLEAN:
                {
                    stack.add(new SqlValue(SqlValueType.BOOLEAN, 
                            token.getValueAsBoolean()));
                }
                break;
                case INTEGER:
                {
                    stack.add(new SqlValue(SqlValueType.INTEGER, 
                            token.getValueAsInteger()));
                }
                break;
                case DOUBLE:
                {
                    stack.add(new SqlValue(SqlValueType.DOUBLE,
                            token.getValueAsDouble()));
                }
                break;
                case STRING:
                {
                    stack.add(new SqlValue(SqlValueType.STRING, 
                            token.getValueAsString()));
                }
                break;
                case NULL:
                {
                    stack.add(new SqlValue(SqlValueType.NULL, null));
                }
                break;
                    
                case OPERATOR:
                case FUNCTION:
                {
                    IFunction function = null;
                    try 
                    {
                        function = functions.getVariable(token.name);
                    } 
                    catch (UndefinedVariableException ex) 
                    {
                        throw new FledExecutionException(
                                "unexpected error: couldn't find function: " + token, 
                                ex);
                    }

                    int paramCount = function.numOfParams();
                    if (paramCount > stack.size())
                    {
                        throw new FledExecutionException(
                                "invalid amount of params");
                    }

                    SqlValue[] params = new SqlValue[paramCount];
                    for(int ii = params.length - 1; ii >= 0; ii--)
                    {
                        params[ii] = stack.pop();
                    }
                    SqlValue funcResult = ((ISqlFunction)function).compute(params);
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
}
