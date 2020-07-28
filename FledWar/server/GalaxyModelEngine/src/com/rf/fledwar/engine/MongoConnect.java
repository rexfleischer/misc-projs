/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class MongoConnect
{
    private static final String[] defaulthosts = 
//            new String[]{"localhost:25795"};
            new String[]{"localhost:27017"};
    
    private static Mongo mongo;
    
    private static String user;
    
    private static char[] pass;
    
    private static String spaceDBName;
    
    public static void initDefault(String spaceDBName) throws UnknownHostException
    {
        initWithOptions(defaulthosts, spaceDBName);
    }
    
    public static void initWithOptions(String[] hosts, String spaceDBName) throws UnknownHostException
    {
        MongoConnect.spaceDBName = Objects.requireNonNull(spaceDBName, "spaceDBName");
        
        ArrayList<ServerAddress> address = new ArrayList<>();
        for (String s : hosts)
        {
            address.add(new ServerAddress(s));
        }
        
        MongoOptions options = new MongoOptions();
        options.setAutoConnectRetry(true);
        options.setConnectionsPerHost(40);
        options.setConnectTimeout(3000);
        
        mongo = new Mongo(address, options);
        mongo.setWriteConcern(WriteConcern.SAFE);
    }
    
    public static void setUserAndPass(String user, char[] pass)
    {
        MongoConnect.user = user;
        MongoConnect.pass = pass;
    }
    
    public static Mongo getMongo()
    {
        return mongo;
    }
    
    public static void closeMongo()
    {
        if (mongo != null)
        {
            mongo.close();
            mongo = null;
        }
    }
    
    public static DB getSpaceDB()
    {
        return getDB(spaceDBName);
    }
    
    public static DB getDB(String dbname)
    {
        DB db = mongo.getDB(dbname);
        if (user != null && pass != null)
        {
            db.authenticate(user, pass);
        }
        return db;
    }
}
