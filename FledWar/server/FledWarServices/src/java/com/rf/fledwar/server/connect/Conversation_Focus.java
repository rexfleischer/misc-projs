/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect;

import com.rf.fledwar.server.connect.focus.FocusQueries;
import com.rf.fledwar.server.connect.focus.FocusQuery;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationState;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class Conversation_Focus extends Conversation
{
    private static final Logger logger = Logger.getLogger(Conversation_Focus.class);
    
    public static final String CONVERSATION_NAME = "focus";
    
    public static final String FOCUS_END = "focusend";
    
    public static final String FOCUS_NAME = "focusname";
    
    public static final String FOCUS_TYPE = "focustype";
    
    public static final String FOCUS_UPDATE = "update";
    
    public static final int TIMEOUT = 1000;
    
    private Map<String, FocusQuery> focuses;

    public Conversation_Focus()
    {
        super(CONVERSATION_NAME);
        this.setTimeout(TIMEOUT);
        setState(ConversationState.WAITING);
        focuses = new HashMap<>();
    }

    @Override
    public Message getInitialMessage() throws ConversationException
    {
        throw new UnsupportedOperationException(
                "server cannot start this conversation");
    }

    @Override
    public Message execute(Message message) throws ConversationException
    {
        String focusname = (String) message.getValue(FOCUS_NAME);
        if (focusname == null || focusname.isEmpty())
        {
            Message response = getEmptyMessage();
            response.putValue("error", "'focusname' must be specified");
            return response;
        }
        
        Boolean endcheck = (Boolean) message.getValue(FOCUS_END);
        if (endcheck != null && endcheck)
        {
            focuses.remove(focusname);
            return null;
        }
        
        // if there is one already with this name, then
        // just update it. if there is an error, then
        // we will stop using that focus.
        String error;
        try
        {
            if (focuses.containsKey(focusname))
            {
                FocusQuery view = focuses.get(focusname);
                error = view.updateFocus(message);
            }
            else
            {
                FocusQuery query = FocusQueries.valueOf(
                        message.getValue(FOCUS_TYPE).toString().toUpperCase())
                        .getFocusQuery();
                error = query.init(data, message);
                focuses.put(focusname, query);
            }
        }
        catch(Exception ex)
        {
            error = ex.getMessage();
            logger.error("could not update or init focus", ex);
        }
        
        if (error != null)
        {
            Message response = getEmptyMessage();
            response.putValue(FOCUS_NAME, focusname);
            response.putValue(FOCUS_END, true);
            response.putValue("error", error);
            return response;
        }
        
        waitTimeoutMessage();
        return null;
    }

    @Override
    public Message waitTimeoutMessage() throws ConversationException
    {
        Iterator<String> it = focuses.keySet().iterator();
        while(it.hasNext())
        {
            String focusname = it.next();
            FocusQuery focusquery = focuses.get(focusname);
            try
            {
                Object update = focusquery.check();
                if (update != null)
                {
                    Message focusupdate = getEmptyMessage();
                    focusupdate.putValue(FOCUS_NAME, focusname);
                    focusupdate.putValue(FOCUS_UPDATE, update);
                    data.write(focusupdate);
                }
            }
            catch(Exception ex)
            {
                throw new ConversationException(ex);
            }
        }
        
        this.triggerLastResponse();
        return null;
    }
    
}
