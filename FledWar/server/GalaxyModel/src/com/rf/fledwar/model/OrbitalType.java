/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.mongodb.DBObject;
import java.util.Map;

/**
 *
 * @author REx
 */
public enum OrbitalType
{
    ASTROID_BELT()
    {
        @Override
        public Orbital getNew()
        {
            return new AstroidBelt();
        }

        @Override
        public Orbital getNew(Map object)
        {
            return new AstroidBelt(object);
        }
    },
    
    DUST_CLOUD()
    {
        @Override
        public Orbital getNew()
        {
            return new DustCloud();
        }

        @Override
        public Orbital getNew(Map object)
        {
            return new DustCloud(object);
        }
    },
    
    PLANET()
    {
        @Override
        public Orbital getNew()
        {
            return new Planet();
        }

        @Override
        public Orbital getNew(Map object)
        {
            return new Planet(object);
        }
    },
    
    PLANET_MOON()
    {
        @Override
        public Orbital getNew()
        {
            return new PlanetMoon();
        }

        @Override
        public Orbital getNew(Map object)
        {
            return new PlanetMoon(object);
        }
    },
    
    STAR()
    {
        @Override
        public Orbital getNew()
        {
            return new Star();
        }

        @Override
        public Orbital getNew(Map object)
        {
            return new Star(object);
        }
    };
    
    public abstract Orbital getNew();
    
    public abstract Orbital getNew(Map object);
}
