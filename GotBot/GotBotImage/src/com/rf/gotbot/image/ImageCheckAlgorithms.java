/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image;

import com.rf.gotbot.image.algorithm.BasicErrorCounter;
import com.rf.gotbot.image.algorithm.SingleDelta;
import java.awt.Robot;

/**
 *
 * @author REx
 */
public enum ImageCheckAlgorithms 
{
    BASIC_ERROR_COUNT()
    {
        @Override
        public ImageCheckAlgorithm get(Robot robot)
        {
            return new BasicErrorCounter(robot);
        }
    },
    SINGLE_DELTAING()
    {
        @Override
        public ImageCheckAlgorithm get(Robot robot)
        {
            return new SingleDelta(robot);
        }
    };
    
    
    public abstract ImageCheckAlgorithm get(Robot robot);
}
