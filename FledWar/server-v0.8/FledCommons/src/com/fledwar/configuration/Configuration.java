/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.configuration;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rfleischer
 */
public class Configuration
{

  public static final String DEFAULT_CLASS_KEY = "clazz";
  
  public static final String INCLUDES = "INCLUDES";
  
  public static final String REGEX_PATH_SPLIT = "\\.";
  
  Object root;

  public Configuration()
  {
    root = new ConfigObject();
  }

  private Configuration(Object root)
  {
    if (root instanceof Configuration) {
      throw new ConfigurationException(
              "cannot place config object at root");
    }
    this.root = root;
  }

  public void loadResource(String path) throws IOException
  {
    URL resource = Configuration.class.getResource(path);
    if (resource == null) {
      throw new FileNotFoundException(
              String.format("%s could not be found", path));
    }
    String file = resource.getFile();
    loadFile(file);
  }

  public void loadFile(String path) throws IOException
  {
    if (!(root instanceof ConfigObject)) {
      throw new ConfigurationException(
              "cannot load file on none root config object");
    }
    ConfigObject configroot = (ConfigObject) root;
    File check = new File(path);
    if (!check.isFile()) {
      throw new FileNotFoundException(
              String.format("%s is not a file", path));
    }
    URL url = readyURL(check);

    ConfigSlurper configsluper = new ConfigSlurper();
    configsluper.setBinding(configroot);

    ConfigObject including = configsluper.parse(url);

    if (including.containsKey(INCLUDES)
        && (including.get(INCLUDES) instanceof List)) {
      String rootdir = check.getParent();
      List includes = (List) including.get(INCLUDES);
      Iterator it = includes.iterator();
      while(it.hasNext()) {
        String include = it.next().toString();
        File thisinclude = new File(rootdir + include);
        if (thisinclude.isFile()) {
          URL includeURL = readyURL(thisinclude);

          configsluper.setBinding(including);
          ConfigObject subinclude = configsluper.parse(includeURL);
          including.merge(subinclude);
        }
      }
    }

    configroot.merge(including);
  }

  private URL readyURL(File file) throws MalformedURLException
  {
    String path = file.getAbsolutePath();
    path = path.replace("\\", "/");
    if (path.startsWith("/")) {
      path = String.format("file:%s", path);
    }
    else {
      path = String.format("file:/%s", path);
    }
    return URI.create(path).toURL();
  }

  public Object getRootObject()
  {
    return root;
  }

  public String getKeyAsString(Object key)
  {
    Object check = smallGet(root, key.toString());
    return (check != null) ? check.toString() : null;
  }

  public Integer getKeyAsInteger(Object key)
  {
    Object check = smallGet(root, key.toString());
    return (check != null) ? ((Number) check).intValue() : null;
  }

  public Double getKeyAsDouble(Object key)
  {
    Object check = smallGet(root, key.toString());
    return (check != null) ? ((Number) check).doubleValue() : null;
  }

  public Boolean getKeyAsBoolean(Object key)
  {
    return (Boolean) smallGet(root, key.toString());
  }

  public List getKeyAsList(Object key)
  {
    return (List) smallGet(root, key.toString());
  }

  public Map getKeyAsMap(Object key)
  {
    return (Map) smallGet(root, key.toString());
  }

  public Class getKeyAsClass(Object key)
  {
    return (Class) smallGet(root, key.toString());
  }

  public Configuration getKeyAsConfiguration(Object key)
  {
    Object value = smallGet(root, key.toString());
    return (value != null) ? new Configuration(value) : null;
  }

  public String getAsString(Object key)
  {
    Object check = get(key);
    return (check != null) ? check.toString() : null;
  }

  public Integer getAsInteger(Object key)
  {
    Object check = get(key);
    return (check != null) ? ((Number) check).intValue() : null;
  }

  public Double getAsDouble(Object key)
  {
    Object check = get(key);
    return (check != null) ? ((Number) check).doubleValue() : null;
  }

  public Boolean getAsBoolean(Object key)
  {
    return (Boolean) get(key);
  }

  public List getAsList(Object key)
  {
    return (List) get(key);
  }

  public Map getAsMap(Object key)
  {
    return (Map) get(key);
  }

  public Class getAsClass(Object key)
  {
    return (Class) get(key);
  }

  public Configuration getAsConfiguration(Object key)
  {
    Object value = get(key);
    return (value != null) ? new Configuration(value) : null;
  }

  public <T> T getAsInstance(Class<T> type, Object key)
          throws Exception
  {
    return (T) getAsClass(key).getConstructor().newInstance();
  }

  public <T> T getAsInstance(Class<T> type)
          throws Exception
  {
    return getAsInstance(type, DEFAULT_CLASS_KEY);
  }

  public int size()
  {
    if (root instanceof Map) {
      return ((Map) root).size();
    }
    else if (root instanceof List) {
      return ((List) root).size();
    }
    else {
      throw new ConfigurationException(
              String.format("unknown root type [%s]", root.getClass()));
    }
  }

  public boolean isEmpty()
  {
    if (root instanceof Map) {
      return ((Map) root).isEmpty();
    }
    else if (root instanceof List) {
      return ((List) root).isEmpty();
    }
    else {
      throw new ConfigurationException(
              String.format("unknown root type [%s]", root.getClass()));
    }
  }

  public boolean containsKey(Object key)
  {
    return get(key) != null;
  }

  public Object get(Object key)
  {
    String[] keys = readyKey(key);

    Object current = getJustBefore(keys);

    return smallGet(current, keys[keys.length - 1]);
  }

  public Set keySet()
  {
    if (root instanceof Map) {
      return ((Map) root).keySet();
    }
    else if (root instanceof List) {
      int size = ((List) root).size();
      Set newset = new LinkedHashSet();
      for(int i = 0; i < size; i++) {
        newset.add(i);
      }
      return newset;
    }
    return null;
  }

  private Object getJustBefore(String[] keys)
  {
    Object current = root;

    for(int i = 0; i < keys.length - 1; i++) {
      current = smallGet(current, keys[i]);
      if (current == null) {
        break;
      }
    }

    return current;
  }

  private Object smallGet(Object current, String key)
  {
    Object next = null;
    if (current instanceof Map) {
      next = ((Map) current).get(key);
    }
    else if (current instanceof List) {
      Integer intkey;
      try {
        intkey = Integer.parseInt(key);
      }
      catch(NumberFormatException ex) {
        throw new ConfigurationException(
                String.format("cannot parse key as integer when "
                              + "config context is List [%s]",
                              key));
      }
      next = ((List) current).get(intkey);
    }
    return next;
  }

  private String[] readyKey(Object key)
  {
    String hashkey = key.toString();
    String[] keys = hashkey.contains(".")
            ? hashkey.split(REGEX_PATH_SPLIT)
            : new String[]{hashkey};
    return keys;
  }

  @Override
  public String toString()
  {
    return root.toString();
  }
}
