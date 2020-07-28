/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.source;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * this represents a log file that is targeted for consuming. there is 
 * only one thread analyzing a file at a time.
 * @author REx
 */
public class LogFile
{
    private static final int LINES = 10;
    
    private String file;
    
    private BufferedReader reader;
    
    public LogFile(String file) throws IOException
    {
        this.file = file;
        this.reader = FileLineReaderFactory
                .discover(file)
                .getBufferedReader(file);
    }
    
    public synchronized String[] getNextLines() throws IOException
    {
        String[] lines = new String[LINES];
        
        for(int i = 0; i < LINES; i++)
        {
            lines[i] = reader.readLine();
        }
        
        return lines;
    }
}
