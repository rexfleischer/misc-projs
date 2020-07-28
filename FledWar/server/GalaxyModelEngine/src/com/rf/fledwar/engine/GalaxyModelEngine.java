/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine;

import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.engine.thread.ThreadManagerRegistry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class GalaxyModelEngine
{
    /**
     * this is the kill switch for mongo at start up
     * 
     * possible values:
     * true
     * false
     */
    public static final String START_MONGO              = "gm.start.mongo";
    
    /**
     * kill switch for background threads
     * 
     * possible values:
     * true
     * false
     */
    public static final String START_BACKGROUND_THREADS = "gm.start.bgthreads";
    
    /**
     * 
     */
    public static final String MONGO_HOSTS              = "gm.mongo.hosts";
    
    public static final String MONGO_ENSURE_INDEXES     = "gm.mongo.insureindex";
    
    public static final String MONGO_ENSURE_NULL_STATE  = "gm.mongo.insurenull";
    
    public static final String MONGO_ENSURE_TIME        = "gm.mongo.insuretime";
    
    public static final String MONGO_SPACE_DB_NAME      = "gm.mongo.spacedbname";
    
    public static final String MONGO_AUTH_USER          = "gm.mongo.auth.user";
    
    public static final String MONGO_AUTH_PASS          = "gm.mongo.auth.pass";
    
    public static final String BACKGROUND_THREAD        = "gm.bgthread";
    
    public static final String TIME_SCALE               = "gm.timescale";
    
    private static Properties gmprops;
    
    private static Double timeScaleCache;
    
    public static Properties getGMProperties()
    {
        return gmprops;
    }
    
    public static void start(Properties gmprops) throws Exception
    {
        GalaxyModelEngine.gmprops = gmprops;
        
        Logger logger = Logger.getLogger(GalaxyModelEngine.class);
        
        Boolean startMongo = Boolean.parseBoolean(
                gmprops.getProperty(START_MONGO, "false"));
        logger.info(String.format("string mongo: %s", startMongo));
        
        Boolean startBackground = Boolean.parseBoolean(
                gmprops.getProperty(START_BACKGROUND_THREADS, "false"));
        logger.info(String.format("string background threads: %s", startBackground));
        
        String rawtime = gmprops.getProperty(TIME_SCALE, "1.0");
        timeScaleCache = Double.parseDouble(rawtime);

        try
        {
            if (startMongo)
            {
                Boolean insureIndexes = Boolean.parseBoolean(
                        gmprops.getProperty(MONGO_ENSURE_INDEXES, "true"));
                Boolean ensureNullState = Boolean.parseBoolean(
                        gmprops.getProperty(MONGO_ENSURE_NULL_STATE, "true"));
                Boolean ensureTime = Boolean.parseBoolean(
                        gmprops.getProperty(MONGO_ENSURE_TIME, "true"));
                String user = gmprops.getProperty(MONGO_AUTH_USER);
                String pass = gmprops.getProperty(MONGO_AUTH_PASS);
                if (user == null   || pass == null ||
                    user.isEmpty() || pass.isEmpty())
                {
                    logger.warn(String.format("prop key %s or %s is empty", 
                                              MONGO_AUTH_PASS, 
                                              MONGO_AUTH_USER));
                    user = null;
                    pass = null;
                }
                
                String spaceDBName = gmprops.getProperty(MONGO_SPACE_DB_NAME);
                if (spaceDBName == null)
                {
                    String errmsg = String.format("%s must be specified", 
                                                  MONGO_SPACE_DB_NAME);
                    logger.error(errmsg);
                    throw new GalaxyModelEngineException(errmsg);
                }
                
                String[] hosts = gmprops.getProperty(MONGO_HOSTS).split("\\,");
                for(int i = 0; i < hosts.length; i++)
                {
                    hosts[i] = hosts[i].trim();
                }
                
                startMongo(hosts, user, pass,
                           spaceDBName,
                           insureIndexes,
                           ensureNullState,
                           ensureTime);
            }

            if (startBackground)
            {
                Map<Class, Map<String, String>> classToConfig = new HashMap<>();
                Iterator it = gmprops.keySet().iterator();
                while(it.hasNext())
                {
                    String key = it.next().toString();
                    if (key.startsWith(BACKGROUND_THREAD))
                    {
                        String rawclazz = key.substring(BACKGROUND_THREAD.length() + 1,
                                                        key.lastIndexOf("."));
                        String rawkey = key.substring(key.lastIndexOf(".") + 1);
                        String rawvalue = gmprops.getProperty(key);
                        
                        Class clazz = Class.forName(rawclazz);
                        Map<String, String> clazzConfig = classToConfig.get(clazz);
                        if (clazzConfig == null)
                        {
                            clazzConfig = new HashMap<>();
                            classToConfig.put(clazz, clazzConfig);
                        }
                        
                        clazzConfig.put(rawkey, rawvalue);
                    }
                }
                
                startBackgroundThreads(classToConfig);
            }
        }
        catch(Exception ex)
        {
            try
            {
                shutdown();
            }
            catch(Exception OMG)
            {
                logger.error("error while trying to shutdown after failed start", OMG);
            }
            throw ex;
        }
    }
    
    public static void shutdown() throws Exception
    {
        ThreadManagerRegistry.stop();
        MongoConnect.closeMongo();
    }
    
    private static void startMongo(String[] hosts, 
                                   String user, 
                                   String pass, 
                                   String spaceDBName,
                                   boolean ensureIndexes,
                                   boolean ensureNullState,
                                   boolean ensureTime) throws Exception
    {
        MongoConnect.initWithOptions(hosts, spaceDBName);
        if (user != null && pass != null)
        {
            MongoConnect.setUserAndPass(user, pass.toCharArray());
        }
        if (ensureIndexes)
        {
            GalaxySystemDAO.ensureAllIndexes();
        }
        if (ensureNullState)
        {
            GalaxySystemDAO.ensureNullState();
        }
        if (ensureTime)
        {
            GalaxySystemDAO.ensureNullTime();
        }
    }
    
    private static void startBackgroundThreads(
            Map<Class, Map<String, String>> classToConfig) 
            throws Exception
    {
        Iterator<Class> it = classToConfig.keySet().iterator();
        while(it.hasNext())
        {
            Class clazz = it.next();
            Map<String, String> config = classToConfig.get(clazz);
            ThreadManagerRegistry.register(clazz, config);
        }
        ThreadManagerRegistry.start();
    }

    public static Double getTimeScale()
    {
        return timeScaleCache;
    }
}
