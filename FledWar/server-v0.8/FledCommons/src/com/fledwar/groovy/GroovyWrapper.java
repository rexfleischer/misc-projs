/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GroovyWrapper
{

  private static GroovyScriptEngine engine;
  private static String script_root;

  public static void init(String script_root)
          throws IOException
  {
    GroovyWrapper.script_root = script_root;
    String[] roots = new String[]{script_root};
    engine = new GroovyScriptEngine(roots);
  }

  public static String getScriptRoot()
  {
    return script_root;
  }

  public static void shutdown()
  {
    if (engine != null) {
      engine = null;
    }
  }

  public static Object runScript(String script, Object... key_values)
          throws GroovyWrapperException
  {
    if (key_values != null && (key_values.length % 2 != 0)) {
      throw new GroovyWrapperException("illegal variable count");
    }
    return runScript(script, convertArrayToMap(key_values));
  }

  public static Object runScript(String script, Map<String, Object> variables)
          throws GroovyWrapperException
  {
    Binding binding = new Binding(variables);
    try {
      return engine.run(script, binding);
    }
    catch(Exception ex) {
      throw new GroovyWrapperException(
              String.format("could not run script %s with variables %s",
                            script, variables.toString()),
              ex);
    }
  }

  public static Object eval(String syntax, Object... key_values)
          throws GroovyWrapperException
  {
    if (key_values != null && (key_values.length % 2 != 0)) {
      throw new GroovyWrapperException("illegal variable count");
    }
    return eval(syntax, convertArrayToMap(key_values));
  }

  public static Object eval(String syntax, Map<String, Object> variables)
          throws GroovyWrapperException
  {
    Binding binding = new Binding(variables);
    GroovyShell shell = new GroovyShell(binding);
    try {
      return shell.evaluate(syntax);
    }
    catch(Exception ex) {
      throw new GroovyWrapperException(
              String.format("could not eval string [%s] with variables %s",
                            syntax, variables),
              ex);
    }
  }

  public static Map<String, Object> convertArrayToMap(Object[] array)
  {
    Map<String, Object> variables = new HashMap<>();
    if (array != null) {
      for(int i = 0; i < array.length; i += 2) {
        variables.put(array[i].toString(), array[i + 1]);
      }
    }
    return variables;
  }
}
