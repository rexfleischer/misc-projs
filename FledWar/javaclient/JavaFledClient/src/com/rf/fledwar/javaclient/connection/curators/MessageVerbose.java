/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.curators;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.MessageCurator;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class MessageVerbose implements MessageCurator
{
    private static final Logger logger = Logger.getLogger(MessageVerbose.class);
    
    private String format;
    
    public MessageVerbose()
    {
        this(null);
    }
    
    public MessageVerbose(String format)
    {
        this.format = format;
    }

    @Override
    public Message curate(ConnectionLiaison liaison, Message message)
    {
        if (logger.isDebugEnabled())
        {
            String debug;
            if (format == null)
            {
                debug = message.toString();
            }
            else
            {
                debug = String.format(format, message);
            }
            logger.debug(debug);
        }
        return message;
    }
    
}
