/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.fileio;

import java.io.IOException;

/**
 *
 * @author REx
 */
public interface RecordFile 
{
    /**
     * size of the file
     * @return 
     */
    public long fileSize()
            throws IOException;
    
    /**
     * returns the number of records in the file
     * @return 
     */
    public int numOfRecords()
            throws IOException;
    
    /**
     * returns the number of possible records
     * @return
     * @throws IOException 
     */
    public int recordCompacity()
            throws IOException;
    
    /**
     * gets the user meta
     * @return 
     */
    public byte[] getMeta()
            throws IOException;
    
    /**
     * sets the user meta. this has to be all of the meta, it does not append.
     * @param meta 
     */
    public void setMeta(byte[] meta)
            throws IOException;
    
    /**
     * size of a record
     * @return 
     */
    public int recordSize(int index) 
            throws IOException;
    
    /**
     * removes the amount of bytes from the file starting at pos
     * @param pos
     * @param amount
     * @throws IOException 
     */
    public void remove(int index)
            throws IOException;
    
    /**
     * read bytes to dest starting at pos with a length of dest.length
     * @param dest
     * @param pos 
     */
    public byte[] read(int index) 
            throws IOException;
    
    /**
     * 
     * @param bytes
     * @param index
     * @throws IOException 
     */
    public void write(byte[] src, int index)
            throws IOException;
    
    /**
     * 
     * @param src
     * @param index
     * @throws IOException 
     */
    public void insert(byte[] src, int index)
            throws IOException;
   
    /**
     * commits saves what is in the buffer in memory and
     * puts it to disk
     */
    public void commit()
            throws IOException;
    
    /**
     * this undoes everything that has been deleted or inserted
     */
    public void rollback()
            throws IOException;
    
    /**
     * 
     */
    public boolean isFull()
            throws IOException;
}
