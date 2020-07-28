/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

import com.rf.fled.tokenizer.*;
import com.rf.fled.tokenizer.rule.TokenRule;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class TokenRuleNode
{
    protected Map<TokenRule, TokenRuleNode> children;
    
    protected TokenType tokentype;
    
    public TokenRuleNode()
    {
        children = new HashMap<>();
    }
    
    public TokenType getTokenType()
    {
        return tokentype;
    }
    
    public void setChild(TokenType tokentype)
            throws TokenRuleOverlapException
    {
        setChild(tokentype, 0);
    }
    
    private void setChild(TokenType tokentype, int ruleon)
            throws TokenRuleOverlapException
    {
        if (ruleon >= tokentype.rules.size())
        {
            if (this.tokentype != null)
            {
                throw new TokenRuleOverlapException(tokentype.toString());
            }
            this.tokentype = tokentype;
        }
        else
        {
            TokenRule rule = tokentype.rules.get(ruleon);
            TokenRuleNode nextrule = children.get(rule);
            if (nextrule == null)
            {
                nextrule = new TokenRuleNode();
                children.put(rule, nextrule);
            }
            nextrule.setChild(tokentype, ruleon + 1);
        }
    }
    
    public Token getToken(String expression, int pos)
            throws IllegalTokenException,
                   TokenRuleOverlapException
    {
        return getToken(expression, pos, pos);
    }
    
    private Token getToken(String expression, int start, int posat)
            throws IllegalTokenException,
                   TokenRuleOverlapException
    {
        Token result = null;
        if (tokentype != null)
        {
            String captured = expression.substring(start, posat);
            
            Object checker = tokentype.doWorkOnToken(captured);
            if (checker != null)
            {
                /**
                 * if the returned value is null then we assume
                 * that it isnt the token
                 */
                result = new Token(tokentype,
                                   checker, 
                                   captured, 
                                   start);
            }
        }
        
        if (posat >= expression.length())
        {
            return result;
        }
        
        Iterator<TokenRule> it = children.keySet().iterator();
        while(it.hasNext())
        {
            TokenRule rule = it.next();
            int match = rule.matches(expression, posat); 
            if (match > 0)
            {
                TokenRuleNode child = children.get(rule);
                Token checker = child.getToken(expression, start, posat + match);
                if (checker != null)
                {
                    if (result == null)
                    {
                        result = checker;
                    }
                    else if (result.matched.length() == checker.matched.length())
                    {
                        throw new TokenRuleOverlapException(
                                String.format("expression overlap [%s] and [%s]", 
                                              result.matched, checker.matched));
                    }
                    else if (result.matched.length() < checker.matched.length())
                    {
                        result = checker;
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.children);
        hash = 31 * hash + Objects.hashCode(this.tokentype);
        return hash;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (getClass() != o.getClass())
        {
            return false;
        }
        return recursiveEquals((TokenRuleNode) o);
    }
    
    private boolean recursiveEquals(TokenRuleNode node)
    {
        /**
         * first check token type stuff
         */
        if (this.tokentype == null)
        {
            if (node.tokentype != null)
            {
                return false;
            }
        }
        else
        {
            if (!this.tokentype.equals(node.tokentype))
            {
                return false;
            }
        }
        
        if (this.children.size() != node.children.size())
        {
            return false;
        }
        
        Iterator<TokenRule> rules = this.children.keySet().iterator();
        while(rules.hasNext())
        {
            TokenRule rule = rules.next();
            
            TokenRuleNode thischild = this.children.get(rule);
            TokenRuleNode thatchild = node.children.get(rule);
            
            if (thischild == null || thatchild == null)
            {
                return false;
            }
            
            if (!thischild.equals(thatchild))
            {
                return false;
            }
        }
        
        return true;
    }
    
    public void recursiveToString(StringBuilder string, TokenRule gotmehere, int depth)
    {
        string.append('\n');
        for(int i = 0; i < depth; i++)
        {
            string.append('.');
        }
        string.append('[');
        string.append("trigger: ");
        string.append(gotmehere);
        string.append(", tokentype: ");
        string.append(tokentype);
        string.append(']');
        
        Iterator<TokenRule> rules = this.children.keySet().iterator();
        while(rules.hasNext())
        {
            TokenRule rule = rules.next();
            TokenRuleNode thischild = this.children.get(rule);
            thischild.recursiveToString(string, rule, depth + 1);
        }
    }
}
