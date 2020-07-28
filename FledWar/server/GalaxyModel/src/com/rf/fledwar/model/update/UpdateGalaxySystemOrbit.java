/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.update;

import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class UpdateGalaxySystemOrbit extends Action
{
    public static final String SYSTEM = "system";
    
    public static final String TIME_SCALE = "timescale";
    
    public static final String CENTER = "center";
    
    public void setSystem(GalaxySystem system)
    {
        putValue(SYSTEM, system);
    }
    
    public GalaxySystem getSystem()
    {
        return (GalaxySystem) getValue(SYSTEM);
    }
    
    public void setTimeScale(double timescale)
    {
        putValue(TIME_SCALE, timescale);
    }
    
    public void setCenterOrbitStatus(OrbitStatus orbitstatus)
    {
        putValue(CENTER, orbitstatus);
    }

    @Override
    public void exec() throws ActionException
    {
        // if there is nothing to update then just return
        GalaxySystem updating = getSystem();
        if (updating == null)
        {
            return;
        }
        
        // no need to update if it was just updated
        long timeelapse = System.currentTimeMillis() - updating.getLastUpdate();
        if (timeelapse < 1)
        {
            return;
        }
        
        double timescale = (double) getValue(TIME_SCALE);
        double gamehours = SpaceFunctionUtil.systemMSToGameHours(timeelapse) * timescale;
        
        // now, update all of the sub orbit status
        {
            Iterator<OrbitStatus> it = updating.getSubOrbitStatus().iterator();
            while(it.hasNext())
            {
                it.next().updateElapseTime(gamehours);
            }
        }
        
        // now update the orbit status of all the orbitals
        {
            Iterator<String> it = updating.getOrbitalNames().iterator();
            while(it.hasNext())
            {
                updating.getOrbital(it.next())
                        .getOrbitStatus()
                        .updateElapseTime(gamehours);
            }
        }
        
        // if there is a center specified, then we will update
        // the system orbit status
        OrbitStatus orbiting = (OrbitStatus) getValue(CENTER);
        if (orbiting != null)
        {
            updating.getCenterOrbitStatus().updateElapseTime(gamehours);
            SpaceFunctionUtil.applyOrientOrbit(SpaceConstents.GALAXY_CENTER, 
                                               orbiting, 
                                               updating.getCenterOrbitStatus(), 
                                               updating.getGalaxyOrbitStatus());
        }
        else
        {
            // if orbiting is null, then we assume that 
        }
    }
}
