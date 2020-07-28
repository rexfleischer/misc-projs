/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table;

/**
 *
 * @author REx
 */
public interface DataTypeDefinition 
{
    /**
     * hand in a string and this interface will return the 
     * @param datatype
     * @return
     */
    public DataType getDataType(String datatype);
    
    /**
     * gets a list of all the datatypes that are supported 
     * @return 
     */
    public DataType[] getDataTypeList();
}
