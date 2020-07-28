/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class TableColumn implements Serializable
{
    public final String name;
    
    public final DataType datatype;
    
    public final boolean indexed;
    
    public final boolean unique;
    
    public TableColumn(String name, DataType datatype, boolean indexed, boolean unique)
    {
        this.name       = name;
        this.datatype   = datatype;
        this.indexed    = indexed;
        this.unique     = unique;
    }
}
