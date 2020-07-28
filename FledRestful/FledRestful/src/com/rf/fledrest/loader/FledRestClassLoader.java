/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContext;

/**
 *
 * @author REx
 */
public abstract class FledRestClassLoader
{
    public static FledRestClassLoader getLoader(ServletContext context)
            throws  ClassNotFoundException, 
                    NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    IllegalArgumentException, 
                    InvocationTargetException,
                    FledRestClassLoaderException
    {
        String loader = context.getInitParameter("fledrest.loader");
        if (loader == null || loader.isEmpty())
        {
            loader = "com.rf.fledrest.loader.DefaultClassLoader";
        }
        Class clazz = Class.forName(loader);
        Constructor constructor = clazz.getConstructor();
        Object instance = constructor.newInstance();
        if (!(instance instanceof FledRestClassLoader))
        {
            throw new IllegalArgumentException(
                    String.format("%s must extend FledRestClassLoader", loader));
        }
        FledRestClassLoader classLoader = (FledRestClassLoader)instance;
        classLoader.init(context);
        return classLoader;
    }
    
    public abstract void init(ServletContext context)
            throws FledRestClassLoaderException;
    
    public abstract String[] load()
            throws FledRestClassLoaderException;
}
