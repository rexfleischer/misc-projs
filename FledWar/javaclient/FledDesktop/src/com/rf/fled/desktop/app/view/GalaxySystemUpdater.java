/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.view;

import com.rf.fledwar.javaclient.connection.ClientConnection;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.model.update.UpdateGalaxySystemOrbit;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxySystemUpdater
{
    private static final Logger logger = Logger.getLogger(GalaxySystemUpdater.class);
    
    private UpdateGalaxySystemOrbit orbitupdate;
    
    private String focusname;
    
    public GalaxySystemUpdater(String focusname)
    {
        this.focusname = focusname;
        this.orbitupdate = new UpdateGalaxySystemOrbit();
    }

    void setTimeScale(double timescale)
    {
        orbitupdate.setTimeScale(timescale);
    }
    
    public void updateSystems(ClientConnection client, 
                              Map<ObjectId, GalaxySystem> systems)
    {
        List updates = (List) client.getFocus(focusname);
        if (updates != null)
        {
            logger.trace("updating from server");
            
            for(Object update : updates)
            {
                GalaxySystem current = new GalaxySystem((Map) update);
                systems.put(current.getId(), current);
            }
        }
        else
        {
            logger.trace("updating self");
            
            for(GalaxySystem system : systems.values())
            {
                orbitupdate.setSystem(system);
                
                ObjectId orbiting = system.getCenterOrbitStatus().getOrbitPath().getSystem();
                
                if (orbiting == null)
                {
                    orbitupdate.setCenterOrbitStatus(
                            SpaceConstents.GALAXY_CENTER);
                }
                else
                {
                    if (systems.containsKey(orbiting))
                    {
                        orbitupdate.setCenterOrbitStatus(
                                systems.get(orbiting).getGalaxyOrbitStatus());
                    }
                    else
                    {
                        orbitupdate.setCenterOrbitStatus(null);
                    }
                }
                
                try
                {
                    orbitupdate.exec();
                }
                catch(ActionException ex)
                {
                    logger.error("exception during system update", ex);
                }
                system.triggerLastUpdate();
            }
        }
    }
    
}
