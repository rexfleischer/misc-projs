/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.configuration.Configuration;
import com.fledwar.mongo.MongoConnect;
import com.fledwar.util.PoolManager;
import com.fledwar.util.PoolManagerException;
import com.mongodb.DBCollection;
import java.lang.reflect.Constructor;
import org.apache.log4j.Logger;

/**
 * 
 * @author REx
 */
public class DAOFactory<T extends BasicDAO> extends PoolManager<T>
{
    public static final String DATABASE = "database";
    
    public static final String COLLECTION = "collection";
    
    private static final Logger logger = Logger.getLogger(DAOFactory.class);
    
    private String database;
    
    private String collection;
    
    private Class clazz;
    
    private Constructor constructor;
    
    public DAOFactory(Configuration config, Class clazz)
            throws BasicDAOException, PoolManagerException
    {
        super(3, 1000, false);
        try
        {
            this.clazz = clazz;
            constructor = clazz.getConstructor(DBCollection.class);
            database = config.getAsString(DATABASE);
            collection = config.getAsString(COLLECTION);
        }
        catch(Exception ex)
        {
            throw new BasicDAOException("could not configure dao factory", ex);
        }
        logger.info("dao factory created with stats...");
        logger.info(String.format("dao factory for class: %s", clazz));
        logger.info(String.format("dao factory for database: %s", database));
        logger.info(String.format("dao factory for collection: %s", collection));
    }

    @Override
    protected Poolable factory(Object... extraparams) 
            throws PoolManagerException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(String.format(
                    "attempting to create dao for class %s with %s.%s", 
                    clazz, database, collection));
        }
        try
        {
            return (T) constructor.newInstance(getNewCollection());
        }
        catch(Exception ex)
        {
            throw new PoolManagerException("unable to factory dao", ex);
        }
    }
    
    private DBCollection getNewCollection()
    {
        return MongoConnect.getDB(database).getCollection(collection);
    }
    
}
