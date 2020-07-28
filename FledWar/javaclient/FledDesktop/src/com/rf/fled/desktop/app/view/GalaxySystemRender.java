/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.view;

import com.rf.fled.desktop.game.Viewport;
import com.rf.fled.desktop.graphics.Text;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.Orbital;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxySystemRender
{
    private Text text;
    
    private Viewport viewport;
    
    private OrbitStatus statusWorker;
    
    protected GalaxySystemRender(Viewport viewport)
    {
        this.viewport = viewport;
        this.text = new Text(0, 0, Color.ORANGE, "");
        this.statusWorker = new OrbitStatus(0, 0, 0, new OrbitPath(".center"));
    }
    
    public void renderSystems(Graphics2D graphics, 
                              Map<ObjectId, GalaxySystem> systems)
    {
        graphics.setColor(Color.ORANGE);
        for(GalaxySystem system : systems.values())
        {
            renderGalaxySystem(graphics, system);
        }
    }
    
    private void renderGalaxySystem(Graphics2D graphics, 
                                    GalaxySystem system)
    {
        Set<String> children = system.getOrbitalNames();
        if (children.isEmpty())
        {
            OrbitStatus status = system.getGalaxyOrbitStatus();
            renderOrbitStatus(graphics, system.getName(), status);
        }
        else
        {
            OrbitStatus systemCenter = system.getGalaxyOrbitStatus();
            
            Iterator<String> it = children.iterator();
            while(it.hasNext())
            {
                String name = it.next();
                Orbital orbital = system.getOrbital(name);
                OrbitStatus status = orbital.getOrbitStatus();
                
                SpaceFunctionUtil.applyOrientOrbit(SpaceConstents.GALAXY_CENTER, 
                                                   systemCenter, 
                                                   status, 
                                                   statusWorker);
                renderOrbitStatus(graphics, orbital.getName(), statusWorker);
            }
        }
    }
    
    private void renderOrbitStatus(Graphics2D graphics, 
                                   String name, 
                                   OrbitStatus absoluteLocation)
    {
        double alpha = absoluteLocation.getAlpha();
        double distance = absoluteLocation.getDistance();
        double rawx = distance * Math.cos(alpha);
        double rawy = distance * Math.sin(alpha);
        if (viewport.place(rawx, rawy))
        {
            int x = viewport.transformX(rawx);
            int y = viewport.transformY(rawy);
            graphics.drawOval(x, y, 10, 10);
            text.setX(x);
            text.setY(y + 20);
            text.setMessage(name);
            text.render(graphics);
        }
    }
}
