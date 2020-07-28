/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

import com.mongodb.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
class MongoMessageQueue implements MessageQueueFactory
{
    private static final String MONGO = "mongo";
    
    private static final String HOST = "host";
    
    private static final String PORT = "port";
    
    private static final String DBNAME = "dbname";
    
    private static final String MO_ID = "_id";
    
    private static final String MO_DELIVERYTIME = "delivery";
    
    private static final String MO_MESSAGE = "message";
    
    private static final String MO_STATUS = "status";
    
    private static final int MO_STATUS_PENDING = 0;
    
    private static final int MO_STATUS_PROCESSING = 1;
    
    private String host;
    
    private int port;
    
    private String dbname;
    
    private DB db;
    
    private Mongo mongo;
    
    private ArrayList<String> queuesEnsured;

    public MongoMessageQueue()
    {
    }

    @Override
    public void init(Properties properties, String name, Map<String, Object> hints) 
            throws MessagingException
    {
        if (hints != null && hints.containsKey(MONGO))
        {
            initWithMongo(hints.get(MONGO));
        }
        else
        {
            initWithoutMongo(properties, name);
        }
        
        dbname = Util.getProperty(properties, 
                                MessageSystem.PROPERTIES_FORMAT, 
                                name, 
                                DBNAME);
        db = mongo.getDB(dbname);
        queuesEnsured = new ArrayList<>();
    }
    
    private void initWithoutMongo(Properties properties, String name) 
            throws MessagingException
    {
        host = Util.getProperty(properties, 
                                MessageSystem.PROPERTIES_FORMAT, 
                                name, 
                                HOST);

        String rawPort = Util.getProperty(properties, 
                                        MessageSystem.PROPERTIES_FORMAT, 
                                        name, 
                                        PORT);
        try
        {
            port = Integer.parseInt(rawPort);
        }
        catch(NumberFormatException ex)
        {
            throw new MessagingException(
                    String.format("invalid number in properties [%s]", rawPort), 
                    ex);
        }
        try
        {
            mongo = new Mongo(host, port);
        }
        catch(Exception ex)
        {
            throw new MessagingException(
                    String.format("could not connect to MongoDB at %s:%s", 
                                  host, port), 
                    ex);
        }
        mongo.setWriteConcern(WriteConcern.SAFE);
    }
    
    private void initWithMongo(Object mongomaybe) throws MessagingException
    {
        if (!(mongomaybe instanceof Mongo))
        {
            throw new MessagingException(
                    "hint 'mongo' must be an instance of Mongo");
        }
        mongo = (Mongo) mongo;
    }

    @Override
    public MessageQueue factory(String queue) throws MessagingException
    {
        DBCollection collection = db.getCollection(queue);
        if (!queuesEnsured.contains(queue))
        {
            collection.ensureIndex(new BasicDBObject().append(MO_DELIVERYTIME, 1));
            queuesEnsured.add(queue);
        }
        return new MessageQueueImpl(collection);
    }

    @Override
    public void destroy() throws MessagingException
    {
        db.dropDatabase();
        close();
    }

    @Override
    public void finished() throws MessagingException
    {
        close();
    }
    
    private void close()
    {
        /**
         * we have a host if this object inits mongo. else let the
         * thing that actually init mongo disconnect
         */
        if (host != null)
        {
            db = null;
            mongo.close();
            mongo = null;
        }
    }
    
    class MessageQueueImpl implements MessageQueue
    {
        DBCollection collection;

        MessageQueueImpl(DBCollection collection)
        {
            this.collection = collection;
        }

        @Override
        public void send(byte[] message) throws MessagingException
        {
            send0(message, 0);
        }

        @Override
        public void send(byte[] message, int sdelay) throws MessagingException
        {
            send0(message, sdelay);
        }

        @Override
        public Message receive(int mswait) throws MessagingException
        {
            
            long current = System.currentTimeMillis();
            long end = current + mswait;
            
            BasicDBObject query = new BasicDBObject()
                    .append(MO_STATUS, MO_STATUS_PENDING);
            BasicDBObject sort = new BasicDBObject()
                    .append(MO_DELIVERYTIME, -1);
            BasicDBObject update = new BasicDBObject()
                    .append(MO_STATUS, MO_STATUS_PROCESSING);
            
            do
            {
                int gettime = Util.currentSTime();
                query.put(MO_DELIVERYTIME, new BasicDBObject().append("$lte", gettime));
                
                DBObject object = collection.findAndModify(query, sort, update);
                
                if (object != null)
                {
                    return new MessageImpl(collection, 
                                           object,
                                           gettime);
                }
                
                current = System.currentTimeMillis();
            }
            while(current < end);
            
            return null;
        }
        
        private void send0(byte[] message, int sdelay)
        {
            /**
             * i know its based on the client, but being that
             * mongo db has no native timestamp as of now for
             * java, and that we only need seconds resolution,
             * i think this will be ok. if not, we will have to
             * move to a new 
             */
            int gettime = Util.currentSTime() + sdelay;
            
            BasicDBObject object = new BasicDBObject();
            object.put(MO_MESSAGE, message);
            object.put(MO_DELIVERYTIME, gettime);
            object.put(MO_STATUS, MO_STATUS_PENDING);
            
            collection.insert(object);
        }

        @Override
        public long getMessageCount() throws MessagingException
        {
            return collection.getCount();
        }
        
    }
    
    class MessageImpl implements Message
    {
        private DBCollection collection;
        
        private DBObject object;
        
        private int recieved;
        
        private boolean finished;

        private MessageImpl(DBCollection collection, DBObject object, int recieved)
        {
            this.collection = collection;
            this.object = object;
            this.recieved = recieved;
            this.finished = false;
        }

        @Override
        public int getDeliveryTime()
        {
            finishCheck();
            return (int) object.get(MO_DELIVERYTIME);
        }

        @Override
        public byte[] getMessage()
        {
            finishCheck();
            return (byte[]) object.get(MO_MESSAGE);
        }

        @Override
        public int getTimeSent()
        {
            finishCheck();
            return ((ObjectId) object.get(MO_ID)).getTimeSecond();
        }

        @Override
        public String getQueue()
        {
            finishCheck();
            return collection.getName();
        }

        @Override
        public int getRecieveTime()
        {
            finishCheck();
            return recieved;
        }

        @Override
        public void finished()
        {
            finishCheck();
            collection.remove(new BasicDBObject()
                    .append(MO_ID, object.get(MO_ID)));
            finished = true;
        }

        @Override
        public void reput()
        {
            finishCheck();
            object.put(MO_STATUS, MO_STATUS_PENDING);
            collection.save(object);
            finished = true;
        }
        
        /**
         * we have to make sure that nothing is modified after
         * finished() or reput() is called because it could
         * corrupt the data in the message queue.
         */
        private void finishCheck()
        {
            if (finished)
            {
                throw new IllegalStateException(
                        "cannot work on message after "
                        + "finished() or reput() is called.");
            }
        }
        
    }
}
