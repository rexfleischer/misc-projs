
package com.rf.fled.script.rpn;

import com.rf.fled.script.tokenizer.TokenPair;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class RPNExecutionState 
{
    private PrintStream stream;
    
    private PrintStream debug;
    
    private List<Object> input;
    
    private ArrayList<Map<String, TokenPair>> stack;
    
    private TokenPair result;
    
    public RPNExecutionState(
            Map<String, TokenPair> variables, 
            List<Object> input,
            PrintStream stream)
    {
        this.input  = input;
        this.stack  = new ArrayList<Map<String, TokenPair>>();
        this.stack.add(new HashMap<String, TokenPair>());// for globals
        this.stack.add(variables);
        
        if (stream == null)
        {
            this.stream = System.out;
        }
        else
        {
            this.stream = stream;
        }
    }
    
    public TokenPair getResult()
    {
        return result;
    }
    
    public void setResult(TokenPair result)
    {
        this.result = result;
    }
    
    public List<Object> getInput()
    {
        return input;
    }
    
    public PrintStream getOutputStream()
    {
        return stream;
    }
    
    public void pushVariableStack()
    {
        stack.add(new HashMap<String, TokenPair>());
    }
    
    public void popVariableStack()
    {
        if (stack.size() == 2)
        {
            throw new IllegalArgumentException("cannot remove stack below 1");
        }
        stack.remove(stack.size() - 1);
    }
    
    public void removeVariable(String key)
    {
        for(Map<String, TokenPair> layer : stack)
        {
            if (layer.containsKey(key))
            {
                layer.remove(key);
                return;
            }
        }
        throw new IllegalArgumentException("unknown variable");
    }
    
    public void setVariable(String key, TokenPair value)
    {
        for(Map<String, TokenPair> layer : stack)
        {
            if (layer.containsKey(key))
            {
                layer.put(key, value);
                return;
            }
        }
        stack.get(stack.size() - 1).put(key, value);
    }
    
    public TokenPair getVariable(String key)
    {
        for(Map<String, TokenPair> layer : stack)
        {
            if (layer.containsKey(key))
            {
                return layer.get(key);
            }
        }
        throw new IllegalArgumentException("undefined variable: " + key);
    }
    
    public TokenPair getVariableSafe(String key)
    {
        for(Map<String, TokenPair> layer : stack)
        {
            if (layer.containsKey(key))
            {
                return layer.get(key);
            }
        }
        return null;
    }
    
    public Map<String, TokenPair> getGlobals()
    {
        return stack.get(0);
    }
    
    public void setGlobal(String key, TokenPair value)
    {
        stack.get(0).put(key, value);
    }
    
    public TokenPair getGlobal(String key)
    {
        return stack.get(0).get(key);
    }
}
