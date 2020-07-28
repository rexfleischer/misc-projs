/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.tables;

import com.rf.fled.database.table.oi.TableImpl;
import com.rf.fled.database.DatabaseException;
import com.rf.fled.database.table.TableColumnDefinition;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class OrderedIndex_TableTest {
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data";
    
    public static final String COLUMN_1_SOMETHING   = "something";
    public static final String COLUMN_2_TIME        = "time";
    public static final String COLUMN_3_ISSTUFF     = "isStuff";
    
    public OrderedIndex_TableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        File[] files = (new File(DIRECTORY)).listFiles();
        for(File file : files)
        {
            file.delete();
        }
    }
    
    @After
    public void tearDown() {
    }
    
    public TableImpl getNew() throws DatabaseException
    {
        TableImpl result = new TableImpl();
        
        TableColumnDefinition[] columns = new TableColumnDefinition[]
        {
            new TableColumnDefinition("something", "string", false, false),
            new TableColumnDefinition("time", "long", true, false),
            new TableColumnDefinition("isStuff", "boolean", false, false),
        };
        
        result.init(DIRECTORY, "test", columns, null);
        return result;
    }

    @Test
    public void testHappyPath()
            throws Exception 
    {
        TableImpl instance = getNew();
        ArrayList<Long> ids = new ArrayList<Long>();
        
        instance.begin();
        for(int i = 0; i < 100001; i++)
        {
            Object[] inserts = new Object[3];
            inserts[0] = "hello world: " + i;
            inserts[1] = System.currentTimeMillis();
            inserts[2] = (i % 2 == 0);
            ids.add(instance.insert(inserts));
            if (i % 10 == 0)
            {
                System.out.println("inserted " + i + " keys");
            }
        }
        instance.commit();
        
        int count = 0;
        instance.begin();
        Iterator<Long> it = ids.iterator();
        while(it.hasNext())
        {
            long id = it.next();
            Object[] record = instance.select(id);

            count++;
            if (count % 100 == 0)
            {
                System.out.println(
                        "id-" + record[0] 
                        + " 1-" + record[1] 
                        + " 2-" + record[2] 
                        + " 3-" + record[3]);
            }
        }
        instance.rollback();
        
        count = 0;
        it = ids.iterator();
        instance.begin();
        while(it.hasNext())
        {
            long id = it.next();
            try
            {
                Object[] record = instance.delete(id);
                Assert.assertEquals(3, record.length);

                count++;
                if (count % 10 == 0)
                {
                    System.out.println("deleted " + count + " records");
                }
            }
            catch(Exception ex)
            {
                System.out.println("error deleting on key = " + id);
                throw ex;
            }
        }
        instance.commit();
        
        instance.drop();
    }
}
