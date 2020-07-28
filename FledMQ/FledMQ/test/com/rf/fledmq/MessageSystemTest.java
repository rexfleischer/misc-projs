/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import org.junit.*;

/**
 *
 * @author REx
 */
public class MessageSystemTest
{
    
    public MessageSystemTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp() throws Exception
    {
        System.out.println("setUp");
        Properties properties = new Properties();
        properties.load(MessageSystemTest.class.getResourceAsStream("/com/rf/fledmq/testconfig.properties"));
        String name = "mongotest";
        MessageSystem.init(properties, name);
    }
    
    @After
    public void tearDown() throws Exception
    {
        System.out.println("tearDown");
        MessageSystem.destroy();
    }

    /**
     * Test of init method, of class MessageSystem.
     */
    @Test
    public void testInit() throws Exception
    {
        System.out.println("testInit");
        MessageQueue queue = MessageSystem.getQueue("test");
        queue.send("hello world!".getBytes());
        Thread.sleep(5);
        
        {
            Message message = queue.receive(100);
            Assert.assertNotNull(message);
            Assert.assertEquals("hello world!", new String(message.getMessage()));
            message.finished();
        }
        
        {
            Message message = queue.receive(100);
            Assert.assertNull(message);
        }
    }
    
    @Test
    public void test_reput() throws Exception
    {
        System.out.println("testInit");
        MessageQueue queue = MessageSystem.getQueue("test");
        queue.send("hello world!".getBytes());
        Thread.sleep(5);
        
        {
            Message message = queue.receive(100);
            Assert.assertNotNull(message);
            Assert.assertEquals("hello world!", new String(message.getMessage()));
            message.reput();
        }
        
        {
            Message message = queue.receive(100);
            Assert.assertNotNull(message);
            Assert.assertEquals("hello world!", new String(message.getMessage()));
            message.finished();
        }
    }
    
    @Test
    public void test_delay() throws Exception
    {
        System.out.println("test_delay");

        for(int i = 0; i < 5; i++)
        {
            MessageQueue queue = MessageSystem.getQueue("test");

            long expected = System.currentTimeMillis() + 2000;
            queue.send("hello world!".getBytes(), 2);

            Message message = queue.receive(10000);
            long received = System.currentTimeMillis();
            System.out.println(String.format("message expected=%s, received=%s, diff=%s", 
                                expected, received, received - expected));
            Assert.assertEquals("hello world!", new String(message.getMessage()));
            Assert.assertTrue(1000 >= Math.abs(received - expected));
            message.finished();
        }

        MessageQueue queue = MessageSystem.getQueue("test");
        Assert.assertEquals(0, queue.getMessageCount());
    }
    
    @Test
    public void test_multipleconsumer() throws Exception
    {
        System.out.println("test_multipleconsumer");

        MessageQueueWatch[] threads = new MessageQueueWatch[3];
        for(int i = 0; i < threads.length; i++)
        {
            threads[i] = new MessageQueueWatch(MessageSystem.getQueue("test"));
            threads[i].start();
        }

        MessageQueue queue = MessageSystem.getQueue("test");

        int amount = 10;
        int iters = 3;
        for(int i = 0; i < amount; i++)
        {
            for(int ii = 0; ii < iters; ii++)
            {
                
                queue.send(Long.toString(System.currentTimeMillis() + 1000 * i + 5000).getBytes(), i + 5);
            }

            Thread.sleep(10);
        }

        while(true)
        {
            int total = 0;
            for(MessageQueueWatch thread : threads)
            {
                total += thread.getMessageCount();
            }
            if (total >= (amount * iters))
            {
                break;
            }
            Thread.sleep(10);
        }

        for(MessageQueueWatch thread : threads)
        {
            System.out.println("messaging to stop");
            thread.messageStop();
        }
        for(MessageQueueWatch thread : threads)
        {
            System.out.println("joining");
            thread.join();
        }

        Assert.assertEquals(0, queue.getMessageCount());
    }
    
    class MessageQueueWatch extends Thread
    {
        private Boolean stop;
        
        private Integer messageCount;
        
        private MessageQueue queue;

        public MessageQueueWatch(MessageQueue queue) 
        {
            this.queue      = queue;
            stop            = false;
            messageCount    = 0;
        }
        
        public void messageStop()
        {
            synchronized(stop)
            {
                stop = true;
            }
        }
        
        public int getMessageCount()
        {
            synchronized(messageCount)
            {
                return messageCount;
            }
        }
        
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    synchronized(stop)
                    {
                        if (stop)
                        {
                            break;
                        }
                    }
                    Message message = queue.receive(1000);
                    
                    if (message != null)
                    {   
                        synchronized(messageCount)
                        {
                            messageCount++;
                        }
                        long received = System.currentTimeMillis();
                        long expected = Long.parseLong(new String(message.getMessage()));

                        System.out.println(String.format(
                                Thread.currentThread().getName() + " says: message expected=%s, received=%s, diff=%s", 
                                expected, received, received - expected));
                        message.finished();
                        Assert.assertTrue(1000 >= Math.abs(received - expected));
                    }
                    else
                    {
                        System.out.println(Thread.currentThread().getName() + " says: nothing found");
                    }
                }
            }
            catch(Exception ex)
            {
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);
                ex.printStackTrace(printWriter);
                System.out.println(result.toString());
            }
        }
    }
}
