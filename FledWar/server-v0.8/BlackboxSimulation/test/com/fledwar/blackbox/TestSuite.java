/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox;

import com.fledwar.blackbox.action.UnitImpulseTest;
import com.fledwar.blackbox.connection.ConnectionActionTest;
import com.fledwar.blackbox.connection.ConnectionFocusTest;
import com.fledwar.blackbox.connection.ConnectionLoginTest;
import com.fledwar.blackbox.connection.ConnectionQueryTest;
import com.fledwar.configuration.Configuration;
import com.fledwar.dao.DAOFactoryRegistry;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.mongo.MongoConnect;
import com.fledwar.util.Callback;
import com.mongodb.BasicDBObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author REx
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  BlackboxEngineTest.class,
  
  ConnectionActionTest.class,
  ConnectionFocusTest.class,
  ConnectionLoginTest.class, 
  ConnectionQueryTest.class,
  
  UnitImpulseTest.class
})
public class TestSuite
{
  public static final String TEST_DB_KEY = "MONGO_DATABASE";
  
  public static Configuration config;
  
  public static DAOFactoryRegistry dao_registry;
  
  public static BlackboxEngine startEngine()
          throws Exception
  {
    BlackboxEngine engine;

    long start = System.currentTimeMillis();

    config = new Configuration();
    config.loadResource("/com/fledwar/blackbox/blackbox.groovy.config");

    Log4JHelper.startDefaultLogger();

    MongoConnect.initWithOptions(
            config.getAsConfiguration("mongo"));

    dao_registry = new DAOFactoryRegistry(config);

    GroovyWrapper.init(
            config.getAsString("scriptroot"));

    engine = new BlackboxEngine(
            config.getAsConfiguration("engine"),
            dao_registry);
    try {
      // these tests assume that the db is clear
      String db_name = config.getAsString(TEST_DB_KEY);
      MongoConnect.getDB(db_name).dropDatabase();

      // now make the data
      engine.command("baseline/baseline_data.groovy", new HashMap());
    }
    catch(Exception ex) {
      Writer writer = new StringWriter();
      ex.printStackTrace(new PrintWriter(writer));
      System.err.println(writer.toString());
    }

    System.out.println();
    System.out.println();
    System.out.println("====================================");
    System.out.println(String.format("startup completed in %sms...",
                                     (System.currentTimeMillis() - start)));
    System.out.println("waiting to let balancing occur at least once...");
    Thread.sleep(2500);
    System.out.println("finished...");
    System.out.println("====================================");
    System.out.println();

    return engine;
  }
  
  public static void shutdownEngine(BlackboxEngine engine)
          throws Exception 
  {
    if (engine != null) {
      engine.shutdown();
      Thread.sleep(1000);
    }
    
    if (dao_registry != null) {
      dao_registry.shutdown();
    }
    
    String db_name = config.getAsString(TEST_DB_KEY);
    MongoConnect.getDB(db_name).dropDatabase();
    MongoConnect.closeMongo();
    
    System.out.println();
    System.out.println();
    System.out.println("====================================");
    System.out.println("system shutdown... waiting 2 seconds");
    Thread.sleep(2000);
    System.out.println("finished...");
    System.out.println("====================================");
    System.out.println();
  }
  
  public static class TestCallbacker implements Callback
  {
    public TestCallbacker()
    {
      
    }
    
    Object result;
    
    public boolean hasDrop() {
      synchronized(this) {
        return result != null;
      }
    }
    
    public Object getDrop(long max_wait) {
      long ending = (System.currentTimeMillis() + max_wait);
      while(!hasDrop() && (ending >= System.currentTimeMillis())) {
        try {
          Thread.sleep(50);
        }
        catch(InterruptedException ex) {
        }
      }
      Object returning;
      synchronized(this) {
        returning = result;
        result = null;
      }
      return returning;
    }
    
    @Override
    public Object call(Object... params) throws Exception {
      synchronized(this) {
        result = params[0];
      }
      return null;
    }
  }
  
  public static class CounterCallback implements Callback
  {
    public List<BasicDBObject> unit_updates = new ArrayList<>();
    
    public List<BasicDBObject> point_updates = new ArrayList<>();

    @Override
    public Object call(Object... params) throws Exception
    {
      BasicDBObject result = (BasicDBObject) params[0];
      if (result.getString("update_drop_key").startsWith("galaxy_unit")) {
        unit_updates.add(result);
      }
      else if (result.getString("update_drop_key").startsWith("galaxy_point")) {
        point_updates.add(result);
      }
      
      return null;
    }
    
  }
}
