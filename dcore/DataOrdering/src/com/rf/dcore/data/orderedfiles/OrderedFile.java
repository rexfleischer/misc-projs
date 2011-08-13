/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.orderedfiles;

import com.rf.dcore.data.error.OrderedFileException;
import com.rf.dcore.util.locks.DataLock;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface OrderedFile
{
    public static final String EXTENSION = "sof";

    /**
     * creates the manager of the OrderedFile to be in ready state
     * @param file the name of the file that it must create
     */
    public void create(String file)
            throws OrderedFileException;

    /**
     * initiates the manager of the OrderedFile to be in ready state
     * @param file the file this object manages
     */
    public void init(String file)
            throws OrderedFileException;

    /**
     * returns the file because this is not a serializable object. so in
     * order to persist the DataOrdering system must manage the files.
     * @return
     */
    public String getFilename()
            throws OrderedFileException;

    /**
     * returns true if the object is currently in use
     * @return boolean if object is in use, false otherwise
     */
    public boolean isUsed()
            throws OrderedFileException;

    /**
     * closes the stream and doesn't allow any more access to this system
     * unless init is called again
     */
    public void close()
            throws OrderedFileException;

    /**
     * write the bytes of the buffer starting from the location
     * @param location where the write will be initiated in bytes
     * @param buffer the data to be written
     */
    public void write(int location, ByteBuffer buffer)
            throws OrderedFileException;

    /**
     * 
     * @param location where the read will be initiated in bytes
     * @param size how many bytes will be read
     * @return
     */
    public ByteBuffer read(int location, int size)
            throws OrderedFileException;

    /**
     * locks the entire file
     * @return
     * @throws OrderedFileException
     */
    public DataLock lockFile()
            throws OrderedFileException;

    /**
     * locks a section of the file
     * @param location the start of the lock in bytes
     * @param size the amount of bytes locked
     * @return
     * @throws OrderedFileException
     */
    public DataLock lockSection(int location, int size)
            throws OrderedFileException;
}
