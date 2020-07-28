/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table;

/**
 *
 * @author REx
 */
public class TableColumnDefinition 
{
    public final String name;
    
    public final String datatype;
    
    public final boolean indexed;
    
    public final boolean unique;
    
    public TableColumnDefinition(
            String name,
            String datatype,
            boolean indexed,
            boolean unique)
    {
        this.name       = name;
        this.datatype   = datatype;
        this.indexed    = indexed;
        this.unique     = unique;
    }
}
