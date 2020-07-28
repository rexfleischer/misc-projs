/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;

/**
 *
 * @author REx
 */
public interface FocusQuery
{
    public static final String VIEW_LEVEL = "view_level";
    
    /**
     * return a string if an error occurs
     * @param message
     * @return 
     */
    public String init(ConnectionLiaison liaison, Message message) throws Exception;
    
    /**
     * 
     * @param message
     * @return
     * @throws Exception 
     */
    public String updateFocus(Message message) throws Exception;
    
    /**
     * check the status
     * @return 
     */
    public Object check() throws Exception;
    
}
