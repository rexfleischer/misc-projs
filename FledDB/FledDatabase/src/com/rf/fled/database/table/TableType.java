
package com.rf.fled.database.table;

/**
 *
 * @author REx
 */
public enum TableType 
{
    /**
     * this only knows about itself and doesnt try to communicate with 
     * other tables or the database. also, this does not have a size 
     * limit on objects that are put in.
     */
    ORDERED_INDEXED
}
