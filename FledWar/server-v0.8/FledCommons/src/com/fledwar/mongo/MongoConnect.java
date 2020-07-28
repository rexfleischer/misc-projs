/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.mongo;

import com.fledwar.configuration.Configuration;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this is pretty strait forward because there isnt much for us to do. but we do
 * need to configure a little bit with hosts, user/pass, and name abstractions.
 * <p/>
 * hosts are for telling the mongo driver where the nodes to query from are.
 * this could be as many as we specify, but it does need to be in a list.
 * <p/>
 * next, we can optionally include an user/pass that will be put in every
 * authentication object that is created through getAbstractedDB() or getDB().
 * <p/>
 * name abstractions are for easy changing of names of databases. for instance,
 * if there is testing or statuses that need to be ran, we can make the database
 * on completely different places within mongo.
 * <p/>
 * an example of the configuration is as such: some.groovy.key = [ user :
 * "OMG!", pass : "asdfqwer", hosts : ["somehost:1234", "somewhere:3456"], ];
 * <p/>
 * @author REx
 */
public class MongoConnect
{
  public static final String HOSTS = "hosts";
  
  public static final String URL = "url";
  
  public static final String USER = "user";
  
  public static final String PASS = "pass";
  
  public static final String ENVIRONMENT_KEY = "FLEDWAR_MONGO";
  
  
  
  private static MongoClient mongo;
  
  private static String user;
  
  private static char[] pass;

  public static void initDefault()
          throws IOException
  {
//        Configuration default_config = new Configuration();
//        default_config.loadResource("/com/fledwar/mongo/mongo.groovy.config");
//        initWithOptions(default_config.getAsConfiguration("mongo"));
    mongo = new MongoClient();
  }
  
  public static void initWithEnvironment() 
          throws IOException {
    
    
  }
  
  public static void initWithOptions(Configuration config)
          throws UnknownHostException
  {
    user = config.getAsString(USER);
    pass = (config.containsKey(PASS))
            ? config.getAsString(PASS).toCharArray()
            : null;

    if (config.containsKey(URL)) {
      mongo = new MongoClient(new MongoClientURI(
              config.getAsString(URL)));
      mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
    }
    else {
      List<String> hosts = config.getAsList(HOSTS);
      ArrayList<ServerAddress> address = new ArrayList<>();
      for(String s : hosts) {
        address.add(new ServerAddress(s));
      }

      MongoClientOptions options = new MongoClientOptions.Builder()
              .readPreference(ReadPreference.primaryPreferred())
              .writeConcern(WriteConcern.ACKNOWLEDGED)
              .build();

      mongo = new MongoClient(address, options);
    }
  }

  public static MongoClient getMongo()
  {
    return mongo;
  }

  public static void closeMongo()
  {
    if (mongo != null) {
      mongo.close();
      mongo = null;
    }
    user = null;
    pass = null;
  }

  public static DB getDB(String dbname)
  {
    Objects.requireNonNull(dbname, "dbname");

    DB db = mongo.getDB(dbname);
    if (user != null && pass != null) {
      db.authenticate(user, pass);
    }
    return db;
  }
}
