/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

import com.rf.fled.tokenizer.rule.TokenRule;
import com.rf.fled.tokenizer.rule.TokenRuleParser;
import com.rf.fled.tokenizer.rule.TokenRuleParser.ParsedRule;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class TokenType
{
    public final String type;
    
    public final String rule;
    
    public final List<TokenRule> rules;
    
    public final boolean keep;
    
    /**
     * override this method if conversions are needed... for instance,
     * if the rule defines a special case number (like PI), you can
     * convert here if you wish. 
     * @param rawtoken the token that is pulled from the string being parsed.
     * @return 
     */
    public Object doWorkOnToken(String rawtoken)
    {
        return rawtoken;
    }
    
    /**
     * this is a hook for converting tokens programmatically. 
     * 
     * this is the only option right now (6/25/2012) for extra converting, 
     * and depending on how much it is used i (rex) will try to make
     * the common overloads expressable through the rules.
     * 
     * @param expression
     * @param start
     * @param match
     * @param tokens 
     */
    public void postWork(String expression, int start, String match, List<Token> tokens)
    {
        
    }

    public TokenType(String type, String rule)
            throws TokenRuleSyntaxException
    {
        this.type   = type;
        this.rule   = rule;
        ParsedRule parsedrules = TokenRuleParser.factoryRules(rule);
        this.rules  = Collections.unmodifiableList(parsedrules.rules);
        this.keep   = !parsedrules.attributes.contains('k');
    }
    
    @Override
    public String toString()
    {
        return String.format("[type: %s, rule: %s]", type, rule);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TokenType other = (TokenType) obj;
        if (!Objects.equals(this.type, other.type))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
}
