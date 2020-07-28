/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.focus;

import com.rf.fled.desktop.game.GameException;
import com.rf.fled.desktop.game.Viewport;
import com.rf.fledwar.model.GalaxySystem;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public interface FocusState
{
    public void update(Viewport viewport, Map<ObjectId, GalaxySystem> systems)
            throws GameException;
    
    public ObjectId getFocusId();
}
