/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.sql;

/**
 *
 * @author REx
 */
public class SqlValue
{
    public Object value;
    
    public SqlValueType type;
    
    public SqlValue(SqlValueType type, Object value)
    {
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
}
