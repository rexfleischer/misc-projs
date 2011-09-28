/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.transducers;

import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.types.GotBotGrey;
import java.awt.image.BufferedImage;

/**
 *
 * @author REx
 */
public class RGBBufferedImageToGotBotGrey implements 
        GotBotTransducer<BufferedImage, GotBotGrey>
{

    @Override
    public GotBotGrey transduce(BufferedImage input) 
    {
        byte[] data = new byte[input.getWidth() * input.getHeight()];
        int counter = 0;
        for(int y = 0; y < input.getHeight(); y++)
        {
            for(int x = 0; x < input.getWidth(); x++)
            {
                int rgb = input.getRGB(x, y);
                data[counter] = (byte)(
                            ((rgb & 0x00FF0000) >> 16) * 0.3 +  // red
                            ((rgb & 0x0000FF00) >> 8) * 0.59 +  // green
                             (rgb & 0x000000FF) * 0.11);        // blue
                counter++;
            }
        }
        
        GotBotGrey result = new GotBotGrey();
        result.init(input.getWidth(), input.getHeight(), data);
        return result;
    }
    
}
