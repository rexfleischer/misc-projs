/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.input.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 *
 * @author REx
 */
public class ScreenShot 
{
    public static BufferedImage shoot(Robot robot)
    {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        return robot.createScreenCapture(new Rectangle(0, 0, 
                                (int) screenDim.getWidth(),
                                (int) screenDim.getHeight()));
    }
}
