/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.transducers;

import com.rf.gotbot.image.GotBotImage;
import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.types.GotBotDeviation;

/**
 *
 * @author REx
 */
public class GotBotImagesToGotBotDeviation implements 
        GotBotTransducer<GotBotImage[], GotBotDeviation>
{

    @Override
    public GotBotDeviation transduce(GotBotImage[] input) 
    {
        // first get average width and height
        int width = 0;
        int height = 0;
        for(GotBotImage image : input)
        {
            width   += image.getWidth();
            height  += image.getHeight();
        }
        width   /= input.length;
        height  /= input.length;
        
        if (width != input[0].getWidth() || height != input[0].getHeight())
        {
            throw new IllegalArgumentException(
                    "all delta images must be same size");
        }
        
        short[] average     = new short[width * height];
        short[] deviation   = new short[width * height];
        
        int index = 0;
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int tempAverage = 0;
                for(GotBotImage image : input)
                {
                    tempAverage += (image.getPixel(x, y) & 0x000000FF);
                }
                tempAverage /= input.length;
                
                float tempDeviation = 0f;
                for(GotBotImage image : input)
                {
                    int snip = (image.getPixel(x, y) & 0x000000FF) - tempAverage;
                    tempDeviation += snip * snip;
                }
                tempDeviation /= input.length;
                
                deviation[index] = (short)Math.round(Math.sqrt(tempDeviation));
                average[index]   = (short)tempAverage;
                index++;
            }
        }
        
        GotBotDeviation result = new GotBotDeviation();
        result.init(width, height, new short[][]{ average, deviation });
        return result;
    }
    
}
