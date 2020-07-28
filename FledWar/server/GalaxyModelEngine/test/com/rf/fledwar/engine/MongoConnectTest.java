/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class MongoConnectTest
{
    
    @Test
    public void testSimplyConnecting() throws Exception
    {
        MongoConnect.initDefault("testspacedb");
        DBCollection collection = MongoConnect.getSpaceDB().getCollection("testc");
        
        collection.insert(new BasicDBObject().append("hello", "world"));
    }
}
