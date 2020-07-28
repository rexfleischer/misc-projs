/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.language;

import com.rf.fled.script.UndefinedFunctionException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public abstract class FunctionSet 
{
    public class FunctionPair implements Comparable<FunctionPair>
    {
        private String alias;
        private Function function;
        public FunctionPair(String alias, Function function)
        {
            this.alias      = alias;
            this.function   = function;
        }

        @Override
        public int compareTo(FunctionPair o) 
        {
            return alias.compareTo(o.alias);
        }
    }
    
    protected abstract void getFunctionList(List<FunctionPair> functionBuffer);
    
    private FunctionPair[] functions;
    
    public FunctionSet()
    {
        List<FunctionPair> functionBuffer = new LinkedList<FunctionPair>();
        getFunctionList(functionBuffer);
        
        functions = new FunctionPair[functionBuffer.size()];
        functionBuffer.toArray(functions);
        Arrays.sort(functions);
    }
    
    public Function getFunctionSafe(String alias)
    {
        int index = binarySearchFromAlias(alias);
        if (0 <= index && index < functions.length)
        {
            return functions[index].function;
        }
        else
        {
            return null;
        }
    }
    
    public Function getFunction(String alias) 
            throws UndefinedFunctionException
    {
        try
        {
            return functions[binarySearchFromAlias(alias)].function;
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            throw new UndefinedFunctionException(
                    alias + " is not part of the function definition");
        }
    }
    
    private int binarySearchFromAlias(String key) 
    {
        int low = 0;
        int high = functions.length - 1;

        while (low <= high) 
        {
            int mid = (low + high) >>> 1;
            FunctionPair midVal = functions[mid];
            int cmp = midVal.alias.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }
}
