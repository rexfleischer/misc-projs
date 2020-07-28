/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class JsonHelperTest
{
    
    public JsonHelperTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        JsonHelper.init();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
    
    @Test
    public void bench1() throws Exception
    {
        System.out.println("bench1()");
        
        int TEST_TO = 100000;
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0);
        doBench(TEST_TO, expected);
    }
    
    @Test
    public void bench2() throws Exception
    {
        System.out.println("bench2()");
        
        int TEST_TO = 100000;
        
        BasicDBList expectedlist = new BasicDBList();
        expectedlist.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        expectedlist.add(new BasicDBObject().append("first1", 2345.0).append("first2", "hello2"));
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", expectedlist);
        doBench(TEST_TO, expected);
    }
    
    public void doBench(int TEST_TO, BasicDBObject expected)
    {
        {
            long start = System.currentTimeMillis();
            for(int i = 0; i < TEST_TO; i++)
            {
                JsonHelper.toJson(expected);
            }
            long finish = System.currentTimeMillis();
            System.out.println(String.format("time for %s toJson's: %sms", 
                                             TEST_TO,
                                             (finish - start)));
        }
        
        {
            String deserializing = JsonHelper.toJson(expected);
            long start = System.currentTimeMillis();
            for(int i = 0; i < TEST_TO; i++)
            {
                JsonHelper.toJavaMap(deserializing);
            }
            long finish = System.currentTimeMillis();
            System.out.println(String.format("time for %s toJavaMap's: %sms", 
                                             TEST_TO,
                                             (finish - start)));
        }
    }
    
    @Test
    public void testRoundTrip1() throws Exception
    {
        System.out.println("testRoundTrip1()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0);
        String json = JsonHelper.toJson(expected);
        
        Assert.assertEquals("{\"stuff\":true,\"stuff1\":1234.0}", json);
        
        BasicDBObject actual = JsonHelper.toJavaMap(json);
        
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testRoundTrip2() throws Exception
    {
        System.out.println("testRoundTrip2()");
        
        BasicDBList list = new BasicDBList();
        list.add("hello");
        list.add("cool");
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", list);
        String json = JsonHelper.toJson(expected);
        
        Assert.assertEquals("{\"stuff\":true,\"stuff4\":[\"hello\",\"cool\"]}", json);
        
        BasicDBObject actual = JsonHelper.toJavaMap(json);
        
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testRoundTrip3() throws Exception
    {
        System.out.println("testRoundTrip3()");
        
        BasicDBList list = new BasicDBList();
        list.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        list.add(new BasicDBObject().append("first1", 2345.0).append("first2", "hello2"));
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", list);
        String json = JsonHelper.toJson(expected);
        
        Assert.assertEquals(
                "{\"stuff\":true,\"stuff4\":["
                    + "{\"first1\":1234.0,\"first2\":\"hello\"},"
                    + "{\"first1\":2345.0,\"first2\":\"hello2\"}"
                    + "]"
                + "}", json);
        
        BasicDBObject actual = JsonHelper.toJavaMap(json);
        
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testRoundTrip4() throws Exception
    {
        System.out.println("testRoundTrip4()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("thank", "you")
                .append("_id", new ObjectId("123412341234123412341234"));
        String json = JsonHelper.toJson(expected);
        
        Assert.assertEquals("{\"stuff\":true,"
                           + "\"stuff1\":1234.0,"
                           + "\"thank\":\"you\","
                           + "\"_id\":\"123412341234123412341234\"}", 
                json);
        
        BasicDBObject actual = JsonHelper.toJavaMap(json);
        
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testEquals1() throws Exception
    {
        System.out.println("testEquals1()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0);
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0);
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(expected.equals(actual));
    }
    
    @Test
    public void testEquals2() throws Exception
    {
        System.out.println("testEquals2()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0);
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff2", 1234.0);
        Assert.assertFalse(expected.equals(actual));
    }
    
    @Test
    public void testEquals3() throws Exception
    {
        System.out.println("testEquals3()");
        
        BasicDBList expectedlist = new BasicDBList();
        expectedlist.add("tool");
        expectedlist.add("stuff");
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", expectedlist);
        
        BasicDBList actuallist = new BasicDBList();
        actuallist.add("tool");
        actuallist.add("stuff");
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", actuallist);
        
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(expected.equals(actual));
    }
    
    @Test
    public void testEquals4() throws Exception
    {
        System.out.println("testEquals4()");
        
        BasicDBList expectedlist = new BasicDBList();
        expectedlist.add("tool");
        expectedlist.add("stuff");
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", expectedlist);
        
        BasicDBList actuallist = new BasicDBList();
        actuallist.add("tool");
        actuallist.add("stuff2");
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", actuallist);
        
        Assert.assertFalse(expected.equals(actual));
    }
    
    @Test
    public void testEquals5() throws Exception
    {
        System.out.println("testEquals5()");
        
        BasicDBList expectedlist = new BasicDBList();
        expectedlist.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        expectedlist.add(new BasicDBObject().append("first1", 2345.0).append("first2", "hello2"));
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", expectedlist);
        
        BasicDBList actuallist = new BasicDBList();
        actuallist.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        actuallist.add(new BasicDBObject().append("first1", 2345.0).append("first2", "hello2"));
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", actuallist);
        
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(expected.equals(actual));
    }
    
    @Test
    public void testEquals6() throws Exception
    {
        System.out.println("testEquals6()");
        
        BasicDBList expectedlist = new BasicDBList();
        expectedlist.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        expectedlist.add(new BasicDBObject().append("first1", 2345.0).append("first2", "hello2"));
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", expectedlist);
        
        BasicDBList actuallist = new BasicDBList();
        actuallist.add(new BasicDBObject().append("first1", 1234.0).append("first2", "hello"));
        actuallist.add(new BasicDBObject().append("first1", 1234213432.0).append("first2", "hello2"));
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff4", actuallist);
        
        Assert.assertFalse(expected.equals(actual));
    }
    
    @Test
    public void testEquals7() throws Exception
    {
        System.out.println("testEquals7()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", new ObjectId("123412341234123412341234"));
        
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", new ObjectId("123412341234123412341234"));
        
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(expected.equals(actual));
    }
    
    @Test
    public void testEquals8() throws Exception
    {
        System.out.println("testEquals8()");
        
        BasicDBObject expected = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", new ObjectId("123412341234123412341234"));
        
        BasicDBObject actual = new BasicDBObject()
                .append("stuff", true)
                .append("stuff1", 1234.0)
                .append("stuff0", new ObjectId("123412341234123412340000"));
        
        Assert.assertFalse(expected.equals(actual));
    }
    
    /**
     * be warned... this test fails because ObjectId#equals method
     * checks against the value as if the value is a string.
     * 
     * to see an illustration of this, see the test after this one
     */
//    @Test
//    public void testEquals9() throws Exception
//    {
//        System.out.println("testEquals9()");
//        
//        BasicDBObject expected = new BasicDBObject()
//                .append("stuff", true)
//                .append("stuff1", 1234.0)
//                .append("stuff0", new ObjectId("123412341234123412341234"));
//        
//        BasicDBObject actual = new BasicDBObject()
//                .append("stuff", true)
//                .append("stuff1", 1234.0)
//                .append("stuff0", "123412341234123412341234");
//        
//        Assert.assertFalse(actual.equals(expected));
//        Assert.assertFalse(expected.equals(actual));
//    }
//    @Test
//    public void testStupidObjectId() throws Exception
//    {
//        String rawstring = "123412341234123412341234";
//        ObjectId objectid = new ObjectId(rawstring);
//        
//        Assert.assertFalse(rawstring.equals(objectid));
//        Assert.assertFalse(objectid.equals(rawstring));
//    }
}
