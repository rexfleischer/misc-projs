/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import java.io.IOException;

/**
 *
 * @author REx
 */
public interface FileManager
{
    /**
     * the reason there are two different types of saves and loads is because 
     * RecordFiles are the files that can be processed during transactions
     * @return
     * @throws IOException 
     */
    public FileTransaction beginTransaction()
            throws IOException;
    
    public String buildFileName(String id)
            throws IOException;
    
    public RecordFile loadRecordFile(String id)
            throws IOException;
    
    public void saveRecordFile(RecordFile data)
            throws IOException;
    
    public void deleteFile(String id)
            throws IOException;
    
    public byte[] loadUnknownFile(String id)
            throws IOException;
    
    public void saveUnknownFile(String id, byte[] data)
            throws IOException;
}
