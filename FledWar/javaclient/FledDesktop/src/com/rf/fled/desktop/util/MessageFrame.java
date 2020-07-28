/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.util;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.TextArea;
import javax.swing.JFrame;

/**
 *
 * @author REx
 */
public class MessageFrame extends JFrame
{
    
    public MessageFrame() throws HeadlessException
    {
        super("Log Message Frame");
        MessageFrameLog4j.text = new TextArea(30, 100);
        MessageFrameLog4j.text.setEditable(false);
        getContentPane().add(MessageFrameLog4j.text, BorderLayout.CENTER);
        pack();
    }
    
    public void addMessage(String message)
    {
        MessageFrameLog4j.text.append(message);
        MessageFrameLog4j.text.append("\n");
    }
}
