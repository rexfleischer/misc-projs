/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.mongo;

import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class ObjectIdHelper
{
    public static ObjectId parseObject(Object parsing)
    {
        if (parsing != null)
        {
            if (parsing instanceof Map)
            {
                Map checkmap = (Map) parsing;
                Object timecheck = checkmap.get("_time");
                Object machinecheck = checkmap.get("_machine");
                Object inccheck = checkmap.get("_inc");
                
                if ((timecheck != null && timecheck instanceof Number) &&
                    (machinecheck != null && machinecheck instanceof Number) &&
                    (inccheck != null && inccheck instanceof Number))
                {
                    return new ObjectId(
                            ((Number) timecheck).intValue(), 
                            ((Number) machinecheck).intValue(), 
                            ((Number) inccheck).intValue());
                }
            }
            else if (parsing instanceof String)
            {
                return new ObjectId((String) parsing);
            }
            else if (parsing instanceof ObjectId)
            {
                return (ObjectId) parsing;
            }   
        }
        else
        {
            return null;
        }
        throw new IllegalArgumentException(String.format(
                "could not parse GalaxySystem id [%s]", 
                parsing));
    }
}
