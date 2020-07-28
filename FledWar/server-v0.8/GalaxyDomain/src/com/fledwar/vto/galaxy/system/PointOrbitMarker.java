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
public class PointOrbitMarker extends SystemPoint
{

    public PointOrbitMarker()
    {
        super(SystemPointType.ORBIT_MARKER);
    }

    public PointOrbitMarker(Map data)
    {
        super(data);
    }
    
}
