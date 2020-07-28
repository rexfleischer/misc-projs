/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer.rule;

/**
 *
 * @author REx
 */
public enum SetType
{
    SINGLE  ((char)0x00, (char)0x00),
    ESCAPE  ('\\', (char)0x00),
    SET     ('[', ']'),
    GROUP   ('(', ')');
    
    private char init;
    
    private char ender;
    
    private SetType(char init, char ender)
    {
        this.init   = init;
        this.ender  = ender;
    }
    
    public char getInit()
    {
        return init;
    }
    
    public char getEnder()
    {
        return ender;
    }
    
    public static SetType discoverTypeByInitiator(char initiator)
    {
        SetType[] values = values();
        for(SetType value : values)
        {
            if (value.getInit() == initiator)
            {
                return value;
            }
        }
        return SINGLE;
    }
}
