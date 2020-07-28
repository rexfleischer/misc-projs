/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

import java.util.Map;
import java.util.Properties;

/**
 *
 * @author REx
 */
interface MessageQueueFactory
{
    void init(Properties properties, 
              String name, 
              Map<String, Object> hints) 
            throws MessagingException;
    
    MessageQueue factory(String queue) 
            throws MessagingException;

    void destroy() throws MessagingException;

    void finished() throws MessagingException;
}
