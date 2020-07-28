/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.tokenizer;

import com.rf.bytescript.environment.IFunction;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.exception.UndefinedFunctionException;
import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.util.OrderedNamedValueArray;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
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
    
    public List<Value> convert(List<Value> infix) 
            throws  ByteScriptSyntaxException, 
                    UndefinedFunctionException, 
                    UndefinedVariableException
    {
        Stack<Value> stack = new Stack<Value>();
        ArrayList<Value> result = new ArrayList<Value>();
        
        for(int i = 0; i < infix.size(); i++)
        {
            Value curr = infix.get(i);

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
                            throw new ByteScriptSyntaxException(
                                    "parathesis mismatch");
                        }
                        stack.pop();
                        if (!stack.isEmpty() && 
                             stack.peek().type == ValueType.FUNCTION)
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
                        throw new ByteScriptSyntaxException(
                                "parathesis mismatch");
                    }
                    break;

                case OPERATOR:
                case FUNCTION:
                    IFunction currFunction = functions
                            .getVariable(curr.getValueAsString());
                    while(!stack.isEmpty())
                    {
                        Value compared = stack.peek();
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
            Value curr = stack.pop();
            if (curr.type == ValueType.PARATHESIS)
            {
                throw new ByteScriptSyntaxException("parathesis mismatch");
            }
            result.add(curr);
        }
        
        
        return result;
    }
}
