/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.interfaces;

/**
 *
 * @author REx
 */
public interface Configurable<_Ky>
{
    public String configContext();
    
    public void configSetInteger(_Ky key, Integer value);
    
    public void configSetString(_Ky key, String value);
    
    public void configSetBoolean(_Ky key, Boolean value);
    
    public void configRemoveInteger(_Ky key);
    
    public void configRemoveString(_Ky key);
    
    public void configRemoveBoolean(_Ky key);
    
    public boolean configBooleanExists(_Ky key);
    
    public boolean configIntegerExists(_Ky key);
    
    public boolean configStringExists(_Ky key);
    
    public Integer configGetInteger(_Ky key);
    
    public String configGetString(_Ky key);
    
    public Boolean configGetBoolean(_Ky key);
}
