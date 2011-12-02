/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.config.FledConfigOption;
import com.rf.fled.config.FledProperties;
import java.io.IOException;

/**
 *
 * @author REx
 */
public enum RecordFileFactory 
{
    MEM_BYTE_ARRAY()
    {

        @Override
        public RecordFile getNewInstance(
                FledProperties properties, String fileId, byte[] meta) 
                throws IOException 
        {
            int recordCount = Integer
                    .parseInt(properties
                    .getProperty(FledConfigOption.RECORD_FILE_SIZE.toString()));
            return new RecordFile_MemByteArray(
                    fileId, recordCount, meta);
        }

        @Override
        public RecordFile getExistingInstance(
                FledProperties properties, String filename) 
                throws IOException 
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    };
    
    public abstract RecordFile getNewInstance(
            FledProperties properties, String fileId, byte[] meta) 
            throws IOException;
    
    public abstract RecordFile getExistingInstance(
            FledProperties properties, String filename)
            throws IOException;
}
