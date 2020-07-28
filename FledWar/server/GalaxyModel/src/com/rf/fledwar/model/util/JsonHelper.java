/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class JsonHelper
{
    private static final Gson prettyGson;
    
    private static final Gson gson;
    
    static 
    {
        prettyGson = (new GsonBuilder()).setPrettyPrinting().create();
        gson = new Gson();
    }
    
    public static String toPrettyJson(Object object)
    {
        return prettyGson.toJson(object);
    }
    
    public static String toJson(Object object)
    {
        return gson.toJson(object);
    }
    
    public static Map toJavaMap(String json)
    {
        return gson.fromJson(json, Map.class);
    }
    
    public static List toJavaList(String json)
    {
        return gson.fromJson(json, List.class);
    }
}
