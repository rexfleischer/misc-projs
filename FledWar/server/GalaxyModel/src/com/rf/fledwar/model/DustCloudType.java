/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.rf.fledwar.model.util.ChanceRole;

/**
 * dust clouds have a few features. first, ships and such can hid in 
 * some types of them. and dust clouds can be harvested and sometimes created. 
 * @author REx
 */
public enum DustCloudType
{
    TYPE1(30),
    
    TYPE2(30),
    
    TYPE3(30);
    
    private int _chance;
    
    private DustCloudType(int chance)
    {
        this._chance = chance;
    }
    
    public static DustCloudType roleType()
    {
        return (DustCloudType) chance.role();
    }
    
    private final static ChanceRole chance = getChanceRole();
    
    private static ChanceRole getChanceRole()
    {
        ChanceRole result = new ChanceRole();
        for(DustCloudType type : values())
        {
            result.set(type._chance, type);
        }
        return result;
    }
}
