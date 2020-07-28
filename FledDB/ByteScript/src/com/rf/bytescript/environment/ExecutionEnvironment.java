/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.exception.ByteScriptInitException;
import com.rf.bytescript.exception.ByteScriptRuntimeException;
import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.util.ByteCodeReader;
import com.rf.bytescript.util.OrderedNamedValueArray;
import com.rf.bytescript.value.Value;
import com.rf.bytescript.value.ValueType;
import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class ExecutionEnvironment
{
    private class ScopeHeap
    {
        OrderedNamedValueArray<VariableValueWraper> variables;
        LinkedList<String> scope;
        
        public ScopeHeap() throws ByteScriptInitException
        {
            variables = new OrderedNamedValueArray<VariableValueWraper>(5);
            scope = new LinkedList<String>();
        }
    }
    
    private class VariableValueWraper
    {
        /**
         * the actual value
         */
        Value value;
        
        /**
         * the variable type... reference or local
         */
        ValueType variableType;
    }
    
    public static final int STACK_HIEGHT = 50;
    
    private int stackPos;
    
    private Value[] stack;
    
    private LinkedList<ScopeHeap> heap;
    
    public final OrderedNamedValueArray<Value> globals;
    
    private OrderedNamedValueArray<ByteCodeReader> code;
    
    private FunctionSet_System systemFunctions;
    
    /**
     * this is used for compiling when there is nothing to an environment yet
     */
    public ExecutionEnvironment () throws ByteScriptInitException
    {
        this.code = new OrderedNamedValueArray<ByteCodeReader>(5);
        this.systemFunctions = new FunctionSet_System();
        this.stack = new Value[STACK_HIEGHT];
        this.stackPos = 0;
        
        this.globals = new OrderedNamedValueArray<Value>(10);
        heap = new LinkedList<ScopeHeap>();
        this.pushScope(true);
    }

    /**
     * used when the code is already compiled
     */
    public ExecutionEnvironment (OrderedNamedValueArray<ByteCodeReader> code) 
            throws ByteScriptInitException
    {
        this.code = code;
        this.systemFunctions = new FunctionSet_System();
        this.stack = new Value[STACK_HIEGHT];
        this.stackPos = 0;
        
        this.globals = new OrderedNamedValueArray<Value>(10);
        heap = new LinkedList<ScopeHeap>();
        this.pushScope(true);
    }
    
    public void push(Value val)
    {
        stack[stackPos++] = val;
    }
    
    public Value pop()
    {
        return stack[--stackPos];
    }
    
    public Value peek()
    {
        if (stackPos == 0)
        {
            return null;
        }
        return stack[stackPos - 1];
    }
    
    public void cleanStack()
    {
        for(int i = stackPos; i < stack.length; i++)
        {
            if (stack[i] == null)
            {
                // we are done
                break;
            }
            stack[i] = null;
        }
    }
    
    public final void pushScope(boolean isFunctionCall) 
            throws ByteScriptInitException
    {
        if (isFunctionCall)
        {
            heap.add(new ScopeHeap());
        }
        else
        {
            heap.getLast().scope.add(null);
        }
    }
    
    public void popScope(boolean isFunctionReturn) 
            throws ByteScriptRuntimeException
    {
        if (isFunctionReturn)
        {
            if (heap.size() == 1)
            {
                throw new ByteScriptRuntimeException(
                        "cannot pop the only scope");
            }
            heap.removeLast();
        }
        else
        {
            ScopeHeap last = heap.getLast();
            
            while(true)
            {
                if (last.scope.isEmpty())
                {
                    throw new ByteScriptRuntimeException(
                            "cannot pop the only scope");
                }
                String name = last.scope.removeLast();
                if (name == null)
                {
                    break;
                }
                try
                {
                    last.variables.removeVariable(name);
                }
                catch (UndefinedVariableException ex)
                {
                    throw new ByteScriptRuntimeException(
                            "an unexpected error occurred", ex);
                }
            }
        }
    }

    /**
     * 
     * @param name
     * @return 
     */
    public Value getVariable(String name) 
            throws UndefinedVariableException
    {
        VariableValueWraper wraper = heap.getLast().variables.getVariable(name);
        if (wraper.variableType == ValueType.REFERENCE)
        {
            return wraper.value;
        }
        else
        {
            return new Value(wraper.value.type, wraper.value.value);
        }
    }
    
    public void setVariable(String name, Value value)
            throws ByteScriptRuntimeException, UndefinedVariableException
    {
        VariableValueWraper wraper = heap.getLast().variables.getVariable(name);
        if (wraper.value.type == ValueType.UNKNOWN)
        {
            /**
             * if the variable is defined as unknown, then it can
             * be any datatype
             */
            wraper.value = value;
        }
        else if (wraper.value.type == value.type)
        {
            if (wraper.value.type == ValueType.OBJECT)
            {
                throw new UnsupportedOperationException();
            }
            else
            {
                wraper.value = value;
            }
        }
        else
        {
            throw new ByteScriptRuntimeException(
                    name + " definition exception " + wraper.value + ": " 
                    + name + " cannot be assigned to " + value);
        }
    }
    
    public void declareVariable(String name, Value value) 
            throws ByteScriptRuntimeException
    {
        VariableValueWraper wraper = new VariableValueWraper();
        wraper.variableType = ValueType.VARIABLE;
        wraper.value = value;
        if (!heap.getLast().variables.setVariable(name, wraper))
        {
            throw new ByteScriptRuntimeException(name + " already defined");
        }
    }
    
    public void declareReference(String name, Value value)
            throws ByteScriptRuntimeException
    {
        VariableValueWraper wraper = new VariableValueWraper();
        wraper.variableType = ValueType.REFERENCE;
        wraper.value = value;
        if (!heap.getLast().variables.setVariable(name, wraper))
        {
            throw new ByteScriptRuntimeException(name + " already defined");
        }
    }

    /**
     * 
     * @param functionName
     * @return 
     */
    public ByteCodeReader getCompiledFunctionSafe (String functionName)
    {
        return code.getVariableSafe(functionName);
    }
    
    /**
     * 
     * @param functionName
     * @return 
     */
    public ISystemFunction getSystemFunctionSafe (String functionName)
    {
        return systemFunctions.getVariableSafe(functionName);
    }
}
