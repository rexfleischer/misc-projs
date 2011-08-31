/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.output.util;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 *
 * @author REx
 */
public class ShowImage 
{
    public static void show(BufferedImage screen)
    {
        JWindow window = new JWindow(new JFrame());
        window.getContentPane().setLayout(new BorderLayout());
        window.getContentPane().add(BorderLayout.CENTER, new JLabel(new ImageIcon(screen)));
        window.pack();
        window.setVisible(true);
    }
}
