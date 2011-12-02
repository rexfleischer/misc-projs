/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.config.FledProperties;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class FileManager_DoNothing implements FileManager
{
    public FileManager_DoNothing(FledProperties properties, String directory)
    {
        
    }

    @Override
    public FileTransaction beginTransaction() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String buildFileName(String id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RecordFile loadRecordFile(String id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveRecordFile(RecordFile data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteFile(String id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] loadUnknownFile(String id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveUnknownFile(String id, byte[] data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
