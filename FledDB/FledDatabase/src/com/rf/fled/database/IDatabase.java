
package com.rf.fled.database;

import com.rf.fled.database.result.IResultSet;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface IDatabase 
{
    public static final String EXTENSION = "mdb";
    
    public String getName();
    
    public String getHomeFile();
    
    public void init(String directory, String name, Map<String, Object> hints)
            throws DatabaseException;
    
    public void begin()
            throws DatabaseException;
    
    public void commit()
            throws DatabaseException;
    
    public void rollback()
            throws DatabaseException;
    
    public IResultSet query(String query)
            throws DatabaseException;
    
    public IResultSet execute(String query)
            throws DatabaseException;
}
