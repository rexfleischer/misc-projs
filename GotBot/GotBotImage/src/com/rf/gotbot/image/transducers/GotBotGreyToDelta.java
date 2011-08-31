/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.transducers;

import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.types.DeltaImage;
import com.rf.gotbot.image.types.GreyScaleImage;

/**
 *
 * @author REx
 */
public class GotBotGreyToDelta implements 
        GotBotTransducer<GreyScaleImage, DeltaImage>
{

    @Override
    public DeltaImage transduce(GreyScaleImage input) 
    {
        int oWidth = input.getWidth();
        int oHeight = input.getHeight();
        short[] horizontal  = new short[oHeight * (oWidth - 1)];
        short[] vertical    = new short[(oHeight - 1) * oWidth];
        
        // do this manually...
        short[] temp = new short[oWidth * oHeight];
        for(int y = 0; y < oHeight; y++)
        {
            for(int x = 0; x < oWidth; x++)
            {
                temp[x + y * oWidth] = (short)(input.getPixel(x, y) & 0x000000ff);
            }
        }
        
        int counter = 0;
        
        // first, do horizontal
        for(int y = 0; y < oHeight; y++)
        {
            for(int x = 0; x < oWidth - 1; x++)
            {
                if (temp[counter] < temp[counter + 1])
                {
                    horizontal[counter - y] = (short)
                        (temp[counter + 1] - temp[counter]);
                }
                else
                {
                    horizontal[counter - y] = (short)
                        (temp[counter] - temp[counter + 1]);
                }
                counter++;
            }
        }
        
        counter = 0;
        
        // now do vertical
        for(int y = 0; y < oHeight - 1; y++)
        {
            for(int x = 0; x < oWidth; x++)
            {
                if (temp[counter] < temp[counter + oWidth])
                {
                    vertical[counter] = (short)
                        (temp[counter + oWidth] - temp[counter]);
                }
                else
                {
                    vertical[counter] = (short)
                        (temp[counter] - temp[counter + oWidth]);
                }
                counter++;
            }
        }
        
        DeltaImage result = new DeltaImage();
        result.init(oWidth, oHeight, new short[][]{horizontal, vertical});
        return result;
    }
    
}
