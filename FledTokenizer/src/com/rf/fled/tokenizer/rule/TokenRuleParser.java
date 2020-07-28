/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

import com.rf.fled.tokenizer.TokenRuleSyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author REx
 */
public class TokenRuleParser
{
    public static class ParsedRule
    {
        public List<Character> attributes = new ArrayList<>();

        public List<TokenRule> rules = new ArrayList<>();
    }
    
    public static ParsedRule factoryRules(String expression)
            throws TokenRuleSyntaxException
    {
        ParsedRule result = new ParsedRule();
        
        RuleType ruletype = null;
        
        SetType chartype = null;
        
        StringBuilder charset = new StringBuilder();
        
        for(int i = 0, n = expression.length(); i < n; i++)
        {
            char character = expression.charAt(i);
            
            if (ruletype == null)
            {
                ruletype = RuleType.discoverRule(character);
                if (ruletype == null)
                {
                    throw new TokenRuleSyntaxException(
                            String.format("illegal start of rule at index %s [%s]", 
                                          i, expression));
                }
                if (ruletype == RuleType.ATTRIBUTE)
                {
                    i++;
                    if (i >= expression.length())
                    {
                        throw new TokenRuleSyntaxException(
                                String.format("unexpected end of a rule [%s]", 
                                              expression));
                    }
                    result.attributes.add(expression.charAt(i));
                    ruletype = null;
                    chartype = null;
                }
            }
            else if (chartype == null)
            {
                chartype = SetType.discoverTypeByInitiator(character);
                switch(chartype)
                {
                    case ESCAPE:
                        i++;
                        character = expression.charAt(i);
                    case SINGLE:
                        result.rules.add(new TokenRule(ruletype,
                                                       chartype, 
                                                       new char[]{character},
                                                       null));
                        ruletype = null;
                        chartype = null;
                        break;
                        
                    case GROUP:
                    case SET:
                        charset = new StringBuilder();
                        break;
                    default:
                        throw new TokenRuleSyntaxException(
                                "HOW DID THIS HAPPEN TO ME!!??!!");
                }
            }
            else if (character == '\\')
            {
                i++;
                charset.append(character);
                charset.append(expression.charAt(i));
            }
            else if (character == chartype.getEnder())
            {
                switch(chartype)
                {
                    case GROUP:
                        result.rules.add(buildTokenForGroup(ruletype, 
                                                            chartype,
                                                            charset));
                        break;
                    case SET:
                        result.rules.add(buildTokenForCharSet(ruletype,
                                                              chartype,
                                                              charset));
                        break;
                    default:
                        throw new TokenRuleSyntaxException(
                                "HOW DID THIS HAPPEN TO ME!!??!!");
                }
                ruletype = null;
                chartype = null;
            }
            else
            {
                charset.append(character);
            }
        }
        
        if (chartype != null || ruletype != null)
        {
            throw new TokenRuleSyntaxException(
                    String.format("expression ended unexpectedly [%s]", 
                                  expression));
        }
        
        return result;
    }
    
    private static TokenRule buildTokenForCharSet(RuleType ruletype, 
                                                  SetType chartype, 
                                                  StringBuilder charset)
            throws TokenRuleSyntaxException
    {
        StringBuilder singles = new StringBuilder();
        
        StringBuilder ranges = new StringBuilder();
        
        String iterating = charset.toString();
        
        boolean onrange = false;
        
        for(int i = 0, n = iterating.length(); i < n; i++)
        {
            char first = iterating.charAt(i);
            if (first == '\\')
            {
                i++;
                first = iterating.charAt(i);
            }
            
            if (i < n - 1 && iterating.charAt(i + 1) == '-')
            {
                // this means a range
                onrange = true;
            }
            
            if (onrange)
            {
                i += 2;
                if (i >= iterating.length())
                {
                    throw new TokenRuleSyntaxException(
                            String.format("illegal character range: must have "
                            + "a character on both sides of an '-', or escape "
                            + "like '\\-' [%s]", iterating));
                }
                char second = iterating.charAt(i);
                if (second == '\\')
                {
                    i++;
                    second = iterating.charAt(i);
                }
                ranges.append(first);
                ranges.append(second);
                onrange = false;
            }
            else
            {
                singles.append(first);
            }
        }
        
        char[] singlechars = null;
        char[] rangechars = null;
        
        if (singles.length() > 0)
        {
            singlechars = new char[singles.length()];
            singles.getChars(0, singles.length(), singlechars, 0);
        }
        if (ranges.length() > 0)
        {
            rangechars = new char[ranges.length()];
            ranges.getChars(0, ranges.length(), rangechars, 0);
        }
        
        return new TokenRule(ruletype, chartype, singlechars, rangechars);
    }
    
    private static TokenRule buildTokenForGroup(RuleType ruletype, 
                                                SetType chartype, 
                                                StringBuilder charset)
            throws TokenRuleSyntaxException
    {
        StringBuilder singles = new StringBuilder();
        
        String iterating = charset.toString();
        
        for(int i = 0, n = iterating.length(); i < n; i++)
        {
            char first = iterating.charAt(i);
            if (first == '\\')
            {
                i++;
                first = iterating.charAt(i);
            }
            singles.append(first);
        }
        
        char[] singlechars = new char[singles.length()];
        singles.getChars(0, singles.length(), singlechars, 0);
        
        return new TokenRule(ruletype, chartype, singlechars, null);
    }
}
