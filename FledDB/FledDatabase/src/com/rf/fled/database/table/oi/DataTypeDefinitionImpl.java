/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

import com.rf.fled.database.table.DataType;
import com.rf.fled.database.table.DataTypeDefinition;

/**
 *
 * @author REx
 */
public class DataTypeDefinitionImpl implements DataTypeDefinition
{
    private static DataType[] dataTypes;
    
    private static DataTypeDefinition instance;
    
    static
    {
        DataTypes[] values = DataTypes.values();
        DataType[] result = new DataType[values.length];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = values[i].get();
        }
        dataTypes = result;
        instance = new DataTypeDefinitionImpl();
    }
    
    public static DataTypeDefinition getInstance()
    {
        // can touch this
        return instance;
    }
    
    private DataTypeDefinitionImpl()
    {
        // cant touch this
    }

    @Override
    public DataType getDataType(String datatype) 
    {
        return DataTypes.valueOf(datatype.toUpperCase()).get();
    }

    @Override
    public DataType[] getDataTypeList() 
    {
        return dataTypes;
    }
}
