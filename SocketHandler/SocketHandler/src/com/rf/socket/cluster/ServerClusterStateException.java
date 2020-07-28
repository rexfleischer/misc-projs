/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

/**
 *
 * @author REx
 */
public class ServerClusterStateException extends Exception
{

    public ServerClusterStateException(Throwable cause)
    {
        super(cause);
    }

    public ServerClusterStateException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ServerClusterStateException(String message)
    {
        super(message);
    }

    public ServerClusterStateException()
    {
    }
    
}
