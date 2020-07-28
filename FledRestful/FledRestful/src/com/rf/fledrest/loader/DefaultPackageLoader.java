/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.loader;

import java.io.File;
import java.util.LinkedList;
import javax.servlet.ServletContext;

/**
 *
 * @author REx
 */
public class DefaultPackageLoader extends FledRestClassLoader
{
    String rawList;
    
    String[] packages;
    
    String[] classes;

    @Override
    public void init(ServletContext context) throws FledRestClassLoaderException
    {
        rawList = context.getInitParameter("fledrest.packageloader.packagelist");
        if (rawList == null || rawList.isEmpty())
        {
            throw new FledRestClassLoaderException(
                    "fledrest.packageloader.packagelist must be specified in web.xml");
        }
        if (rawList.contains(","))
        {
            packages = rawList.split(",");
        }
        else
        {
            packages = new String[]{rawList};
        }
    }

    @Override
    public String[] load() throws FledRestClassLoaderException
    {
        if (classes != null)
        {
            return classes;
        }
        LinkedList<String> classesBuffer = new LinkedList<String>();
        
        for(String _package : packages)
        {
            String conversion = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(_package.replace('.', '/'))
                    .getFile();
            if (conversion == null)
            {
                throw new FledRestClassLoaderException(
                        String.format("%s was not able to be found", _package));
            }
            
            File directory = new File(conversion);
            if (!directory.isDirectory())
            {
                throw new FledRestClassLoaderException(
                        String.format("%s is not a package name", _package));
            }
            
            File[] files = directory.listFiles();
            for(File file : files)
            {
                String path = file.getPath().replace("\\", "/");
                if (path.endsWith(".class") && !path.contains("$"))
                {
                    classesBuffer.add(String.format(
                            "%s.%s", 
                            _package, 
                            path.substring(path.lastIndexOf("/") + 1, path.length() - 6)));
                }
            }
        }
        
        classes = new String[classesBuffer.size()];
        classes = classesBuffer.toArray(classes);
        return classes;
    }
    
}
