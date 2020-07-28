/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.tokenizer;

import com.rf.fled.lang.FledSyntaxException;
import com.rf.fled.lang.IFunction;
import com.rf.fled.lang.util.OrderedNamedValueArray;
import com.rf.fled.lang.util.UndefinedVariableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author REx
 */
public class InfixToRPNConverter 
{
    private OrderedNamedValueArray<IFunction> functions;
    
    public InfixToRPNConverter(OrderedNamedValueArray<IFunction> functions)
    {
        this.functions = functions;
    }
    
    public List<Token> convert(List<Token> infix) 
            throws  FledSyntaxException, 
                    UndefinedVariableException
    {
        Stack<Token> stack = new Stack<Token>();
        ArrayList<Token> result = new ArrayList<Token>();
        
        for(int i = 0; i < infix.size(); i++)
        {
            Token curr = infix.get(i);

            switch(curr.type)
            {
                case NULL:
                case BOOLEAN:
                case INTEGER:
                case DOUBLE:
                case STRING:
                case VARIABLE:
                    // these are all variables, just push to the output
                    result.add(curr);
                    break;

                case PARATHESIS:
                    if (curr.getValueAsString().equals("("))
                    {
                        stack.push(curr);
                    }
                    else
                    {
                        while(!stack.isEmpty() &&
                              !stack.peek().getValueAsString().equals("("))
                        {
                            result.add(stack.pop());
                        }
                        if (stack.isEmpty())
                        {
                            throw new FledSyntaxException("parathesis mismatch");
                        }
                        stack.pop();
                        if (!stack.isEmpty() && 
                             stack.peek().type == TokenType.FUNCTION)
                        {
                            result.add(stack.pop());
                        }
                    }
                    break;

                case COMMA:
                    while(!stack.isEmpty() &&
                          !stack.peek().getValueAsString().equals("("))
                    {
                        result.add(stack.pop());
                    }
                    if (stack.isEmpty())
                    {
                        throw new FledSyntaxException(
                                "parathesis mismatch");
                    }
                    break;

                case OPERATOR:
                case FUNCTION:
                    IFunction currFunction = functions
                            .getVariable(curr.getValueAsString());
                    while(!stack.isEmpty())
                    {
                        Token compared = stack.peek();
                        IFunction comparedFunc = functions
                                .getVariable(compared.getValueAsString());
                        if (currFunction.getPresedence() <= 
                                comparedFunc.getPresedence())
                        {
                            result.add(stack.pop());
                        }
                        else
                        {
                            break;
                        }
                    }

                    stack.push(curr);
                    break;

                default:
                    throw new IllegalArgumentException("invalid ValueType: " + curr);
            }
        }

        while(!stack.isEmpty())
        {
            Token curr = stack.pop();
            if (curr.type == TokenType.PARATHESIS)
            {
                throw new FledSyntaxException("parathesis mismatch");
            }
            result.add(curr);
        }
        
        
        return result;
    }
}
