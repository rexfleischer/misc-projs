/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

import com.rf.socket.SocketHandler;
import java.util.List;
import java.util.Map;

/**
 * command 
 * @author REx
 */
public abstract class CommandHandler
{
    public final String name;
    
    public CommandHandler(String name)
    {
        this.name = name;
    }
    
    /**
     * this will get called until execute returns false 
     * or an exception is thrown.
     * @param cluster
     * @param socket
     * @throws CommandHandlerException 
     */
    public abstract boolean execute(List<ServerCluster.ServerInfo> cluster, 
                                    SocketHandler socket,
                                    Message message,
                                    Map<String, Object> state)
            throws CommandHandlerException;
}
