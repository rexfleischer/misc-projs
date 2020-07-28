/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table;

import com.rf.fled.database.DatabaseException;
import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.KeyValuePair;

/**
 *
 * @author REx
 */
public interface ITable 
{
    public static final String PRIMARY_KEY = "id";
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void begin()
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void commit()
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void rollback()
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void truncate()
            throws DatabaseException;
    
    /**
     * basically, value.length must equal the amount of columns and be
     * the correct datatypes. 
     * @param values
     * @return the id that is associated with it
     * @throws DatabaseException 
     */
    public long insert(Object[] values)
            throws DatabaseException;
    
    /**
     * 
     * @param id
     * @param values
     * @throws DatabaseException 
     */
    public void update(long id, Object[] values)
            throws DatabaseException;
    
    /**
     * 
     * @param id
     * @return
     * @throws DatabaseException 
     */
    public Object[] delete(long id)
            throws DatabaseException;
    
    /**
     * 
     * @param id
     * @return
     * @throws DatabaseException 
     */
    public Object[] select(long id)
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void close()
            throws DatabaseException;
    
    /**
     * 
     */
    public DataTypeDefinition getDataTypeDefinition()
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public long count()
            throws DatabaseException;
    
    /**
     * 
     * @throws DatabaseException 
     */
    public void drop()
            throws DatabaseException;
    
    /**
     * 
     * @return
     * @throws DatabaseException 
     */
    public TableColumn[] getColumnDefinition()
            throws DatabaseException;
    
    /**
     * 
     * @param column
     * @param start
     * @return
     * @throws DatabaseException 
     */
    public IBrowser<KeyValuePair<Long, Object>> browseIndexer(String column, Long start)
            throws DatabaseException;
    
    /**
     * 
     * @param start
     * @return
     * @throws DatabaseException 
     */
    public IBrowser<KeyValuePair<Long, Object>> browseKeys(Long start)
            throws DatabaseException;
}
