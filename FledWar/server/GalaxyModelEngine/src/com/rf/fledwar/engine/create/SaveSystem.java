/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.engine.dao.BasicDAOException;
import com.rf.fledwar.model.GalaxySystem;

/**
 *
 * @author REx
 */
public class SaveSystem
{
    public static void insertSystem(GalaxySystem system)
            throws ActionException
    {
        try
        {
            GalaxySystemDAO dao = new GalaxySystemDAO();
            dao.insert(system);
        }
        catch(BasicDAOException ex)
        {
            throw new ActionException(ex);
        }
    }
    
    public static void updateSystem(GalaxySystem system)
            throws ActionException
    {
        try
        {
            GalaxySystemDAO dao = new GalaxySystemDAO();
            dao.update(system);
        }
        catch(BasicDAOException ex)
        {
            throw new ActionException(ex);
        }
    }
}
