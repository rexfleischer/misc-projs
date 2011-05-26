/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author REx
 */
public class PersistenceManager
{
    public final Observer contextsRealized = new Observer()
    {
        protected Map<String, ArrayList<String>> contexts =
                new HashMap<String, ArrayList<String>>();
        
        public void update(Observable o, Object arg)
        {
           // contexts.add((String)arg);
        }
    };

    public PersistenceManager()
    {
        
    }
}
