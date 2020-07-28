/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.loader;

import javax.servlet.ServletContext;

/**
 *
 * @author REx
 */
public class DefaultClassLoader extends FledRestClassLoader
{
    String rawList;
    
    String[] classes;

    @Override
    public void init(ServletContext context)
            throws FledRestClassLoaderException
    {
        rawList = context.getInitParameter("fledrest.defaultloader.classlist");
        if (rawList == null || rawList.isEmpty())
        {
            throw new FledRestClassLoaderException(
                    "fledrest.deafultloader.classlist must be specified in web.xml");
        }
        if (rawList.contains(","))
        {
            classes = rawList.split(",");
        }
        else
        {
            classes = new String[]{rawList};
        }
        
        for(int i = 0; i < classes.length; i++)
        {
            classes[i] = classes[i].trim();
        }
    }

    @Override
    public String[] load()
            throws FledRestClassLoaderException
    {
        return classes;
    }
}
