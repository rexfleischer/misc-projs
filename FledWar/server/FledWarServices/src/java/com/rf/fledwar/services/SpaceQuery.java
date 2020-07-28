/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.services;

import com.mongodb.DBCursor;
import com.rf.fledwar.engine.dao.BasicDAOException;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.JsonHelper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author REx
 */
@Path("/query")
@Produces(MediaType.TEXT_PLAIN)
public class SpaceQuery
{
    @GET
    @Path("list")
    public String listAllGalaxySystems()
    {
        Map result = new LinkedHashMap();
        
        try
        {
            GalaxySystemDAO dao = new GalaxySystemDAO();
            
            DBCursor cursor = dao.getCollection().find();
            ArrayList<String> list = new ArrayList<>(cursor.count());
            while(cursor.hasNext())
            {
                GalaxySystem system = new GalaxySystem((Map) cursor.next());
                list.add(system.getName());
            }
            
            result.put("names", list);
            result.put("result", "successful");
        }
        catch(BasicDAOException ex)
        {
            result.put("result", "unsuccessful");
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            result.put("error", out.toString());
        }
        
        return JsonHelper.toPrettyJson(result);
    }
    
    @GET
    @Path("byname/{name}")
    public String queryByName(@PathParam("name") final String name)
    {
        try
        {
            GalaxySystem system = (new GalaxySystemDAO()).findByName(name);
            if (system == null)
            {
                return "not found";
            }
            return JsonHelper.toPrettyJson(system.getDBObject());
        }
        catch(BasicDAOException ex)
        {
            Map result = new LinkedHashMap();
            result.put("result", "unsuccessful");
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            result.put("error", out.toString());
            return JsonHelper.toPrettyJson(result);
        }
    }
}
