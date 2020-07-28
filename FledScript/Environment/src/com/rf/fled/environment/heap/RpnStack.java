/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.heap;

/**
 *
 * @author REx
 */
public class RpnStack
{
    private static final int DEFAULT_SIZE = 64;
    
    private int stackPos;
    
    private HeapValue[] stack;
    
    public RpnStack()
    {
        this(DEFAULT_SIZE);
    }
    
    public RpnStack(int size)
    {
        stackPos = 0;
        stack = new HeapValue[size];
    }
    
    public void push(HeapValue val)
    {
        stack[stackPos++] = val;
    }
    
    public HeapValue pop()
    {
        HeapValue result = stack[--stackPos];
        stack[stackPos] = null;
        return result;
    }
    
    public HeapValue top()
    {
        if (stackPos == 0)
        {
            return null;
        }
        return stack[stackPos - 1];
    }
}
