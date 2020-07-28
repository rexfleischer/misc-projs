/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

/**
 *
 * @author REx
 */
public interface Message
{
    /**
     * gets the message that was sent
     * @return 
     */
    public byte[] getMessage();
    
    /**
     * the queue this message belongs in.
     * @return 
     */
    public String getQueue();
    
    /**
     * the time in seconds when this message got sent
     * @return 
     */
    public int getTimeSent();
    
    /**
     * the time in seconds this message was *supposed* to be
     * delivered. this is not the actual time it was delivered.
     * @return 
     */
    public int getDeliveryTime();
    
    /**
     * the time this message was actually delivered.
     * @return 
     */
    public int getRecieveTime();
    
    /**
     * means the message was successfully consumed and
     * can be deleted from memory and disk.
     */
    public void finished();
    
    /**
     * means the message was not successfully consumed
     * and needs to be put back into the queue.
     */
    public void reput();
}
