/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import java.io.Serializable;

/**
 *
 * @author REx
 */
public class FileManagerContext implements Serializable
{
    public FileManagerType managerType;
    
    public FileManagerCacheType cacheType;
    
    public String directory;
    
    public String context;
    
    public Long counter;
    
    public FileManagerContext()
    {
        
    }
    
    public FileManagerContext(FileManagerContext origin)
    {
        managerType = origin.managerType;
        cacheType   = origin.cacheType;
        directory   = origin.directory;
        context     = origin.context;
        counter     = origin.counter;
    }
}
