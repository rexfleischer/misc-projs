/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;

/**
 *
 * @author REx
 */
interface Query
{
    Object exec(ConnectionLiaison liaison, Message message) throws Exception;
}
