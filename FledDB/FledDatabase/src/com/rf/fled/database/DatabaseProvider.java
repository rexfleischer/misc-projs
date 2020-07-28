/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database;

import com.rf.fled.database.nonmanager.DatabaseImpl;
import com.rf.fled.persistence.util.BasicStreamIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 
 * @author REx
 */
public class DatabaseProvider 
{
    public static IDatabase createDatabase(
            String directory, 
            String name,
            DatabaseType type,
            Map<String, Object> hints)
            throws DatabaseException
    {
        if (directory == null || directory.isEmpty())
        {
            throw new IllegalArgumentException("directory must be specified");
        }
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("name must be specified");
        }
        if (type == null)
        {
            throw new IllegalArgumentException("type must be specified");
        }
        if (hints == null)
        {
            throw new IllegalArgumentException("hints cannot be null");
        }
        switch(type)
        {
            case NONMANAGER:
                DatabaseImpl database = new DatabaseImpl();
                database.init(directory, name, hints);
                return database;
            default:
                throw new IllegalArgumentException("unknown database type");
        }
    }
    
    public static IDatabase loadDatabase(String directory, String name) 
            throws IOException, ClassNotFoundException
    {
        return loadDatabase(directory + "/" + name + "." + IDatabase.EXTENSION);
    }
    
    public static IDatabase loadDatabase(String homeFile) 
            throws IOException, ClassNotFoundException
    {
        return (IDatabase) BasicStreamIO.streamToObject(new FileInputStream(homeFile));
    }
}
