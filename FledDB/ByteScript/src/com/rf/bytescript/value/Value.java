
package com.rf.bytescript.value;

/**
 *
 * @author REx
 */
public class Value
{
    
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
