/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.transducers;

import com.rf.gotbot.image.GotBotTransducer;
import com.rf.gotbot.image.types.GotBotGrey;

/**
 *
 * @author REx
 */
public class GotBotGreyToRangedGrey implements 
        GotBotTransducer<GotBotGrey, GotBotGrey>
{
    private byte ranges;
    
    public GotBotGreyToRangedGrey()
    {
        ranges = 16;
    }
    
    public GotBotGreyToRangedGrey(byte ranges)
    {
        this.ranges = ranges;
    }

    @Override
    public GotBotGrey transduce(GotBotGrey input) 
    {
        byte rangeSize   = (byte)(256 / ranges);
        int width       = input.getWidth();
        int height      = input.getHeight();
        byte[] data     = new byte[width * height];
        
        int counter = 0;
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                byte thisByte = (byte)(input.getByte(x, y) + 0x80);
                byte thisRange = (byte)(thisByte / ranges);
                byte thisImpl  = (byte)(thisRange * rangeSize);
                data[counter] = thisImpl;
//                data[counter] = (byte)(
//                        ((byte)(input.getByte(x, y) / ranges) - 0x01) * rangeSize);
                ++counter;
            }
        }
        
        GotBotGrey result = new GotBotGrey();
        result.init(width, height, data);
        return result;
    }
    
}
