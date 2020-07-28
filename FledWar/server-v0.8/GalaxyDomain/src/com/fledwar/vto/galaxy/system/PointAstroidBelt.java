/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.system;

import java.util.Map;

/**
 *
 * @author REx
 */
public class PointAstroidBelt extends SystemPoint
{

    public PointAstroidBelt()
    {
        super(SystemPointType.ASTROID_BELT);
    }

    public PointAstroidBelt(Map data)
    {
        super(data);
    }
    
    public byte[] getHealth()
    {
        byte[] result = (byte[]) get(HEALTH);
        if (result == null)
        {
            result = new byte[36];
            for(int i = 0; i < result.length; i++)
            {
                result[i] = 100;
            }
            put(HEALTH, result);
        }
        return result;
    }
    
    public MaterialMap getMaterialMap()
    {
        return new MaterialMap(ensuredMapGet(MATERIAL));
    }
    
}
