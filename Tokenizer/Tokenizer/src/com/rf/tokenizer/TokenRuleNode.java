/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author REx
 */
abstract class TokenRuleNode
{
    static class TokenizingContext
    {
        int start;

        String parsing;

        LinkedList<Token> tokens;

        TokenRuleNode rootrule;

        TokenizingContext(String parsing, TokenRuleNode rootrule)
        {
            this.parsing = parsing;
            this.tokens = new LinkedList<>();
            this.rootrule = rootrule;
        }
    }
    
    static class Match
    {
        int size;
        
        String name;
    }
    
    String name;
    
    char[] attributes;
    
    String rule;
    
    TokenRuleNode child;

    TokenRuleNode()
    {
        this.name       = null;
        this.rule       = null;
        this.attributes = null;
        this.child      = null;
    }
    
    boolean hasAttribute(char key)
    {
        if (attributes == null)
        {
            return false;
        }
        for (int i = 0; i < attributes.length; i++)
        {
            if (attributes[i] == key)
            {
                return true;
            }
        }
        return false;
    }
    
    boolean keep()
    {
        return ! hasAttribute(RuleCharacter.ATTRIBUTE_KEEP_N);
    }
    
    abstract Match matches(TokenizingContext context, int index)
            throws TokenizerParseException;
    
    static class TokenRuleNode_Iterate extends TokenRuleNode
    {
        ArrayList<TokenRuleNode> children;

        TokenRuleNode_Iterate()
        {
            children = new ArrayList<>();
        }
        
        @Override
        Match matches(TokenizingContext context, int index) throws TokenizerParseException
        {
            Match result = null;
            for(int i = 0; i < children.size(); i++)
            {
                Match match = children.get(i).matches(context, index);
                if (match != null)
                {
                    if (result == null)
                    {
                        result = match;
                    }
                    else if (match.size > result.size)
                    {
                        result = match;
                    }
                }
            }
            return result;
        }
    }
    
    static class TokenRuleNode_LoopGreedy extends TokenRuleNode
    {
        TokenRuleNode wraps;
        
        int min;
        
        int max;

        TokenRuleNode_LoopGreedy(int min, int max)
        {
            this.min = min;
            this.max = max;
        }
        
        @Override
        boolean matches(TokenizingContext context, int index) 
                throws TokenizerParseException
        {
            int matches = 0;
            
            for( ; matches < max; matches++)
            {
                if (!wraps.matches(context, index))
                {
                    break;
                }
                
                if (child != null && !child.matches(context, index))
                {
                    break;
                }
            }
            
            return min <= matches;
        }
    }
    
    static class TokenRuleNode_LoopNotGreedy extends TokenRuleNode
    {
        TokenRuleNode wraps;
        
        int min;
        
        int max;

        TokenRuleNode_LoopNotGreedy(int min, int max)
        {
            this.min = min;
            this.max = max;
        }
        
        @Override
        boolean matches(TokenizingContext context) throws TokenizerParseException
        {
            int matches = 0; 
            
            for( ; matches < max; matches++)
            {
                if (!wraps.matches(context))
                {
                    break;
                }
                
                if ()
            }
            
            return min <= matches;
        }
    }
    
    static class TokenRuleNode_Context extends TokenRuleNode
    {
        TokenRuleNode wraps;

        public TokenRuleNode_Context()
        {
        }
        
        
        
        @Override
        boolean matches(TokenizingContext context) throws TokenizerParseException
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    // character evals
    
    static class TokenRuleNode_Any extends TokenRuleNode
    {
        @Override
        boolean matches(TokenizingContext context) throws TokenizerParseException
        {
            return true;
        }
    }
    
    static class TokenRuleNode_Single extends TokenRuleNode
    {
        
    }
    
    void recursiveToString(StringBuilder string,
                           TokenRuleNode gotmehere, 
                           int depth)
    {
        string.append('\n');
        for(int i = 0; i < depth; i++)
        {
            string.append('.');
        }
        string.append('[');
        string.append(rule);
        string.append(']');
        
//        Iterator<TokenRuleNode> it = this.children.iterator();
//        while(it.hasNext())
//        {
//            TokenRuleNode node = it.next();
//            node.recursiveToString(string, this, depth + 1);
//        }
    }
}
