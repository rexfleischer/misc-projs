/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

/**
 *
 * @author REx
 */
public enum RuleType
{
    LITERAL  ('-'),
    NOT      ('~'),
    MANY     ('*'),
    ATTRIBUTE('@');
    
    private char rule;
    
    private RuleType(char rule)
    {
        this.rule = rule;
    }
    
    public char getRule()
    {
        return rule;
    }
    
    public static RuleType discoverRule(char character)
    {
        RuleType[] values = values();
        for(RuleType value : values)
        {
            if (value.getRule() == character)
            {
                return value;
            }
        }
        return null;
    }
}
