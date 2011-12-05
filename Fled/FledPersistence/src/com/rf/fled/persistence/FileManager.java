/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.interfaces.Serializer;

/**
 *
 * @author REx
 */
public interface FileManager 
{
    public static final String EXTENSION = "db";
    
    public FileManager beginTransaction()
            throws FledTransactionException;
    
    public void commit()
            throws FledTransactionException;
    
    public void rollback()
            throws FledTransactionException;
    
    public Object loadFile(long id, Serializer<byte[]> serializer)
            throws FledPresistanceException;
    
    public void updateFile(long id, Object data, Serializer<byte[]> serializer)
            throws FledPresistanceException;
    
    public long saveFile(Object data, Serializer<byte[]> serializer)
            throws FledPresistanceException;
    
    public void deleteFile(long id)
            throws FledPresistanceException;
    
    public Object loadNamedFile(String name, Serializer<byte[]> serializer)
            throws FledPresistanceException;
    
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer)
            throws FledPresistanceException;
    
    public void deleteNamedFile(String name)
            throws FledPresistanceException;
    
    long incFileCount();
}
