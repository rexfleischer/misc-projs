/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

/**
 *
 * @author REx
 */
public interface MessageQueue
{
    public void send(byte[] message)
            throws MessagingException;
    
    public void send(byte[] message, int sdelay)
            throws MessagingException;
    
    public Message receive(int mswait)
            throws MessagingException;
    
    public long getMessageCount()
            throws MessagingException;
}
