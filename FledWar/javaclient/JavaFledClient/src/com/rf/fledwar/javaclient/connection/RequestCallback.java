/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

import com.rf.fledwar.socket.Message;

/**
 *
 * @author REx
 */
public interface RequestCallback
{
    public void onError(Message error);
    
    public void callback(Object requested);
}
