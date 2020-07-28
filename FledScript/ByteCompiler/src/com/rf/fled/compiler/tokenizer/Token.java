/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.tokenizer;

/**
 *
 * @author REx
 */
public class Token
{
    public TokenType type;
    
    public Object value;
    
    public Token(TokenType type, Object value)
    {
        this.type   = type;
        this.value  = value;
    }
    
    @Override
    public String toString()
    {
        return "[" + type + ", " + value + "]";
    }
    
    public Boolean getValueAsBoolean()
    {
        return (Boolean) value;
    }
    
    public Integer getValueAsInteger()
    {
        return (Integer) value;
    }
    
    public Double getValueAsDouble()
    {
        return (Double) value;
    }
    
    public String getValueAsString()
    {
        return (String) value;
    }
}
