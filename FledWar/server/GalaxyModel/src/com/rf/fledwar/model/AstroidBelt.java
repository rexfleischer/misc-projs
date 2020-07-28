/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import java.util.Map;

/**
 *
 * @author REx
 */
public class AstroidBelt extends Orbital
{
    /**
     * the type of astroid belt. this helps determine the types
     * of materials that can be harvested from it.
     */
    private static final String ASTROID_BELT_TYPE = "astroidbelttype";

    public AstroidBelt()
    {
        super(OrbitalType.ASTROID_BELT);
    }

    public AstroidBelt(Map data)
    {
        super(data);
    }

    public AstroidBeltType getAstroidBeltType()
    {
        String type = (String) this.get(ASTROID_BELT_TYPE);
        return AstroidBeltType.valueOf(type);
    }

    public void setAstroidBeltType(AstroidBeltType type)
    {
        this.put(ASTROID_BELT_TYPE, type.name());
    }
    
}
