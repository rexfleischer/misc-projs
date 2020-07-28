/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.generic;

import com.rf.fled.compiler.ByteScriptSyntaxException;
import com.rf.fled.compiler.tokenizer.InfixToRPNConverter;
import com.rf.fled.compiler.tokenizer.Token;
import com.rf.fled.compiler.tokenizer.TokenType;
import com.rf.fled.environment.ExecutionEnvironment;
import com.rf.fled.environment.bytecode.ByteCodeAggregator;
import com.rf.fled.environment.util.UndefinedValueException;
import com.rf.fled.environment.heap.HeapValue;
import com.rf.fled.environment.heap.HeapValueType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public class ByteCodeWriteManager
{
    public final InfixToRPNConverter converter;
    
    ByteCodeWriteManager (InfixToRPNConverter converter)
    {
        this.converter = converter;
    }
    
    public void compile (
            ByteCodeAggregator aggregator, 
            ExecutionEnvironment environment,
            List<Token> infix,
            Integer position)
            throws ByteScriptSyntaxException
    {
        LinkedList<Token> statement = new LinkedList<Token>();

        for( ; position < infix.size(); position++)
        {
            Token token = infix.get(position);

            if (token.type == TokenType.SEPERATOR)
            {
                Token assignment = null;
                for(Token pair : statement)
                {
                    if (pair.type == TokenType.ASSIGNMENT)
                    {
                        assignment = pair;
                        break;
                    }
                }

                if (assignment!= null)
                {
                    writeAssignment(aggregator, environment, statement, assignment);
                }
                else
                {
                    writeStatement(aggregator, environment, statement);
                }
                statement = new LinkedList<Token>();
            }
            else if (token.type == TokenType.DECLARATION)
            {
                if (!statement.isEmpty())
                {
                    throw new ByteScriptSyntaxException(
                            "illegal declaration placement");
                }

                position += writeDeclaration(aggregator, environment, infix, position) - 1;
            }
            else
            {
                statement.add(token);
            }
        }

        if (!statement.isEmpty())
        {
            throw new ByteScriptSyntaxException(
                    "illegal end of a statement");
        }
    }
    
    public void writeAssignment (
            ByteCodeAggregator aggregator, 
            ExecutionEnvironment environment,
            LinkedList<Token> statement, 
            Token assignment)
    {
        
    }
    
    public void writeStatement (
            ByteCodeAggregator aggregator, 
            ExecutionEnvironment environment,
            LinkedList<Token> statement) 
            throws  ByteScriptSyntaxException, 
                    UndefinedValueException
    {
        
        List<Token> convert = converter.convert(statement);
        /**
         * basically, with each statement, it tries to go through as if
         * the program is running so it can try to do type hinting.
         */
        ICompilingFunction lastFunction = null;
        for(int i = 0; i < convert.size(); i++)
        {
            Token token = convert.get(i);
            
            switch(token.type)
            {
                case BOOLEAN:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeBoolean((Boolean) token.value);
                    enviornment.push(new HeapValue(HeapValue.ValueType.BOOLEAN, null));
                }
                break;
                    
                case INTEGER:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeInteger((Integer) token.value);
                    enviornment.push(new HeapValue(HeapValueType.INTEGER, null));
                }
                break;
                    
                case DOUBLE:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeDouble((Double) token.value);
                    enviornment.push(new HeapValue(HeapValue.ValueType.DOUBLE, null));
                }
                break;
                    
                case STRING:
                {
                    writeOperation(ByteCodeOperation.PUSH_H);
                    writeString((String) token.value);
                    enviornment.push(new HeapValue(HeapValue.ValueType.STRING, null));
                }
                break;
                    
                case VARIABLE:
                {
                    writePushVariable(token.name);
                    enviornment.push(enviornment.getVariable(token.name));
                }
                break;
                    
                case FUNCTION:
                {
                    if (token.name.contains("."))
                    {
                        throw new UnsupportedOperationException();
                        // object function call
                    }
                    else
                    {
                        Function function  = enviornment
                                .langFunctions
                                .getFunctionSafe(token.name);
                        if (function != null)
                        {
                            ICompilingOperation cfunc = 
                                    (ICompilingOperation) function;
                            if (cfunc.getType() != TokenType.FUNCTION)
                            {
                                throw new UndefinedFunctionException(
                                        token.name + " is not a defined function");
                            }
                            if (cfunc.isAlwaysLast())
                            {
                                lastFunction = cfunc;
                            }
                            else
                            {
                                cfunc.compile(enviornment, this);
                            }
                        }
                        else
                        {
                            function = enviornment.getSystemFunctionSafe(token.name);
                            if (function != null)
                            {
                                writeOperation(ByteCodeOperation.CALL_S);
                                writeString(token.name);
                            }
                            else
                            {
                                writeOperation(ByteCodeOperation.CALL_C);
                                writeString(token.name);
                            }
                        }
                    }
                }
                break;
                    
                case OPERATOR:
                {
                    Function function = enviornment
                                .langFunctions
                                .getFunctionSafe(token.name);
                    if (!(function instanceof ICompilingOperation))
                    {
                        throw new FledSyntaxException("");
                    }
                    ICompilingOperation operation = (ICompilingOperation) function;
                    if (operation.isAlwaysLast())
                    {
                        lastFunction = operation;
                    }
                    else
                    {
                        operation.compile(enviornment, this);
                    }
                }
                break;
                    
                default:
                    throw new FledSyntaxException(
                            "unable to parse: invalid type for bytecode writing");
            }
        }
        if (lastFunction != null)
        {
            lastFunction.compile(enviornment, this);
        }
    }

    public int writeDeclaration (
            ByteCodeAggregator aggregator, 
            ExecutionEnvironment environment, 
            List<Token> infix, 
            Integer position)
    {
        return 0;
    }
}
