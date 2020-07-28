/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

import com.rf.fled.database.table.DataType;

/**
 *
 * @author REx
 */
public interface DataTypeImpl extends DataType
{
    public int deserialize(
            Object[] columns, 
            int index, 
            int location, 
            byte[] buffer);

    public byte[] serialize(
            Object object);

    public long reduceToLong(
            Object object);
}
