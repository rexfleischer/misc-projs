/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import com.rf.fledwar.socket.Message;
import java.util.Map;
import java.util.Objects;

/**
 * when an order is required for communication, then
 * we start a protocol implementation. this will handle
 * the validation and ordering of receiving and sending
 * messages between the client and server.
 * @author REx
 */
public abstract class Conversation
{
    /**
     * the name of the conversation. 
     * 
     * NOTE: there can only be one of each name
     * going on at the same time for a single socket.
     */
    protected final String name;
    
    /**
     * 
     */
    protected ConnectionLiaison data;
    
    /**
     * 
     */
    protected boolean thisStarted;
    
    /**
     * 
     */
    protected int timeout;
    
    /**
     * 
     */
    protected String errorMessage;
    
    /**
     * 
     */
    protected ConversationState state;
    
    /**
     * 
     */
    protected long lastresponse;
    
    /**
     * 
     */
    protected Map<String, Object> params;
    
    /**
     * 
     * @param name 
     */
    protected Conversation(String name)
    {
        this.name = Objects.requireNonNull(name, "name");
        this.state = ConversationState.NEW;
        this.thisStarted = true;
        this.timeout = -1;
        this.data = null;
    }

    public String getName()
    {
        return name;
    }

    public ConnectionLiaison getData()
    {
        return data;
    }

    public boolean isThisStarted()
    {
        return thisStarted;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public ConversationState getState()
    {
        return state;
    }

    public long getLastResponse()
    {
        return lastresponse;
    }

    public Map<String, Object> getParams()
    {
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public void setData(ConnectionLiaison data)
    {
        this.data = data;
    }

    public void setThisStarted(boolean thisStarted)
    {
        this.thisStarted = thisStarted;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    protected void setState(ConversationState state)
    {
        this.state = state;
    }

    public void triggerLastResponse()
    {
        this.lastresponse = System.currentTimeMillis();
    }
    
    
    
    
    /**
     * 
     * @return 
     */
    protected Message getEmptyMessage()
    {
        Message result = new Message();
        result.setProtocol(name);
        return result;
    }
    
    /**
     * implementation is supposed to override if the initial message
     * sent is supposed to be different than just an empty message.
     * @return
     * @throws MessageProtocolException 
     */
    public abstract Message getInitialMessage() throws ConversationException;
    
    /**
     * this does one step of the protocol. the input is guaranteed to
     * be not null. the response will be sent to the client if it is
     * not null... the protocol will not end until the state is either
     * ERROR or FINISHED.
     *          
     * @param message
     * @return
     * @throws MessageProtocolException 
     */
    public abstract Message execute(Message message) throws ConversationException;
    
    /**
     * 
     * @return 
     */
    public abstract Message waitTimeoutMessage() throws ConversationException;
}
