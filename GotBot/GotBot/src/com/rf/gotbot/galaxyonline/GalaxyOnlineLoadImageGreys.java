/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import com.rf.gotbot.image.GotBotImage;
import com.rf.gotbot.image.transducers.RGBBufferedImageToGotBotGrey;
import com.rf.gotbot.image.types.GotBotGrey;
import com.rf.gotbot.loader.LoadImagesByDirectory;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author REx
 */
public class GalaxyOnlineLoadImageGreys extends 
        LoadImagesByDirectory<GotBotImage>
{
    private RGBBufferedImageToGotBotGrey toGreyTrandsucer = 
            new RGBBufferedImageToGotBotGrey();
    
    @Override
    protected GotBotGrey load(File file) 
    {
        try
        {
            return toGreyTrandsucer.transduce(ImageIO.read(file));
        }
        catch(Exception ex)
        {
            System.err.println(
                    "error occurred while loading images: " + ex.getMessage());
            return null;
        }
    }
    
}
