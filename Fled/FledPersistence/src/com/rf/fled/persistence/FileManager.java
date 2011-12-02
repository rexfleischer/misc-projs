/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.exceptions.FledIOException;
import com.rf.fled.exceptions.FledTransactionException;
import com.rf.fled.interfaces.Serializer;

/**
 *
 * @author REx
 */
public interface FileManager 
{
    public static final String EXTENSION = "db";
    
    public FileManager beginTransaction()
            throws FledIOException;
    
    public void commit()
            throws FledIOException, FledTransactionException;
    
    public void rollback()
            throws FledIOException;
    
    public Object loadFile(long id, Serializer<byte[]> serializer)
            throws FledIOException;
    
    public void updateFile(long id, Object data, Serializer<byte[]> serializer)
            throws FledIOException;
    
    public long saveFile(Object data, Serializer<byte[]> serializer)
            throws FledIOException;
    
    public void deleteFile(long id)
            throws FledIOException;
    
    public Object loadNamedFile(String name, Serializer<byte[]> serializer)
            throws FledIOException;
    
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer)
            throws FledIOException;
    
    public void deleteNamedFile(String name)
            throws FledIOException;
    
    long incFileCount();
}
