/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.services;

import com.rf.fledwar.model.util.JsonHelper;
import com.rf.fledwar.server.FledWarServer;
import com.rf.fledwar.socket.server.ServerSocketListener;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author REx
 */
@Path("/info")
@Produces(MediaType.TEXT_PLAIN)
public class InfoServices
{
    @GET
    @Path("connection_port")
    public String getConnectionPort()
    {
        ServerSocketListener listener = FledWarServer.getServerSocketListener();
        Map result = new HashMap<>();
        
        if (listener == null)
        {
            result.put("error", "game server not turned on");
        }
        else
        {
            result.put("port", listener.getServerSocket().getLocalPort());
        }
        
        return JsonHelper.toPrettyJson(result);
    }
}
