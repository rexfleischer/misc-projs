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
public enum SystemPointType
{
    ORBIT_MARKER()
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointOrbitMarker(data);
        }
    },
    STAR
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointStar(data);
        }
    },
    PLANET
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointPlanet(data);
        }
    },
    MOON
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointMoon(data);
        }
    },
    CLOUD
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointCloud(data);
        }
    },
    ASTROID
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointAstroid(data);
        }
    },
    ASTROID_BELT
    {
        @Override
        public SystemPoint factory(Map data)
        {
            return new PointAstroidBelt(data);
        }
    };
    
    public abstract SystemPoint factory(Map data);
    
    public static SystemPoint factorySystemPoint(Map data)
    {
        String type = data.get(SystemPoint.TYPE).toString();
        return valueOf(type).factory(data);
    }
}
