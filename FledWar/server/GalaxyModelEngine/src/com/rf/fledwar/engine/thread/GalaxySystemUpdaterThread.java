/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.model.update.UpdateGalaxySystemOrbit;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxySystemUpdaterThread extends BackgroundThread
{

    public GalaxySystemUpdaterThread()
    {
        super();
    }

    @Override
    protected void doSingleIteration() throws Exception
    {
        GalaxySystemDAO gsdao = null;
        GalaxySystem system = null;
        try
        {
            gsdao = new GalaxySystemDAO();
            system = gsdao.findLastUpdatedAndFlag();
            
            if (system == null)
            {
                return;
            }
            
            logger.info(String.format("updating system [name:%s]",
                                      system.getName()));

            UpdateGalaxySystemOrbit orbitupdate = new UpdateGalaxySystemOrbit();
            if (logger.isDebugEnabled())
            {
                logger.debug(String.format("before:\n%s", system));
            }
            
            OrbitPath orbitpath = system.getCenterOrbitStatus().getOrbitPath();
            ObjectId orbiting = orbitpath.getSystem();
            if (orbiting != null)
            {
                GalaxySystem centersystem = gsdao.findOneFromId(orbiting);
                if (centersystem == null)
                {
                    throw new ActionException(String.format(
                            "unable to find center system when a system was "
                            + "specified [updating:%s, specified path:%s]", 
                            system.getName(), 
                            orbitpath.toFullPath()));
                }
                OrbitStatus center = centersystem.getGalaxyOrbitStatus();
                orbitupdate.setCenterOrbitStatus(center);
            }
//            else
//            {
//                center = SpaceConstents.GALAXY_CENTER;
//            }
            
            orbitupdate.setTimeScale(GalaxyModelEngine.getTimeScale());
            orbitupdate.setSystem(system);
            orbitupdate.exec();
            if (logger.isDebugEnabled())
            {
                logger.debug(String.format("after:\n%s", system));
            }

            gsdao.finishedWithUpdateAndUnflag(system);
        }
        catch(Throwable ex)
        {
            StringBuilder report = new StringBuilder("error during background thread update... exception:\n");
            report.append("  ");
            report.append(ex.getMessage());
            report.append("\n");
            report.append("  trying to unflag system:\n");
            if (system == null)
            {
                report.append("    system never successfully found\n");
            }
            else
            {
                try
                {
                    gsdao.unflagWithoutUpdate(system);
                    report.append("    successfully unflaged ");
                }
                catch(Throwable th)
                {
                    report.append("    could not unflag system: (");
                    report.append(th.getMessage());
                    report.append(") ");
                }
                report.append("[system id:");
                report.append(system.getId());
                report.append("]\n");
            }
            
            logger.error(report.toString(), ex);
        }
    }
    
}
