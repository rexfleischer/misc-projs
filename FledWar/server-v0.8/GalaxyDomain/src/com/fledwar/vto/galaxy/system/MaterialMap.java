/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.system;

import com.fledwar.vto.BasicVTO;
import java.util.Map;

/**
 *
 * @author REx
 */
public class MaterialMap extends BasicVTO
{

    public MaterialMap(Map data)
    {
        super(data);
    }
    
    public void setMaterialAmount(String type, byte percent)
    {
        put(type, percent);
    }
    
    public byte getMaterialAmount(String type)
    {
        return (byte) get(type);
    }
    
}
