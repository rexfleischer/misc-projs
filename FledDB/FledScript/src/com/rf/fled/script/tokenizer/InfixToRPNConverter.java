/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.tokenizer;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.UndefinedFunctionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author REx
 */
public class InfixToRPNConverter 
{
    private FunctionSet functions;
    
    public InfixToRPNConverter(FunctionSet functions)
    {
        this.functions = functions;
    }
    
    public List<TokenPair> convert(List<TokenPair> infix) 
            throws FledSyntaxException, UndefinedFunctionException
    {
        Stack<TokenPair> stack = new Stack<TokenPair>();
        ArrayList<TokenPair> result = new ArrayList<TokenPair>();
        
        for(int i = 0; i < infix.size(); i++)
        {
            TokenPair curr = infix.get(i);

            switch(curr.type)
            {
                case NULL:
                case BOOLEAN:
                case INTEGER:
                case DOUBLE:
                case STRING:
                case VARIABLE:
                case HOOK:
                    // these are all variables, just push to the output
                    result.add(curr);
                    break;

                case PARATHESIS:
                    if (curr.name.equals("("))
                    {
                        stack.push(curr);
                    }
                    else
                    {
                        while(!stack.isEmpty() &&
                              !stack.peek().name.equals("("))
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
                          !stack.peek().name.equals("("))
                    {
                        result.add(stack.pop());
                    }
                    if (stack.isEmpty())
                    {
                        throw new FledSyntaxException("parathesis mismatch");
                    }
                    break;

                case OPERATOR:
                case FUNCTION:
                    Function currFunction = null;
                    while(!stack.isEmpty() &&
                            curr.type.globalPrecedence <= 
                            stack.peek().type.globalPrecedence)
                    {
                        TokenPair compared = stack.peek();
                        if (curr.type.globalPrecedence == 
                                compared.type.globalPrecedence)
                        {
                            /**
                             * if it gets into here, then that means the
                             * two tokens are the same type... 
                             */
                            if (currFunction == null)
                            {
                                currFunction = functions.getFunction(curr.name);
                            }
                            Function comparedMeta = 
                                    functions.getFunction(compared.name);

                            if (currFunction.getPresedence() <= 
                                    comparedMeta.getPresedence())
                            {
                                result.add(stack.pop());
                            }
                            else
                            {
                                break;
                            }
                        }
                        else
                        {
                            result.add(stack.pop());
                        }
                    }

                    stack.push(curr);
                    break;

                default:
                    throw new IllegalArgumentException("invalid TokenType: " + curr);
            }
        }

        while(!stack.isEmpty())
        {
            TokenPair curr = stack.pop();
            if (curr.type == TokenType.PARATHESIS)
            {
                throw new FledSyntaxException("parathesis mismatch");
            }
            result.add(curr);
        }
        
        
        return result;
    }
}
