/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.pagination;

import com.rf.dcore.data.error.DataOrderingException;
import com.rf.dcore.util.locks.DataLock;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface DataOrdering
{
    public static final String EXTENSION = "sdm";

    /**
     * creates a new DataOrdering system based on the given params
     * @param master the file that is going to be the master
     * @param data depending on the type of DataOrdering implementation that
     * is going to be used, you will need to hand in different data.
     * @throws DataOrderingException
     */
    public void create(String indexerName,
            String workingDir,
            String namingPrefix,
            int recordPerFile,
            int recordByteSize)
            throws  DataOrderingException;

    public String getMaster();

    /**
     * initiates the start up of the system of files based on the
     * information with the master file
     * @param master the data that defines the DataOrdering system
     * @throws DataOrderingException
     */
    public void init(String master)
            throws  DataOrderingException;

    /**
     * closes all the files and threads with this object
     * @throws DataOrderingException
     */
    public void close()
            throws  DataOrderingException;

    /**
     * closes any files that are not in use
     * @throws DataOrderingException
     */
    public void ensureOpenFiles()
            throws  DataOrderingException;

    /**
     * this just does a simple read from the given location
     * @param location location * sizeOfRecord
     * @return the bytes that were read
     * @throws DataOrderingException
     */
    public ByteBuffer read(int location)
            throws  DataOrderingException;

    /**
     * write data starting, based on records, from location 
     * @param location location * sizeOfRecord
     * @param data the data to be wrote to file
     * @throws DataOrderingException
     */
    public void write(int location, ByteBuffer data)
            throws  DataOrderingException;

    /**
     * locks a section for complex operations.
     * @param start the first record to be locked
     * @param amount the amount of records to be locked
     * @return the object that represents the lock of the section
     * @throws DataOrderingException
     */
    public DataLock lockSection(int start, int amount)
            throws DataOrderingException;

    /**
     * signals the DataOrdering system that this location can be overridden
     * @param location the location to be released
     * @throws DataOrderingException
     */
    public void release(int location)
            throws  DataOrderingException;
}
