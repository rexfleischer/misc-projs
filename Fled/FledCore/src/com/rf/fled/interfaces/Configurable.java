/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.interfaces;

/**
 *
 * @author REx
 */
public interface Configurable
{
    public String configContext();
    
    public void configSetInteger(String key, Integer value);
    
    public void configSetString(String key, String value);
    
    public void configSetBoolean(String key, Boolean value);
    
    public void configRemoveInteger(String key);
    
    public void configRemoveString(String key);
    
    public void configRemoveBoolean(String key);
    
    public boolean configBooleanExists(String key);
    
    public boolean configIntegerExists(String key);
    
    public boolean configStringExists(String key);
    
    public Integer configGetInteger(String key);
    
    public String configGetString(String key);
    
    public Boolean configGetBoolean(String key);
}
