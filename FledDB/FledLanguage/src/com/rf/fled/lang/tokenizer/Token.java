/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.tokenizer;

/**
 *
 * @author REx
 */
public class Token implements Comparable<Token>
{
    public String name;
    
    public TokenType type;
    
    public Object value;
    
    
    public Token(String name, TokenType type, Object value)
    {
        this.name   = name;
        this.type   = type;
        this.value  = value;
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
    
    @Override
    public String toString()
    {
        return "[" + type.name() 
                + ", " + name + ", " 
                + (value != null ? value.toString() : "null") 
                + "]";
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (!(o instanceof Token))
        {
            return false;
        }
        Token other = (Token) o;
        
        /**
         * first check type because its the cheapest
         */
        if (type != other.type)
        {
           return false; 
        }
        
        /*
         * check if they are both null or not null
         */
        if ((name == null) != (other.name == null))
        {
            return false;
        }
        if (name != null && !name.equals(other.name))
        {
            return false;
        }
        
        
        if ((value == null) != (other.value == null))
        {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 59 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Token o) 
    {
        return name.compareTo(o.name);
    }
}
