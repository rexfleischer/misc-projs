/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.rf.fledwar.model.util.ChanceRole;

/**
 *
 * @author REx
 */
public enum AstroidBeltType
{
    TYPE1(30),
    TYPE2(30),
    TYPE3(30);
    
    private int _chance;
    
    private AstroidBeltType(int chance)
    {
        this._chance = chance;
    }
    
    public static AstroidBeltType roleType()
    {
        return (AstroidBeltType) chance.role();
    }
    
    private final static ChanceRole chance = getChanceRole();
    
    private static ChanceRole getChanceRole()
    {
        ChanceRole result = new ChanceRole();
        for(AstroidBeltType type : values())
        {
            result.set(type._chance, type);
        }
        return result;
    }
}
