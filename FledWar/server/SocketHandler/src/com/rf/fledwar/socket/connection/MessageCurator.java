/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import com.rf.fledwar.socket.Message;

/**
 *
 * @author REx
 */
public interface MessageCurator
{
    public Message curate(ConnectionLiaison liaison, Message message);
}
