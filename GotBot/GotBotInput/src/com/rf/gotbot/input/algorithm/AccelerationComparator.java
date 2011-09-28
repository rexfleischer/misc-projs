/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.input.algorithm;

import com.rf.gotbot.image.transducers.RGBBufferedImageToGotBotGrey;
import com.rf.gotbot.image.types.GotBotDeviation;
import com.rf.gotbot.image.types.GotBotGrey;
import com.rf.gotbot.input.ImageCheckAlgorithm;
import com.rf.gotbot.input.util.ScreenShot;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class AccelerationComparator implements 
        ImageCheckAlgorithm<GotBotDeviation>
{
    private Robot robot;
    
    private Map<String, GotBotDeviation> allImages;
    
    private GotBotGrey screen;

    public AccelerationComparator(Robot robot) 
    {
        this.robot  = robot;
        allImages   = null;
    }

    @Override
    public void initNewScreen() 
    {
        screen = (new RGBBufferedImageToGotBotGrey())
                    .transduce(ScreenShot.shoot(robot));
    }

    @Override
    public void finished() 
    {
        screen = null;
    }

    @Override
    public void setImages(Map<String, GotBotDeviation> images) 
    {
        allImages = images;
    }

    @Override
    public void setConfig(String config, Object value) 
    {
        
    }

    @Override
    public void setImageConfig(String imageKey, String config, Object value) 
    {
        
    }
    
    public class ScreenShotEntry
    {
        public final String key;
        public final GotBotDeviation image;
        public ScreenShotEntry(final String key, final GotBotDeviation image)
        {
            this.key = key;
            this.image = image;
        }
    }

    @Override
    public Map<String, ArrayList<Rectangle>> getImages(String[] keys) 
    {
        if (keys == null)
        {
            throw new NullPointerException("key");
        }
        if (keys.length == 0)
        {
            throw new IllegalArgumentException("keys cannot be empty");
        }
        
        Map<String, ArrayList<Rectangle>> result =
                new HashMap<String, ArrayList<Rectangle>>();
        
        LinkedList<ScreenShotEntry> entryBuffer = new LinkedList<ScreenShotEntry>();
        for(int i = 0; i < keys.length; i++)
        {
            if (!allImages.containsKey(keys[i]))
            {
                throw new IllegalArgumentException(
                        keys[i] + " is not in the image map");
            }
            entryBuffer.add(new ScreenShotEntry(
                    keys[i], 
                    allImages.get(keys[i])));
        }
        
        ScreenShotEntry[] entries = new ScreenShotEntry[entryBuffer.size()];
        entries = entryBuffer.toArray(entries);
        
        int searchWidth = screen.getWidth();
        int searchHeight = screen.getHeight();
        
        for(int x = 0; x < searchWidth; x++)
        {
            for(int y = 0; y < searchHeight; y++)
            {
                for(ScreenShotEntry entry : entries)
                {
                    if (x + entry.image.getWidth() < searchWidth &&
                            y + entry.image.getHeight() < searchHeight)
                    {
                        if (unsafeCheckImage(x, y, entry.image))
                        {
                            if (result.containsKey(entry.key))
                            {
                                result.get(entry.key).add(new Rectangle(x, y,
                                        entry.image.getWidth(),
                                        entry.image.getHeight()));
                            }
                            else
                            {
                                ArrayList<Rectangle> insert =
                                        new ArrayList<Rectangle>();
                                insert.add(new Rectangle(x, y,
                                        entry.image.getWidth(),
                                        entry.image.getHeight()));
                                result.put(entry.key, insert);
                            }
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    private boolean unsafeCheckImage(int xStart, int yStart, GotBotDeviation image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        
        for(int x = 1; x < width - 1; x++)
        {
            for(int y = 1; y < height - 1; y++)
            {
                int screenColor = screen.getPixel(x + xStart, y + yStart);
                if (!image.isAcceptablePixel(x, y, screenColor))
                {
                    return false;
                }
            }
        }
        
        return true;
    }
}
