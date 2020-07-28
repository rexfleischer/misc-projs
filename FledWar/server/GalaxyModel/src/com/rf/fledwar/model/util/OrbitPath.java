/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.util;

import java.util.Arrays;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class OrbitPath
{
    private static final String SPLITTER = ".";
    
    private static final String REGEX_SPLIT = "\\.";
    
    /**
     * this means that whatever system it points, it 
     * orbits the default (or very center) of the system.
     */
    public static final String PATH_CENTER = "center";
    
    private String[] splitpath;
    
    public OrbitPath(String path)
    {
        Objects.requireNonNull(path, "path");
        splitpath = path.split(REGEX_SPLIT);
    }
    
    public OrbitPath(int size)
    {
        if (size < 1)
        {
            throw new IllegalArgumentException(
                    String.format("size cannot be less than one [%s]", size));
        }
        splitpath = new String[size + 1];
    }
    
    public void setSystem(ObjectId id)
    {
        if (id == null)
        {
            splitpath[0] = "";
        }
        else
        {
            splitpath[0] = id.toString();
        }
    }
    
    public ObjectId getSystem()
    {
        return (!splitpath[0].isEmpty()) 
                ? new ObjectId(splitpath[0]) 
                : null;
    }
    
    public int getPathSize()
    {
        return splitpath.length - 1;
    }
    
    public String getPathPart(int index)
    {
        return splitpath[index + 1];
    }
    
    public void setPathPart(int index, String path)
    {
        splitpath[index + 1] = path;
    }
    
    public String toFullPath()
    {
        StringBuilder string = new StringBuilder(splitpath[0]);
        for(int i = 1; i < splitpath.length; i++)
        {
            string.append(SPLITTER);
            if (splitpath[i] == null)
            {
                throw new NullPointerException(
                        String.format("not all path parts were set %s", 
                                      Arrays.toString(splitpath)));
            }
            string.append(splitpath[i]);
        }
        return string.toString();
    }
    
    public boolean isOrbitingCenter()
    {
        return (splitpath[0] != null) && (splitpath[0].equals(PATH_CENTER));
    }
}
