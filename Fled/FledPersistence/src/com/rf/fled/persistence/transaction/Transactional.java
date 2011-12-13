/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.filemanager.FileManager;

/**
 * optional extension to Persistence
 * @author REx
 */
public interface Transactional 
{
    /**
     * 
     * @param newManager
     * @return 
     */
    public Transactional deepCopy(FileManager newManager);
    
    /**
     * 
     * @return 
     */
    public FileManager getFileManager();
}
