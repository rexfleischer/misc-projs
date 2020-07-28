/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.util;

import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.value.Value;
import java.util.List;

/**
 *
 * @author REx
 */
public class CompileUtil
{
    public static int getMatchingParas(
            int start, 
            List<Value> all, 
            List<Value> result, 
            String open, 
            String close) 
            throws ByteScriptSyntaxException
    {
        int position = start;
        int paraStack = 0;
        boolean foundEnd = false;
        for(int n = all.size(); position < n && !foundEnd; position++)
        {
            Value pair = all.get(position);
            
            if (pair.value instanceof String)
            {
                String name = pair.getValueAsString();
                if (name.equals(open))
                {
                    paraStack++;
                }
                else if (name.equals(close))
                {
                    paraStack--;
                    if (paraStack == 0)
                    {
                        position++;
                        foundEnd = true;
                    }
                }
                else if (paraStack == 0)
                {
                    throw new ByteScriptSyntaxException(
                            "compiler error: illegal construct call");
                }
            }
            
            result.add(pair);
        }
        
        if (!foundEnd)
        {
            throw new ByteScriptSyntaxException(
                    "illegal end of a construct");
        }
        
        return position - start;
    }
    
    public static int getUntilMatch(
            int start, 
            List<Value> all, 
            List<Value> result, 
            String match) 
            throws ByteScriptSyntaxException
    {
        int position = start;
        boolean foundEnd = false;
        for(int n = all.size(); position < n && !foundEnd; position++)
        {
            Value pair = all.get(position);
            
            if (pair.value instanceof String)
            {
                String name = pair.getValueAsString();
                if (name.equals(match))
                {
                    foundEnd = true;
                }
            }
            result.add(pair);
        }
        
        if (!foundEnd)
        {
            throw new ByteScriptSyntaxException(
                    "illegal end of a construct");
        }
        
        return position - start;
    }
}
