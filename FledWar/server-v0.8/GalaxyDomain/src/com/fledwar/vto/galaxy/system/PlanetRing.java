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
public class PlanetRing extends BasicVTO
{
    public PlanetRing()
    {
    }
    
    public PlanetRing(Map data)
    {
        super(data);
    }
    
    public Double getInnerDistance()
    {
        return getAsDouble(SystemPoint.DIST_INNER);
    }
    
    public Double getOuterDistance()
    {
        return getAsDouble(SystemPoint.DIST_OUTER);
    }
    
    public void setInnerDistance(double innerdist)
    {
        put(SystemPoint.DIST_INNER, innerdist);
    }
    
    public void setOuterDistance(double outerdist)
    {
        put(SystemPoint.DIST_OUTER, outerdist);
    }
}
