/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.filemanager;

import java.io.ByteArrayInputStream;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.util.StreamSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author REx
 */
public class FlatFileManagerTest {
    
    public FlatFileManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void generalTest() throws Exception {
        System.out.println("savePage");
        FlatFileManager instance = new FlatFileManager(System.getProperty("user.dir"));
        Serializer<ByteBuffer> serializer = new MockPageSerializer();
        
        instance.savePage("123", new MockPage(123L, "hello 123"), serializer);
        instance.savePage("124", new MockPage(124L, "hello 124"), serializer);
        instance.savePage("125", new MockPage(125L, "hello 125"), serializer);
        
        MockPage inst = null;
        
        inst = (MockPage) instance.getPage("123", serializer);
        assertEquals(inst.content, "hello 123");
        
        inst = (MockPage) instance.getPage("124", serializer);
        assertEquals(inst.content, "hello 124");
        
        inst = (MockPage) instance.getPage("125", serializer);
        assertEquals(inst.content, "hello 125");
        
        instance.deletePage("123");
        instance.deletePage("124");
        instance.deletePage("125");
        
        assertNull(instance.getPage("123", serializer));
        assertNull(instance.getPage("124", serializer));
        assertNull(instance.getPage("125", serializer));
    }
    
    public class MockPageSerializer implements Serializer<ByteBuffer>
    {

        @Override
        public Object deserialize(ByteBuffer buffer) 
                throws IOException
        {
            try
            {
                byte[] toArray = new byte[buffer.capacity()];
                buffer.position(0);
                buffer.get(toArray);
                return StreamSerializer.deserialize(new ByteArrayInputStream(toArray));
            }
            catch(Exception ex)
            {
                throw new IOException(ex);
            }
        }

        @Override
        public ByteBuffer serialize(Object obj) 
                throws IOException 
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StreamSerializer.serialize(stream, obj);
            ByteBuffer result = ByteBuffer.allocate(stream.size());
            result.put(stream.toByteArray());
            stream.close();
            return result;
        }
        
    }
}
