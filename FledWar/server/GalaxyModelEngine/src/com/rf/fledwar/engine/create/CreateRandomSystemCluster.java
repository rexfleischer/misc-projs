/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.engine.dao.BasicDAOException;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.GalaxySystemState;
import com.rf.fledwar.model.GalaxySystemType;
import com.rf.fledwar.model.update.Action;
import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class CreateRandomSystemCluster extends Action
{
    public static final String CLUSTER = "cluster";
    
    public static final String NAME = "name";
    
    public static final String CLUSTER_ORBIT_STATUS = "orbit_status";
    
    public static final String MAX_SYSTEMS = "max_systems";
    
    public static final String MIN_SYSTEMS = "min_systems";
    
    public static final String MAX_CLUSTER_RADIUS = "max_cluster_radius";
    
    public static final String COLLISION_DISTANCE = "collision_distance";
    
    public static final int DEFAULT_MAX_SYSTEMS = 50;
    
    public static final int DEFAULT_MIN_SYSTEMS = 20;
    
    public static final double DEFAULT_MAX_CLUSTER_RADIUS = SpaceConstents.LIGHTYEAR * 20;
    
    public static final double DEFAULT_COLLISION_DISTANCE = SpaceConstents.LIGHTYEAR;
    
    public List<GalaxySystem> getCluster()
    {
        return (List<GalaxySystem>) getValue(CLUSTER);
    }
    
    public void setClusterName(String name)
    {
        putValue(NAME, name);
    }
    
    public void setClusterOrbitStatus(OrbitStatus status)
    {
        putValue(CLUSTER_ORBIT_STATUS, status);
    }
    
    public void setMaxSystems(int max)
    {
        putValue(MAX_SYSTEMS, max);
    }
    
    public void setMinSystem(int min)
    {
        putValue(MIN_SYSTEMS, min);
    }
    
    public void setMaxClusterRadius(double meters)
    {
        putValue(MAX_CLUSTER_RADIUS, meters);
    }
    
    private int getMaxSystems()
    {
        return (getValues().containsKey(MAX_SYSTEMS)) 
                ? (int) getValue(MAX_SYSTEMS) 
                : DEFAULT_MAX_SYSTEMS;
    }
    
    private int getMinSystems()
    {
        return (getValues().containsKey(MIN_SYSTEMS)) 
                ? (int) getValue(MIN_SYSTEMS) 
                : DEFAULT_MIN_SYSTEMS;
    }
    
    private double getMaxClusterRadius()
    {
        return (getValues().containsKey(MAX_CLUSTER_RADIUS))
                ? (double) getValue(MAX_CLUSTER_RADIUS)
                : DEFAULT_MAX_CLUSTER_RADIUS;
    }
    
    private double getCollisionDistance()
    {
        return (getValues().containsKey(COLLISION_DISTANCE))
                ? (double) getValue(COLLISION_DISTANCE)
                : DEFAULT_COLLISION_DISTANCE;
    }

    @Override
    public void exec() throws ActionException
    {
        List<GalaxySystem> resultcluster = new ArrayList<>();
        List<GalaxySystem> cluster = new ArrayList<>();
        putValue(CLUSTER, resultcluster);
        
        OrbitStatus centerstatus = (OrbitStatus) getValue(CLUSTER_ORBIT_STATUS);
        double currentyear = SpaceFunctionUtil.currentGameYear();
        double maxclusterradius = getMaxClusterRadius();
        double collisiondistance = getCollisionDistance();
        String name = (String) getValue(NAME);
        Integer maxsystems = getMaxSystems();
        Integer minsystems = getMinSystems();
        Random random = new Random();
        
        if (centerstatus == null)
        {
            throw new ActionException(
                    "configuration exception: clustercenter must be specified");
        }
        
        if (name == null)
        {
            throw new ActionException(
                    "configuration exception: cluster name must be specified");
        }
        
        GalaxySystem clustercenter = new GalaxySystem();
        clustercenter.setState(GalaxySystemState.UPDATING);
        clustercenter.setSystemType(GalaxySystemType.GRAVITY_CENTER);
        clustercenter.setName(String.format("%s-center", name));
        clustercenter.getGalaxyOrbitStatus().setAlpha(      centerstatus.getAlpha());
        clustercenter.getGalaxyOrbitStatus().setDeltaAlpha( centerstatus.getDeltaAlpha());
        clustercenter.getGalaxyOrbitStatus().setDistance(   centerstatus.getDistance());
        clustercenter.getGalaxyOrbitStatus().setOrbitPath(  centerstatus.getOrbitPath());
        clustercenter.getCenterOrbitStatus().setAlpha(      centerstatus.getAlpha());
        clustercenter.getCenterOrbitStatus().setDeltaAlpha( centerstatus.getDeltaAlpha());
        clustercenter.getCenterOrbitStatus().setDistance(   centerstatus.getDistance());
        clustercenter.getCenterOrbitStatus().setOrbitPath(  centerstatus.getOrbitPath());
        
        int systemcount = random.nextInt(maxsystems - minsystems) + minsystems;
        for(int i = 0; i < systemcount; i++)
        {
            CreateRandomSystem randomsystem = new CreateRandomSystem();
            randomsystem.setCurrentYear(currentyear);
            randomsystem.setSystemName(createSystemName(name, i));
            randomsystem.exec();
            
            // get the new system and set the state to updating
            // making the background threads not try to update
            GalaxySystem system = randomsystem.getSystem();
            system.setState(GalaxySystemState.UPDATING);
            cluster.add(system);
        }
        
        
        /**
         * we want to try and rollback the systems that
         * are being inserted if something goes wrong.
         */
        GalaxySystemDAO gsdao = null;
        try
        {
            gsdao = new GalaxySystemDAO();
            
            // now that we have the id of the center of
            // the cluster, we can create where all of 
            // the other systems are...
            gsdao.insertAsIs(clustercenter);
            
            // create the orbit status for all of the 
            // playable galaxy system
            OrbitPath clusterpath = new OrbitPath(1);
            clusterpath.setSystem(clustercenter.getId());
            clusterpath.setPathPart(0, OrbitPath.PATH_CENTER);
            OrbitPath galaxypath = new OrbitPath(".center");
            
            // the delta_alpha of all the systems in the 
            // cluster must have the same delta alpha
            double delta_alpha = random.nextDouble() * 0.0000001;
            // @TODO: make this configurable
            
            // the reason we dont use an iterator is 
            // because we need to check the previously
            // iterated GalaxySystems so we can make
            // sure systems arent too close together...
            // but its cool, its an array list so
            // we have random access..
            for(int i = 0; i < cluster.size(); i++)
            {
                GalaxySystem system = cluster.get(i);
                
                boolean foundplace = false;
                
                // try to place no more than 50 times
                for(int j = 0; j < 50 && !foundplace; j++)
                {
                    OrbitStatus randomstatus = randomOrbitStatus(random, 
                                                                 maxclusterradius, 
                                                                 delta_alpha, 
                                                                 clusterpath);
                    
                    // check all of the already inserted systems 
                    // to make sure none of the get too close
                    boolean collision = false;
                    for(int k = 0; k < i && !collision; k++)
                    {
                        GalaxySystem systemchecking = cluster.get(k);
                        OrbitStatus statuschecking = systemchecking.getCenterOrbitStatus();
                        if (SpaceFunctionUtil.distanceBetweenTwoObjects(
                                statuschecking.getAlpha(), 
                                statuschecking.getDistance(), 
                                randomstatus.getAlpha(), 
                                randomstatus.getDistance()) <= collisiondistance)
                        {
                            collision = true;
                        }
                    }
                    
                    if (!collision)
                    {
                        OrbitStatus destination = system.getCenterOrbitStatus();
                        destination.setAlpha(randomstatus.getAlpha());
                        destination.setDeltaAlpha(randomstatus.getDeltaAlpha());
                        destination.setDistance(randomstatus.getDistance());
                        destination.setOrbitPath(randomstatus.getOrbitPath());
                        SpaceFunctionUtil.applyOrientOrbit(SpaceConstents.GALAXY_CENTER, 
                                                           centerstatus, 
                                                           destination, 
                                                           system.getGalaxyOrbitStatus());
                        
                        // we dont know the delta of the galaxy orbit because
                        // this system is not directly orbiting the center
                        // of the galaxy
                        system.getGalaxyOrbitStatus().setDeltaAlpha(0.0);
                        system.getGalaxyOrbitStatus().setOrbitPath(galaxypath);
                        foundplace = true;
                    }
                }
                
                if (foundplace)
                {
                    gsdao.insertAsIs(system);
                    resultcluster.add(system);
                }
                else
                {
                    if (minsystems <= resultcluster.size())
                    {
                        break;
                    }
                }
            }
            
            // now that we got here, we need to go through
            // all of the new galaxy systems and unflag them
            // so they can be updated and worked on by
            // the background threads
            gsdao.finishedWithUpdateAndUnflag(clustercenter);
            for(GalaxySystem system : resultcluster)
            {
                gsdao.finishedWithUpdateAndUnflag(system);
            }
        }
        catch(BasicDAOException ex)
        {
            // ok, check all of the systems for an id
            // and try to remove the ones that have an id
            if (gsdao != null)
            {
                Iterator<GalaxySystem> it = cluster.iterator();
                while(it.hasNext())
                {
                    GalaxySystem system = it.next();
                    safeRemove(system, gsdao);
                }
                
                safeRemove(clustercenter, gsdao);
            }
            
            throw new ActionException(ex);
        }
        
    }
    
    private String createSystemName(String clustername, int iter)
    {
        return String.format("%s-system%s", clustername, iter);
    }
    
    private void safeRemove(GalaxySystem system, GalaxySystemDAO gsdao)
    {
        ObjectId systemid = system.getId();
        if (systemid != null)
        {
            try
            {
                gsdao.removeFromIdQuick(systemid);
            }
            catch(Throwable ex)
            {
                // @TODO: make this log through log4j
                System.err.println(String.format("unable to remove system [systemid:%s]", systemid));
            }
        }
    }
    
    private OrbitStatus randomOrbitStatus(Random random, 
                                          double maxdist, 
                                          double delta_alpha, 
                                          OrbitPath path)
    {
        return new OrbitStatus(random.nextDouble() * maxdist, 
                               random.nextDouble() * (2 * Math.PI), 
                               delta_alpha, 
                               path);
    }
}
