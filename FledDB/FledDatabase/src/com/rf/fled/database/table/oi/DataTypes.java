/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

/**
 *
 * @author REx
 */
public enum DataTypes 
{
    BOOLEAN(new DataTypeImpl_Boolean()),
    INTEGER(new DataTypeImpl_Integer()),
    LONG   (new DataTypeImpl_Long   ()),
    STRING (new DataTypeImpl_String ()),
    BINARY (new DataTypeImpl_Binary ());

    private DataTypeImpl instance;
    private DataTypes(DataTypeImpl instance)
    {
        this.instance = instance;
    }

    public DataTypeImpl get()
    {
        return instance;
    }
}
