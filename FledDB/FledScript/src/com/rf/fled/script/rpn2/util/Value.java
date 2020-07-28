
package com.rf.fled.script.rpn2.util;

/**
 *
 * @author REx
 */
public class Value
{
    public enum ValueType
    {
        VOID,
        NULL,
        BOOLEAN,
        INTEGER,
        DOUBLE,
        STRING,
        OBJECT,
        UNKNOWN, // meaning it can be any of the above
        REFERENCE,
        VARIABLE,
        
        SYSTEM,  // used for stack popping and pushing
    }
    
    public Value(ValueType type, Object value)
    {
        this.type   = type;
        this.value  = value;
    }
    
    public ValueType type;
    
    public Object value;
    
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
