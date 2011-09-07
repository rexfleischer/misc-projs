/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import com.rf.gotbot.image.transducers.GotBotGreyToDelta;
import com.rf.gotbot.image.transducers.RGBBufferedImageToGotBotGrey;
import com.rf.gotbot.image.types.DeltaImage;
import com.rf.gotbot.loader.LoadImagesByDirectory;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author REx
 */
public class GalaxyOnlineLoadImages extends LoadImagesByDirectory<DeltaImage>
{
    private RGBBufferedImageToGotBotGrey toGreyTrandsucer = 
            new RGBBufferedImageToGotBotGrey();
    
    private GotBotGreyToDelta toDeltaTransducer = 
            new GotBotGreyToDelta();

    @Override
    protected DeltaImage load(File file)
    {
        try
        {
            return toDeltaTransducer.transduce(
                    toGreyTrandsucer.transduce(
                        ImageIO.read(file)));
        }
        catch(Exception ex)
        {
            System.err.println(
                    "error occurred while loading images: " + ex.getMessage());
            return null;
        }
    }
    
}
