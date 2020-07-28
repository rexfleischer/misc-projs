/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledScriptInitException;
import com.rf.fled.script.UndefinedVariableException;
import com.rf.fled.script.rpn2.util.ByteCodeReader;
import com.rf.fled.script.rpn2.util.OrderedVariableArray;
import com.rf.fled.script.rpn2.util.Value;
import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class ExecutionEnvironment
{
    private class ScopeHeap
    {
        OrderedVariableArray<VariableValueWraper> variables;
        LinkedList<String> scope;
        
        public ScopeHeap() throws FledScriptInitException
        {
            variables = new OrderedVariableArray<VariableValueWraper>(5);
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
        Value.ValueType variableType;
        
        /**
         * the type that this variable is... bool, unknown, object... ect.
         */
        Value.ValueType dataType;
    }
    
    public static final int STACK_HIEGHT = 50;
    
    private int stackPos;
    
    private Value[] stack;
    
    private LinkedList<ScopeHeap> heap;
    
    public final OrderedVariableArray<Value> globals;
    
    private FunctionSet_Compiled code;
    
    private FunctionSet_System systemFunctions;
    
    /**
     * this is used for compiling when there is nothing to an environment yet
     */
    public ExecutionEnvironment () throws FledScriptInitException
    {
        this.code = new FunctionSet_Compiled();
        this.systemFunctions = new FunctionSet_System();
        this.stack = new Value[STACK_HIEGHT];
        this.stackPos = 0;
        
        this.globals = new OrderedVariableArray<Value>(10);
        heap = new LinkedList<ScopeHeap>();
        this.pushScope(true);
    }

    /**
     * used when the code is already compiled
     */
    public ExecutionEnvironment (FunctionSet_Compiled code) 
            throws FledScriptInitException
    {
        this.code = code;
        this.systemFunctions = new FunctionSet_System();
        this.stack = new Value[STACK_HIEGHT];
        this.stackPos = 0;
        
        this.globals = new OrderedVariableArray<Value>(10);
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
            throws FledScriptInitException
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
    
    public void popScope(boolean isFunctionReturn) throws FledExecutionException
    {
        if (isFunctionReturn)
        {
            if (heap.size() == 1)
            {
                throw new FledExecutionException(
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
                    throw new FledExecutionException(
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
                    throw new FledExecutionException(
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
        if (wraper.variableType == Value.ValueType.REFERENCE)
        {
            return wraper.value;
        }
        else
        {
            return new Value(wraper.value.type, wraper.value.value);
        }
    }
    
    public void setVariable(String name, Value value)
            throws FledExecutionException, UndefinedVariableException
    {
        VariableValueWraper wraper = heap.getLast().variables.getVariable(name);
        if (wraper.dataType == Value.ValueType.UNKNOWN)
        {
            /**
             * if the variable is defined as unknown, then it can
             * be any datatype
             */
            wraper.value = value;
        }
        else if (wraper.dataType == value.type)
        {
            if (wraper.dataType == Value.ValueType.OBJECT)
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
            throw new FledExecutionException(
                    name + " definition exception " + wraper.value + ": " 
                    + name + " cannot be assigned to " + value);
        }
    }
    
    public void declareVariable(String name, Value.ValueType type, Value value) 
            throws FledExecutionException
    {
        VariableValueWraper wraper = new VariableValueWraper();
        wraper.dataType = type;
        wraper.variableType = Value.ValueType.VARIABLE;
        wraper.value = value;
        if (!heap.getLast().variables.setVariable(name, wraper))
        {
            throw new FledExecutionException(name + " already defined");
        }
    }
    
    public void declareReference(String name, Value.ValueType type, Value value)
            throws FledExecutionException
    {
        VariableValueWraper wraper = new VariableValueWraper();
        wraper.dataType = type;
        wraper.variableType = Value.ValueType.REFERENCE;
        wraper.value = value;
        if (!heap.getLast().variables.setVariable(name, wraper))
        {
            throw new FledExecutionException(name + " already defined");
        }
    }

    /**
     * 
     * @param functionName
     * @return 
     */
    public ByteCodeReader getCompiledFunctionSafe (String functionName)
    {
        return code.getFunction(functionName);
    }
    
    /**
     * 
     * @param functionName
     * @return 
     */
    public ISystemFunction getSystemFunctionSafe (String functionName)
    {
        return systemFunctions.getFunctionSafe(functionName);
    }
}
