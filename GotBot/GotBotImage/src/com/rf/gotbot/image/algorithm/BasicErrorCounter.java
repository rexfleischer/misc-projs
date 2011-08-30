/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.algorithm;

import com.rf.gotbot.image.ImageCheckAlgorithm;
import com.rf.gotbot.image.ImageCheckConfig;
import com.rf.gotbot.image.util.ConvertToGreyScale;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class BasicErrorCounter extends ImageCheckAlgorithm
{
    private Map<String, ArrayList<BufferedImage>> images;
    
    private Map<String, boolean[]> ignores;
    
    private int maxPixelDiffForError;
    
    private int maxPixelsInError;
    
    private String[] requiredImages;
    
    public BasicErrorCounter(Robot robot) 
    {
        super(robot);
        images = new HashMap<String, ArrayList<BufferedImage>>();
        ignores = new HashMap<String, boolean[]>();
        maxPixelsInError = 0;
        maxPixelDiffForError = 0;
        requiredImages = new String[]{};
    }

    @Override
    protected void initNewScreenCallback() 
    {
        screen = ConvertToGreyScale.convert(screen);
    }
    

    //<editor-fold defaultstate="collapsed" desc="config stuff">
    @Override
    public void setConfig(ImageCheckConfig config, Object value)
    {
        switch(config)
        {
            case MAX_PIXELS_IN_ERROR:
                maxPixelsInError = (Integer) value;
                break;
            case MAX_PIXEL_DIFF_FOR_ERROR:
                maxPixelDiffForError = (Integer) value;
                break;
            case REQUIRED_IMAGE_LIST:
                requiredImages = (String[]) value;
                break;
            default:
                throw new IllegalArgumentException(
                        config.name() + " is not a supported config value here");
        }
    }
    
    @Override
    public void setImageConfig(String imageKey, ImageCheckConfig config, Object value)
    {
        if (images.containsKey(imageKey))
        {
            switch(config)
            {
                case PIXELS_TO_IGNORE:
                    ignores.put(imageKey, (boolean[])value);
                    break;
                default:
                    throw new IllegalArgumentException(
                            config.name() + " is not a supported config");
            }
        }
        else
        {
            throw new IllegalArgumentException(imageKey + " does not exists");
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="image setting">
    @Override
    public void setImage(String key, BufferedImage image)
    {
        if (images.containsKey(key))
        {
            images.get(key).add(image);
        }
        else
        {
            ArrayList<BufferedImage> newList = new ArrayList<BufferedImage>();
            newList.add(image);
            images.put(key, newList);
        }
    }
    
    @Override
    public void setImages(String key, ArrayList<BufferedImage> images)
    {
        if (this.images.containsKey(key))
        {
            this.images.get(key).addAll(images);
        }
        else
        {
            ArrayList<BufferedImage> newList = new ArrayList<BufferedImage>(images);
            this.images.put(key, newList);
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="public Map<String, ArrayList<Rectangle>> getImages(String[] keys)">
    @Override
    public Map<String, ArrayList<Rectangle>> getImages(String[] keys)
    {
        if (keys == null)
        {
            throw new NullPointerException("keys");
        }
        Map<String, ArrayList<Rectangle>> result =
                new HashMap<String, ArrayList<Rectangle>>();
        
        LinkedList<ScreenShotEntry> entryBuffer = new LinkedList<ScreenShotEntry>();
        for(int i = 0; i < keys.length; i++)
        {
            if (!images.containsKey(keys[i]))
            {
                throw new IllegalArgumentException(
                        keys[i] + " is not in the image map");
            }
            
            Iterator<BufferedImage> it = images.get(keys[i]).iterator();
            while(it.hasNext())
            {
                entryBuffer.add(new ScreenShotEntry(keys[i], it.next()));
            }
        }
        for(int i = 0; i < requiredImages.length; i++)
        {
            if (!images.containsKey(requiredImages[i]))
            {
                throw new IllegalArgumentException(
                        requiredImages[i] + " is not in the image map");
            }
            
            Iterator<BufferedImage> it = images.get(requiredImages[i]).iterator();
            while(it.hasNext())
            {
                entryBuffer.add(new ScreenShotEntry(requiredImages[i], it.next()));
            }
        }
        
        if (entryBuffer.isEmpty())
        {
            return null;
        }
        
        ScreenShotEntry[] entries = new ScreenShotEntry[entryBuffer.size()];
        for(int i = 0; i < entries.length; i++)
        {
            entries[i] = entryBuffer.remove(0);
        }
        
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
                        boolean[] ignored = null;
                        if (ignores.containsKey(entry.key))
                        {
                            ignored = ignores.get(entry.key);
                        }
                        if (unsafeCheckImage(x, y, entry.image, ignored, maxPixelDiffForError, maxPixelsInError))
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
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="public class ScreenShotEntry">
    public class ScreenShotEntry
    {
        public final String key;
        public final BufferedImage image;
        public ScreenShotEntry(final String key, final BufferedImage image)
        {
            this.key = key;
            this.image = image;
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="private boolean unsafeCheckImage">
    private boolean unsafeCheckImage(
            int xStart,
            int yStart,
            BufferedImage image,
            boolean[] ignored,
            int maxPixelError,
            int maxPixelsOut)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelInError = 0;
        
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if (ignored == null || !ignored[x + y * width])
                {
                    int imageColor = image.getRGB(x, y);
                    int screenColor = screen.getRGB(x + xStart, y + yStart);
                    if (imageColor != screenColor)
                    {
                        if (maxPixelError < Math.abs(
                                (imageColor & 0x000000FF) - 
                                (screenColor & 0x000000FF)))
                        {
                            pixelInError++;
                            if (pixelInError >= maxPixelsOut)
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
    //</editor-fold>
    
}
