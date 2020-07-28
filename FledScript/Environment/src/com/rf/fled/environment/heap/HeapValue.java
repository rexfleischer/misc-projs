
package com.rf.fled.environment.heap;

/**
 *
 * @author REx
 */
public class HeapValue
{
    public HeapValue(HeapValueType type, Object value)
    {
        this.type   = type;
        this.value  = value;
    }
    
    public HeapValueType type;
    
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
