/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

import com.rf.fled.database.table.TableColumn;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.util.ByteArray;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class ValueColumnSeralizer implements ISerializer<byte[]>
{
    private TableColumn[] columns;
        
    public ValueColumnSeralizer(TableColumn[] columns)
    {
        this.columns = columns;
    }

    @Override
    public Object deserialize(byte[] buffer)
            throws IOException
    {
        Object[] result = new Object[columns.length];
        int location = 0;
        for(int i = 0; i < columns.length; i++)
        {
            location = ((DataTypeImpl) columns[i].datatype)
                    .deserialize(result, i, location, buffer);
        }
        return result;
    }

    @Override
    public byte[] serialize(Object obj)
            throws IOException
    {
        Object[] objects = (Object[]) obj;
        ByteArray bytes = new ByteArray();
        for(int i = 0; i < objects.length; i++)
        {
            byte[] toAppend = ((DataTypeImpl) columns[i].datatype)
                    .serialize(objects[i]);
            bytes.write(toAppend, bytes.compacityUsed());
        }
        return bytes.copyUsedBytes();
    }
}
