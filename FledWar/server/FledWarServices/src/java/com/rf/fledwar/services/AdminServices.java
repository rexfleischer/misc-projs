/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.services;

import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.util.JsonHelper;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.server.FledWarServer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author REx
 */
@Path("/admin")
@Produces(MediaType.TEXT_PLAIN)
public class AdminServices
{
    @GET
    @Path("start")
    public String startServer()
    {
        Map result = new LinkedHashMap();
        
        try
        {
            FledWarServer.start();
            result.put("result", "successful");
        }
        catch(Exception ex)
        {
            result.put("result", "unsuccessful");
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            result.put("error", out.toString());
        }
        
        return JsonHelper.toPrettyJson(result);
    }
    
    @GET
    @Path("stop")
    public String stopServer()
    {
        Map result = new LinkedHashMap();
        
        try
        {
            FledWarServer.shutdown();
            result.put("result", "successful");
        }
        catch(Exception ex)
        {
            result.put("result", "unsuccessful");
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            result.put("error", out.toString());
        }
        
        return JsonHelper.toPrettyJson(result);
    }
    
    @GET
    @Path("config")
    public String getConfig()
    {
        Properties properties = FledWarServer.getProperties();
        if (properties == null)
        {
            return "no properties";
        }
        
        StringBuilder result = new StringBuilder();
        Iterator it = properties.keySet().iterator();
        while(it.hasNext())
        {
            Object key = it.next();
            Object value = properties.get(key);
            result.append(String.format("%s: %s\n", key, value));
        }
        return result.toString();
    }
    
    @GET
    @Path("usage")
    public String getUsage()
    {   
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("free memory: ").append(format.format(freeMemory / 1024)).append("\n");
        sb.append("allocated memory: ").append(format.format(allocatedMemory / 1024)).append("\n");
        sb.append("max memory: ").append(format.format(maxMemory / 1024)).append("\n");
        sb.append("total free memory: ").append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)).append("\n");
        
        return sb.toString();
    }
    
    @GET
    @Path("cluster")
    public String createCluster()
    {
        Map result = new LinkedHashMap();
        
        try
        {
            
            MongoConnect.getSpaceDB().dropDatabase();
            
            /**
             * basically only good for testing
             */
            CreateRandomSystemCluster clustergenerator = new CreateRandomSystemCluster();
            clustergenerator.setClusterName("cluster");
            clustergenerator.setClusterOrbitStatus(new OrbitStatus(
                    SpaceConstents.LIGHTYEAR * 10000,
                    0.0, 
                    SpaceConstents.GALAXY_ANGULAR_SPEED, 
                    new OrbitPath(".center")));
            clustergenerator.exec();
            
            GalaxySystemDAO.ensureAllIndexes();
            
            result.put("result", "successful");
        }
        catch(Exception ex)
        {
            result.put("result", "unsuccessful");
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            result.put("error", out.toString());
        }
        
        return JsonHelper.toPrettyJson(result);
    }
    
    @GET
    @Path("connections")
    public String getServerConnections()
    {
        return FledWarServer.getServerSocketListener().getConnections();
    }
}
