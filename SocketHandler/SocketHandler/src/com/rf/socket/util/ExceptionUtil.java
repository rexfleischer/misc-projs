/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author REx
 */
public class ExceptionUtil 
{
    public static void logError(String message, Exception ex)
    {
        Writer out = new StringWriter();
        ex.printStackTrace(new PrintWriter(out));
        System.err.println(message);
        System.err.println(out);
    }
    
    public static void logError(Exception ex)
    {
        Writer out = new StringWriter();
        ex.printStackTrace(new PrintWriter(out));
        System.err.println(out);
    }
}
