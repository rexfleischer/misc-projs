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
public enum FileManagerFactory 
{
    DO_NOTHING()
    {
        @Override
        public FileManager getInstance(
                FledProperties properties, String directory)
            throws IOException
        {
            return new FileManager_DoNothing(properties, directory);
        }
    },
    MEM_BYTE_ARRAY()
    {
        @Override
        public FileManager getInstance(
                FledProperties properties, String directory)
            throws IOException
        {
            return new FileManager_FlatFile(properties, directory);
        }
    };
    
    public abstract FileManager getInstance(
            FledProperties properties, String directory)
            throws IOException;
}
