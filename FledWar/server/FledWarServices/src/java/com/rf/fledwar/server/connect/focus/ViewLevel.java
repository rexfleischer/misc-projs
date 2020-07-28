/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.mongodb.BasicDBObject;
import com.rf.fledwar.model.GalaxySystem;

/**
 *
 * @author REx
 */
public enum ViewLevel
{
    ALL(new BasicDBObject()),
    NO_ORBITALS(new BasicDBObject(GalaxySystem.ORBITALS, 0)),
    NAMES(new BasicDBObject(GalaxySystem.NAME, 1).append(GalaxySystem.ORBIT_STATUS_GALAXY, 1)),
    ;
    
    private BasicDBObject view;
    
    private ViewLevel(BasicDBObject view)
    {
        this.view = view;
    }
    
    public BasicDBObject getView()
    {
        return view;
    }
}
