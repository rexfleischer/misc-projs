/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.pagination.impls;

import com.rf.dcore.data.error.CorruptedDataException;
import com.rf.dcore.data.error.DataOrderingException;
import com.rf.dcore.data.error.OrderedFileException;
import com.rf.dcore.data.orderedfiles.OrderedFile;
import com.rf.dcore.data.orderedfiles.OrderedFiles;
import com.rf.dcore.data.pagination.DataOrdering;
import com.rf.dcore.data.pagination.DataOrderings;
import com.rf.dcore.util.FileSerializer;
import com.rf.dcore.util.KeyPool;
import com.rf.dcore.util.locks.DataLock;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author REx
 */
public class ThreadSafeOrderedPagination implements DataOrdering
{

    // <editor-fold defaultstate="collapsed" desc="helper objects">
    protected class MasterData implements Serializable
    {
        protected String[] files;
        protected Integer recordPerFile;
        protected Integer recordSize;
        protected KeyPool keys;
        protected String fileType;
        protected String fileNamePrefix;
        protected String workingDirectory;

        protected MasterData(
                ArrayList<String> files,
                Integer recordPerFile,
                Integer recordSize,
                KeyPool keys,
                String fileType,
                String fileNamePrefix,
                String workingDirectory)
        {
            this.files = (String[])files.toArray();
            this.recordPerFile = recordPerFile;
            this.recordSize = recordSize;
            this.keys = keys;
            this.fileType = fileType;
            this.fileNamePrefix = fileNamePrefix;
            this.workingDirectory = workingDirectory;
        }
    }

    protected class OrderedPaginationImplement implements DataLock
    {
        private Lock[] locks;

        public OrderedPaginationImplement(Lock[] locks)
        {
            if (locks == null)
            {
                throw new NullPointerException("locks");
            }
            this.locks = locks;
        }

