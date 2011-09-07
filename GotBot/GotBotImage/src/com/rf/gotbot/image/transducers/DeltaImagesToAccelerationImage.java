/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.transducers;

import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.types.DeltaAccelerationImage;
import com.rf.gotbot.image.types.DeltaImage;

/**
 *
 * @author REx
 */
public class DeltaImagesToAccelerationImage implements 
        GotBotTransducer<DeltaImage[], DeltaAccelerationImage>
{

    @Override
    public DeltaAccelerationImage transduce(DeltaImage[] input) 
    {
        // first get average width and height
        int width = 0;
        int height = 0;
        for(DeltaImage image : input)
        {
            width   += image.getWidth();
            height  += image.getHeight();
        }
        width   /= input.length;
        height  /= input.length;
        
        short[] average     = null;
        short[] deviation   = null;
        
        int index = 0;
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int tempAverage = 0;
                for(DeltaImage image : input)
                {
                    tempAverage += image.getPixel(x, y);
                }
                tempAverage /= input.length;
                
                float tempDeviation = 0f;
                for(DeltaImage image : input)
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
        
        DeltaAccelerationImage result = new DeltaAccelerationImage();
        result.init(width, height, new short[][]{ average, deviation });
        return result;
    }
    
}
