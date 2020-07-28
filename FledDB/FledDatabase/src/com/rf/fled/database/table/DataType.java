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
public interface DataType extends Serializable
{
    /**
     * tells the system if this can be indexed.
     * @return 
     */
    public boolean indexable();
    
    /**
     * returns a human readable description of what the datatype is
     * supposed to do and how its supposed to be read.
     * @return 
     */
    public String description();
    
    /**
     * returns the name that that declares this type to be used.
     * @return 
     */
    public String name();
}
