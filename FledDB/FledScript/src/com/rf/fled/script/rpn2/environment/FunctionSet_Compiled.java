/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.rpn2.util.ByteCodeReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class FunctionSet_Compiled
{
    private Map<String, ByteCodeReader> functions;
    
    private ByteCodeReader main;
    
    public FunctionSet_Compiled()
    {
        this.main = null;
        this.functions = new HashMap<String, ByteCodeReader>();
    }
    
    public ByteCodeReader getMain()
    {
        return main;
    }
    
    public void setMain(ByteCodeReader main)
    {
        this.main = main;
    }
    
    public ByteCodeReader getFunction(String function)
    {
        return functions.get(function);
    }
    
    public void removeFunction(String function)
    {
        functions.remove(function);
    }
    
    public void setFunction(String function, ByteCodeReader code)
    {
        functions.put(function, code);
    }
}
