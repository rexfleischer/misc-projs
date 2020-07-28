package com.rf.fled.tokenizer.util;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.tokenizer.rpn;
//
//import com.rf.fled.compiler.ByteScriptSyntaxException;
//import com.rf.fled.environment.util.OrderedNamedValueArray;
//import com.rf.fled.environment.util.UndefinedValueException;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Stack;
//
///**
// *
// * @author REx
// */
//public class InfixToRPNConverter 
//{
//    private OrderedNamedValueArray<TypePrecedencePair> functions;
//    
//    public InfixToRPNConverter(OrderedNamedValueArray<TypePrecedencePair> functions)
//    {
//        this.functions = functions;
//    }
//    
//    public List<Token> convert(List<Token> infix) 
//            throws  ByteScriptSyntaxException,
//                    UndefinedValueException
//    {
//        Stack<Token> stack = new Stack<Token>();
//        LinkedList<Token> result = new LinkedList<Token>();
//        
//        for(int i = 0; i < infix.size(); i++)
//        {
//            Token curr = infix.get(i);
//
//            switch(curr.type)
//            {
//                case NULL:
//                case BOOLEAN:
//                case INTEGER:
//                case DOUBLE:
//                case STRING:
//                case VARIABLE:
//                    // these are all variables, just push to the output
//                    result.add(curr);
//                    break;
//
//                case PARATHESIS:
//                    if (curr.getValueAsString().equals("("))
//                    {
//                        stack.push(curr);
//                    }
//                    else
//                    {
//                        while(!stack.isEmpty() &&
//                              !stack.peek().getValueAsString().equals("("))
//                        {
//                            result.add(stack.pop());
//                        }
//                        if (stack.isEmpty())
//                        {
//                            throw new ByteScriptSyntaxException(
//                                    "parathesis mismatch");
//                        }
//                        stack.pop();
//                        if (!stack.isEmpty() && 
//                             stack.peek().type == TokenType.FUNCTION)
//                        {
//                            result.add(stack.pop());
//                        }
//                    }
//                    break;
//
//                case COMMA:
//                    while(!stack.isEmpty() &&
//                          !stack.peek().getValueAsString().equals("("))
//                    {
//                        result.add(stack.pop());
//                    }
//                    if (stack.isEmpty())
//                    {
//                        throw new ByteScriptSyntaxException(
//                                "parathesis mismatch");
//                    }
//                    break;
//
//                case OPERATOR:
//                case FUNCTION:
//                    TypePrecedencePair currFunction = functions
//                            .getValue(curr.getValueAsString());
//                    while(!stack.isEmpty())
//                    {
//                        Token compared = stack.peek();
//                        TypePrecedencePair comparedFunc = functions
//                                .getValue(compared.getValueAsString());
//                        if (currFunction.precedence <= comparedFunc.precedence)
//                        {
//                            result.add(stack.pop());
//                        }
//                        else
//                        {
//                            break;
//                        }
//                    }
//
//                    stack.push(curr);
//                    break;
//
//                default:
//                    throw new IllegalArgumentException("invalid ValueType: " + curr);
//            }
//        }
//
//        while(!stack.isEmpty())
//        {
//            Token curr = stack.pop();
//            if (curr.type == TokenType.PARATHESIS)
//            {
//                throw new ByteScriptSyntaxException("parathesis mismatch");
//            }
//            result.add(curr);
//        }
//        
//        
//        return result;
//    }
//}
