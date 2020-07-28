/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.util;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.tokenizer.TokenPair;
import java.util.List;

/**
 *
 * @author REx
 */
public class CompileUtil
{
    public static int getMatchingParas(
            int start, 
            List<TokenPair> all, 
            List<TokenPair> result, 
            String open, 
            String close) 
            throws FledSyntaxException
    {
        int position = start;
        int paraStack = 0;
        boolean foundEnd = false;
        for(int n = all.size(); position < n; position++)
        {
            TokenPair pair = all.get(position);
            
            if (pair.name.equals(open))
            {
                paraStack++;
                if (paraStack > 1)
                {
                    result.add(pair);
                }
            }
            else if (pair.name.equals(close))
            {
                paraStack--;
                if (paraStack == 0)
                {
                    position++;
                    foundEnd = true;
                    break;
                }
                else
                {
                    result.add(pair);
                }
            }
            else if (paraStack == 0)
            {
                throw new FledSyntaxException(
                        "compiler error: illegal construct call");
            }
            else
            {
                result.add(pair);
            }
        }
        
        if (!foundEnd)
        {
            throw new FledSyntaxException(
                    "illegal end of a construct");
        }
        
        return position - start;
    }
    
    public static int getUntilMatch(
            int start, 
            List<TokenPair> all, 
            List<TokenPair> result, 
            String match) 
            throws FledSyntaxException
    {
        int position = start;
        boolean foundEnd = false;
        for(int n = all.size(); position < n; position++)
        {
            TokenPair pair = all.get(position);
            
            if (pair.name.equals(match))
            {
                foundEnd = true;
                break;
            }
            result.add(pair);
        }
        
        if (!foundEnd)
        {
            throw new FledSyntaxException(
                    "illegal end of a construct");
        }
        
        return position - start;
    }
}
