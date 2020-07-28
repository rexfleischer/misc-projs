/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.server;

import com.rf.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author REx
 */
public interface ConnectionHandlerProvider 
{
    /**
     * a simple interface that abstracts the actual work away from
     * getting connections.
     * @param socket
     * @return
     * @throws IOException 
     */
    public SafeYieldingThread provide(Socket socket) 
            throws IOException;
}
