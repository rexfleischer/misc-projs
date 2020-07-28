/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.config;

import java.util.Properties;

/**
 *
 * @author REx
 */
public class EaterProperties extends Properties
{
    public String[] getList(String key)
    {
        String list = getProperty(key);
        if (list == null)
        {
            return null;
        }
        if (!list.contains(","))
        {
            return new String[]{list};
        }
        String[] split = list.split(",");
        for(int i = 0; i < split.length; i++)
        {
            split[i] = split[i].trim();
        }
        return split;
    }
}
