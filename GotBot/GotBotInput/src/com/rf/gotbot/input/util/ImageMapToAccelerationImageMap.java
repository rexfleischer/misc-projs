/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.input.util;

import com.rf.gotbot.image.GotBotImage;
import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.transducers.GotBotImagesToGotBotDeviation;
import com.rf.gotbot.image.types.GotBotDeviation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ImageMapToAccelerationImageMap implements 
        GotBotTransducer<Map<String, GotBotImage>, Map<String, GotBotDeviation>>
{

    @Override
    public Map<String, GotBotDeviation> transduce(Map<String, GotBotImage> input) 
    {
        ArrayList<String> sorted = new ArrayList<>(input.keySet());
        Collections.sort(sorted);
        Iterator<String> it = sorted.iterator();
        
        GotBotImagesToGotBotDeviation transducer = 
                new GotBotImagesToGotBotDeviation();
        
        Map<String, GotBotDeviation> result = 
                new HashMap<>();
        
        ArrayList<GotBotImage> group = new ArrayList<>();
        String basename = null;
        while(it.hasNext())
        {
            String thisName = it.next();
            String thisBasename = basename(thisName);
            if (basename == null)
            {
                basename = basename(thisName);
            }
            
            if (thisBasename.equalsIgnoreCase(basename))
            {
                group.add(input.get(thisName));
            }
            else
            {
                GotBotImage[] resultingGroup = new GotBotImage[group.size()];
                resultingGroup = group.toArray(resultingGroup);
                result.put(basename, transducer.transduce(resultingGroup));
                group.clear();
                group.add(input.get(thisName));
                basename = thisBasename;
            }
        }
        
        if (!group.isEmpty())
        {
            GotBotImage[] resultingGroup = new GotBotImage[group.size()];
            resultingGroup = group.toArray(resultingGroup);
            result.put(basename, transducer.transduce(resultingGroup));
            group.clear();
        }
        
        return result;
    }
    
    private String basename(String name)
    {
        int pos = name.lastIndexOf(".");
        String result = null;
        if (pos == -1)
        {
            result = name;
        }
        else
        {
            result = name.substring(0, pos);
        }
        return result;
    }
    
}