        public void unlock()
        {
            for (Lock lock : locks)
            {
                lock.unlock();
            }
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="instance fields">
    private final Object ENSURE_FILE_LOCK;

    private final Object FILES_LOCK;

    private ArrayList<String> fileNames;

    private Map<String, OrderedFile> openFiles;

    private KeyPool keys;

    private OrderedFiles fileType;

    private Integer recordPerFile;

    private Integer recordByteSize;

    private String masterFile;

    private String fileNamePrefix;

    private String workingDirectory;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public OrderedPagination()">
    public ThreadSafeOrderedPagination()
    {
        ENSURE_FILE_LOCK= new Object();
        FILES_LOCK      = new Object();
        fileNames       = null;
        openFiles       = null;

        keys                = null;
        fileType            = null;
        recordPerFile       = null;
        recordByteSize      = null;
        masterFile          = null;
        fileNamePrefix      = null;
        workingDirectory    = null;
    }
    // </editor-fold>

    @Override
    public void create(
            String indexerName, 
            String workingDir, 
            String namingPrefix, 
            int recordPerFile, 
            int recordByteSize) 
            throws DataOrderingException 
    {
        
    }


    
    
    // <editor-fold defaultstate="collapsed" desc="public void create(String master, Map<String, Object> data)">
    public void create(String master, Map<String, Object> data)
            throws DataOrderingException
    {
        if (master == null)
        {
            throw new NullPointerException("master");
        }
        if (data == null)
        {
            throw new NullPointerException("data");
        }
        if (master.isEmpty())
        {
            throw new IllegalArgumentException(
                    "master must not be empty");
        }
        if (!data.containsKey(DataOrderings.FILE_TYPE))
        {
            throw new IllegalArgumentException(
                    "file type must be included in data");
        }
        if (!data.containsKey(DataOrderings.FILE_NAMING_PREFIX))
        {
            throw new IllegalArgumentException(
                    "file naming template must be included in data");
        }
        if (!data.containsKey(DataOrderings.RECORD_PER_FILE))
        {
            throw new IllegalArgumentException(
                    "records per file must be included in data");
        }
        if (!data.containsKey(DataOrderings.RECORD_BYTE_SIZE))
        {
            throw new IllegalArgumentException(
                    "record byte size must be included in data");
        }
        if (!data.containsKey(DataOrderings.WORKING_DIRECTORY))
        {
            throw new IllegalArgumentException(
                    "working directory must be included in data");
        }
        try
        {
            this.masterFile         = master;
            this.fileType           = OrderedFiles.valueOf((String) data.get(DataOrderings.FILE_TYPE));
            this.fileNamePrefix     = (String) data.get(DataOrderings.FILE_NAMING_PREFIX);
            this.workingDirectory   = (String) data.get(DataOrderings.WORKING_DIRECTORY);
            this.recordPerFile      = (Integer) data.get(DataOrderings.RECORD_PER_FILE);
            this.recordByteSize     = (Integer) data.get(DataOrderings.RECORD_BYTE_SIZE);

            keys            = new KeyPool();
            fileNames       = new ArrayList<>();
            openFiles       = new HashMap<>();

            (new FileSerializer()).serialize(
                    masterFile,
                    new MasterData(
                        fileNames,
                        recordPerFile,
                        recordByteSize,
                        keys,
                        fileType.name(),
                        fileNamePrefix,
                        workingDirectory));
        }
        catch(Exception ex)
        {
            throw new DataOrderingException(
                    "an error occurred while creating the pagination",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void init(String master)">
    public void init(String master)
            throws  DataOrderingException
    {
        try
        {
            MasterData data = (MasterData)
                    (new FileSerializer()).unserialize(masterFile);

            fileNamePrefix      = data.fileNamePrefix;
            workingDirectory    = data.workingDirectory;
            fileType            = OrderedFiles.valueOf(data.fileType);
            recordPerFile       = data.recordPerFile;
            recordByteSize      = data.recordSize;
            fileNames           = new ArrayList<String>(data.files.length);

            for (int i = 0; i < data.files.length; i++)
            {
                if (!(new File(data.files[i]).exists()))
                {
                    throw new CorruptedDataException(
                            data.files[i] + " does not exist");
                }
                fileNames.add(data.files[i]);
            }
        }
        catch(Exception ex)
        {
            throw new DataOrderingException(
                    "an error occurred while initiated the pagination",
                    ex);
        }
    }
    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="public String getMaster()">
    @Override
    public String getMaster()
    {
        return masterFile;
    }
    //</editor-fold>
    

    // <editor-fold defaultstate="collapsed" desc="public void close()">
    public void close()
            throws DataOrderingException
    {
        try
        {
            ArrayList<String> _files = null;
            synchronized(FILES_LOCK)
            {
                // if either of these are null, then that means
                // that they file system is closed either by
                // it never being initailized or by it already
                // being closed
                if (fileNames == null)
                {
                    throw new DataOrderingException("file system is closed");
                }
                if (openFiles == null)
                {
                    throw new DataOrderingException("file system is closed");
                }

                // 'close' this part of the system and give it another
                // reference so it can be saved later
                _files = fileNames;
                fileNames = null;

                // iterate over all the open files and close them
                Iterator<String> it = openFiles.keySet().iterator();
                while(it.hasNext())
                {
                    String filename = it.next();
                    openFiles.get(filename).close();
                }
                openFiles = null;
            }
            (new FileSerializer()).serialize(
                    masterFile,
                    new MasterData(
                        _files,
                        recordPerFile,
                        recordByteSize,
                        keys,
                        fileType.name(),
                        fileNamePrefix,
                        workingDirectory));
        }
        catch(DataOrderingException | OrderedFileException | IOException ex)
        {
            throw new DataOrderingException(
                    "an error occurred while closing the pagination",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void ensureOpenFiles()">
    public void ensureOpenFiles()
            throws DataOrderingException
    {
        synchronized(ENSURE_FILE_LOCK)
        {
            Iterator<String> it = null;
            synchronized(FILES_LOCK)
            {
                if (openFiles == null)
                {
                    throw new DataOrderingException("file system is closed");
                }
                it = openFiles.keySet().iterator();
            }

            while(it.hasNext())
            {
                String filename = it.next();
                // leaving and entering the synchronized zone allows
                // work to still continue when ensuring files
                synchronized(FILES_LOCK)
                {
                    if (openFiles == null)
                    {
                        throw new DataOrderingException("file system is closed");
                    }
                    OrderedFile file = openFiles.get(filename);
                    try
                    {
                        if (!file.isUsed())
                        {
                            file.close();
                            openFiles.remove(filename);
                        }
                    }
                    catch(OrderedFileException ex){ }
                }
            }
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public ByteBuffer read(int location)">
    public ByteBuffer read(int location)
            throws  DataOrderingException
    {
        if (!keys.isUsed(location))
        {
            throw new IllegalArgumentException("location is out of range");
        }
        try
        {
            return safeFileGet(getFileNumber(location)).
                    read(getRawRecordStart(location), recordByteSize);
        }
        catch(DataOrderingException | OrderedFileException ex)
        {
            throw new DataOrderingException(
                    "an error occurred while reading data", ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void write(int location, ByteBuffer data)">
    public void write(int location, ByteBuffer data)
            throws  DataOrderingException
    {
        if (data.capacity() != recordByteSize)
        {
            throw new IllegalArgumentException("invalid size of data");
        }
        try
        {
            keys.forceKeyUsage(location);
            safeFileGet(getFileNumber(location)).
                    write(getRawRecordStart(location), data);
        }
        catch(DataOrderingException | OrderedFileException ex)
        {
            throw new DataOrderingException(
                    "an error occurred while writing data", ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public DataLock lockSection(int start, int amount)">
    public DataLock lockSection(int start, int amount)
            throws  DataOrderingException
    {
        if (start < 1)
        {
            throw new IllegalArgumentException("start cannot be negative");
        }
        if (amount < 1)
        {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        if (keys.getMax() > start + amount)
        {
            throw new IllegalArgumentException("locking out of bounds");
        }
        try
        {
            return null;
        }
        catch(Exception ex)
        {
            throw new DataOrderingException(
                    "an error occurred while inserting data", ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void release(int location)">
    public void release(int location)
            throws  DataOrderingException
    {
        keys.unsetKey(location);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="private OrderedFile createFile(int fileNumber)">
    private OrderedFile createFile(int fileNumber)
            throws DataOrderingException, OrderedFileException
    {
        OrderedFile result = null;
        synchronized(FILES_LOCK)
        {
            if (fileNames == null)
            {
                throw new DataOrderingException("file system is closed");
            }
            String filename = concateFileName(fileNumber);
            result = fileType.getNewInstance();
            result.create(filename);
            fileNames.add(filename);
        }
        return result;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="private OrderedFile safeFileGet(int fileNumber)">
    private OrderedFile safeFileGet(int fileNumber)
            throws DataOrderingException, OrderedFileException
    {
        OrderedFile result = null;
        synchronized (FILES_LOCK)
        {
            String filename = concateFileName(fileNumber);
            if (!fileNames.contains(filename))
            {
                result = createFile(fileNumber);
                openFiles.put(filename, result);
            }
            else
            {
                if (openFiles.containsKey(filename))
                {
                    result = openFiles.get(filename);
                }
                else
                {
                    result = fileType.getNewInstance();
                    result.init(filename);
                    openFiles.put(filename, result);
                }
            }
        }
        return result;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="private String concateFileName(int fileNumber)">
    private String concateFileName(int fileNumber)
    {
        return workingDirectory + "/"
                + fileNamePrefix + fileNumber + "." + OrderedFile.EXTENSION;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="record finder helpers">
    private int getFileNumber(int location)
    {
        return location / recordPerFile;
    }

    private int getRawRecordStart(int location)
    {
        return (location % recordPerFile) * recordByteSize;
    }
    // </editor-fold>

}
