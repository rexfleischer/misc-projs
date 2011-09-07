/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class LoadImagesByDirectory<_Fy>
{
    protected abstract _Fy load(File file);
    
    public Map<String, _Fy> loadDirectory(String dirRoot)
    {
        return recursiveLoadDirectory(dirRoot, "");
    }
    
    private Map<String, _Fy> recursiveLoadDirectory(String dirRoot, String keyAt)
    {
        File dir = new File(dirRoot);
        if (!dir.isDirectory())
        {
            throw new IllegalArgumentException(
                    "dirRoot must be a directory");
        }
        
        Map<String, _Fy> result = new HashMap<>();
        
        File[] files = dir.listFiles();
        for(File file : files)
        {
            if (file.isDirectory())
            {
                result.putAll(recursiveLoadDirectory(
                        file.getAbsolutePath(), 
                        keyAt.equals("") ? 
                                file.getName() : 
                                keyAt + "." + basename(file)));
            }
            else if (file.isFile())
            {
                _Fy loadedFile = load(file);
                if (loadedFile != null)
                {
                    if (keyAt.equals(""))
                    {
                        result.put(basename(file), loadedFile);
                    }
                    else
                    {
                        result.put(keyAt + "." + basename(file), loadedFile);
                    }
                }
            }
        }
        
        return result;
    }
    
    private String basename(File file)
    {
        String search = file.getName();
        int pos = search.lastIndexOf(".");
        if (pos == -1)
        {
            return search;
        }
        return search.substring(0, pos);
    }
}
