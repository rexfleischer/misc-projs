/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class JsonHelper
{
  private static final String ID = "_id";
  
  private static final Gson prettyGson;
  
  private static final Gson gson;
  
  private static final JsonParser parser;

  static {
    prettyGson = (new GsonBuilder())
            .registerTypeAdapter(BasicDBObject.class, new BasicDBObjectAdapter())
            .registerTypeAdapter(BasicDBList.class, new BasicDBListAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .registerTypeAdapter(Class.class, new ClassAdapter())
            .setPrettyPrinting()
            .create();
    gson = (new GsonBuilder())
            .registerTypeAdapter(BasicDBObject.class, new BasicDBObjectAdapter())
            .registerTypeAdapter(BasicDBList.class, new BasicDBListAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .registerTypeAdapter(Class.class, new ClassAdapter())
            .create();
    parser = new JsonParser();
  }

  private static class BasicDBObjectAdapter implements JsonSerializer<BasicDBObject>
  {

    @Override
    public JsonElement serialize(BasicDBObject src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {
      JsonObject result = new JsonObject();
      Iterator<Entry<String, Object>> it = src.entrySet().iterator();
      while(it.hasNext()) {
        Entry<String, Object> entry = it.next();
        String key = entry.getKey();
        Object value = entry.getValue();

        if (key.equals(ID)) {
          result.add(key, context.serialize(value, ObjectId.class));
        }
        else {
          result.add(key, context.serialize(value));
        }
      }

      return result;
    }
  }

  private static class BasicDBListAdapter implements JsonSerializer<BasicDBList>
  {

    @Override
    public JsonElement serialize(BasicDBList src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {
      JsonArray result = new JsonArray();
      for(int i = 0, n = src.size(); i < n; i++) {
        result.add(context.serialize(src.get(i)));
      }

      return result;
    }
  }

  private static class ClassAdapter implements JsonSerializer<Class>
  {

    @Override
    public JsonElement serialize(Class src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {
      return new JsonPrimitive(src.toString());
    }
  }

  private static class ObjectIdAdapter implements JsonSerializer<ObjectId>,
                                                  JsonDeserializer<ObjectId>
  {

    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {
      return new JsonPrimitive(src.toString());
    }

    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) 
            throws JsonParseException
    {
      return new ObjectId(json.getAsString());
    }
  }

  /**
   * we do this because the static constructor is ran the first time this is
   * referenced, meaning it can mess up some benchmarks
   */
  public static void init()
  {
  }

  public static String toPrettyJson(Object object)
  {
    return prettyGson.toJson(object);
  }

  public static String toJson(Object object)
  {
    return gson.toJson(object);
  }

  public static BasicDBObject toJavaMap(String json)
  {
    return (BasicDBObject) recursiveParse(parser.parse(json));
  }

  public static BasicDBList toJavaList(String json)
  {
    return (BasicDBList) recursiveParse(parser.parse(json));
  }

  private static Object recursiveParse(JsonElement root)
  {
    if (root.isJsonPrimitive()) {
      JsonPrimitive primitive = root.getAsJsonPrimitive();
      if (primitive.isNumber()) {
        return primitive.getAsDouble();
      }
      else if (primitive.isString()) {
        return primitive.getAsString();
      }
      else if (primitive.isBoolean()) {
        return primitive.getAsBoolean();
      }

      throw new RuntimeException("1");
    }
    else if (root.isJsonArray()) {
      BasicDBList result = new BasicDBList();
      JsonArray array = root.getAsJsonArray();

      Iterator<JsonElement> it = array.iterator();
      while(it.hasNext()) {
        result.add(recursiveParse(it.next()));
      }

      return result;
    }
    else if (root.isJsonObject()) {
      BasicDBObject result = new BasicDBObject();
      JsonObject object = root.getAsJsonObject();

      Iterator<Map.Entry<String, JsonElement>> it = object.entrySet().iterator();
      while(it.hasNext()) {
        Map.Entry<String, JsonElement> entry = it.next();
        String key = entry.getKey();

        if (key.equals(ID)) {
          result.put(key, new ObjectId(entry.getValue().getAsString()));
        }
        else {
          result.put(key, recursiveParse(entry.getValue()));
        }
      }

      return result;
    }
    else if (root.isJsonNull()) {
      return null;
    }

    throw new RuntimeException("2");
  }
}
