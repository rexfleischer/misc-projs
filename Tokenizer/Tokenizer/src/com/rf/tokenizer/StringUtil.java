/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

/**
 *
 * @author REx
 */
class StringUtil
{
    static String getInner(int start, String parsing, char begin, char end)
    {
        int realstart = -1;
        int level = 0;
        int size = parsing.length();
        
        for(int i = start; i < size; i++)
        {
            char character = parsing.charAt(i);
            
            if (begin == character)
            {
                if (level == 0)
                {
                    realstart = i;
                }
                level++;
            }
            else if (end == character)
            {
                level--;
                if (level == 0)
                {
                    return parsing.substring(realstart + 1, i - 1);
                }
            }
        }
        
        return null;
    }
    
    static boolean canPeek(String parsing, int start)
    {
        return (start + 1) < parsing.length();
    }
    
    static char peek(String parsing, int start)
    {
        return parsing.charAt(start + 1);
    }
}
