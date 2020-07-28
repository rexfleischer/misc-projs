/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.ISerializer;

/**
 *
 * @author REx
 */
public interface IFileManager 
{
    public static final String EXTENSION = "fdb";
    
    /**
     * 
     * @param id
     * @param serializer
     * @return
     * @throws FledPresistanceException 
     */
    public Object loadFile(long id, ISerializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @param data
     * @param serializer
     * @throws FledPresistanceException 
     */
    public void updateFile(long id, Object data, ISerializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param data
     * @param serializer
     * @return
     * @throws FledPresistanceException 
     */
    public long saveFile(Object data, ISerializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @throws FledPresistanceException 
     */
    public void deleteFile(long id)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @param data
     * @param serializer
     * @throws FledPresistanceException 
     */
    public void saveNamedFile(String name, Object data, ISerializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @param serializer
     * @throws FledPersistenceException 
     */
    public Object loadNamedFile(String name, ISerializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @param serializer
     * @throws FledPersistenceException 
     */
    public void deleteNamedFile(String name)
            throws FledPersistenceException;
    
    /**
     * 
     * @return 
     */
    long incFileCount();
    
    long getFileCount();
    
    String getDirectory();
}
