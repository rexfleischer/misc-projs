/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

/**
 *
 * @author REx
 */
public enum ConversationState
{
    /**
     * meaning the protocol is completely finished
     * and the connection handler can move on from
     * this protocol.
     */
    FINISHED,
    
    /**
     * meaning the protocol cannot finish and is
     * in some sort of unknown state. the connection
     * handler should probably ping the client or
     * close the connection
     */
    ERROR,
    
    /**
     * the protocol is waiting for some response from
     * the client and cannot finish the protocol
     * without some waiting.
     */
    WAITING,
    
    /**
     * the protocol has not started.
     */
    NEW,
    
    /**
     * signals that the connection should be closed
     */
    CLOSE_CONNECTION
}
