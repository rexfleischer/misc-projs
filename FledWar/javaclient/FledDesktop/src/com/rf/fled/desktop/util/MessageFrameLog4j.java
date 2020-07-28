/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.util;

import java.awt.TextArea;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author REx
 */
public class MessageFrameLog4j extends AppenderSkeleton
{
    public static TextArea text;

    @Override
    protected void append(LoggingEvent le)
    {
        if (this.layout == null)
        {   
            errorHandler.error("No layout for appender " + name ,
                               null,
                               ErrorCode.MISSING_LAYOUT);
            return;
        }
        
        if (text != null)
        {
            String message = this.layout.format(le);
            text.append(message);
        }
    }

    @Override
    public void close()
    {
        // shrug
    }

    @Override
    public boolean requiresLayout()
    {
        return true;
    }
    
}
