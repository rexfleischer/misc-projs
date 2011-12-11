/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.persistence.filemanager.FileManagerUpdate;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.bplustree.BPlusTree;
import com.rf.fled.persistence.fileio.ByteSerializer;
import com.rf.fled.persistence.filemanager.FileManager_FileSystemNoTree;
import com.rf.fled.persistence.filemanager.FileManager_InMemory;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author REx
 */
public class FledPersistenceProvider implements Externalizable
{
    public enum DATA_STRUCTURE_TYPE
    {
        /**
         * the type of persistence that will be used
         */
        BPLUSTREE,
    }
    
    public enum RECORD_CACHE
    {
        /**
         * for no caching
         */
        NONE,
    }
    
    public enum FILE_MANAGER
    {
        /**
         * the basic file manager, this manages only one directory
         */
        FILE_SYSTEM_NO_TREE,
        
        /**
         * a manager that is only in memory. this is always empty and
         * is never persisted to disk
         */
        MEMORY,
    }
    
    public enum FILE_CACHE
    {
        /**
         * cache files in memory in a soft hash map
         */
        MEMORY,
        
        /**
         * for no caching
         */
        NONE,
    }
    
    public enum HINT_KEYS
    {
        /**
         * for bplustrees
         */
        RECORDS_PER_PAGE,
        
        /**
         * for custom value serializers
         */
        VALUE_SERIALIZER,
        
        /**
         * for custom page serializers
         */
        PAGE_SERIALIZER,
        
    }
    
    private class PersistenceData implements Serializable
    {
        String context;
        String name;
        DATA_STRUCTURE_TYPE dataStructureType;
        RECORD_CACHE recordCache;
        boolean transactional;
        boolean temporary;
        Map<String, Object> hints;
    }
    
    private class FileManagerData implements Serializable
    {
        String context;
        FILE_MANAGER file;
        FILE_CACHE fileCache;
        long recordCountAt;
        boolean temporary;
        Map<String, Object> hints;
    }
    
    public static final String FILE_NAME = "provider.db";
    
    public static final DATA_STRUCTURE_TYPE DEFAULT_TYPE = DATA_STRUCTURE_TYPE.BPLUSTREE;
    
    public static final FILE_CACHE DEFAULT_FILE_CACHE = FILE_CACHE.MEMORY;
    
    public static final RECORD_CACHE DEFAULT_RECORD_CACHE = RECORD_CACHE.NONE;
    
    public static final FILE_MANAGER DEFAULT_FILE_MANAGER = FILE_MANAGER.FILE_SYSTEM_NO_TREE;
    
    public static void saveProvider(FledPersistenceProvider provider) 
            throws FledPersistenceException, IOException
    {
        byte[] bytes = ByteSerializer.serialize(provider);
        File location = new File(provider.directory + "/" + FILE_NAME);
        
        FileOutputStream stream = new FileOutputStream(location);
        stream.write(bytes);
        stream.close();
    }
    
    public static FledPersistenceProvider createProviderForDirectory(String directory)
            throws FledPersistenceException, IOException
    {
        File location = new File(directory + "/" + FILE_NAME);
        if (location.exists())
        {
            throw new FledPersistenceException(
                    directory + " " + LanguageStatements.DIRECTORY_ALREADY_IN_USE.toString());
        }
        
        FledPersistenceProvider result = new FledPersistenceProvider();
        result.directory            = directory;
        result.fileManagerData   = new HashMap<String, FileManagerData>();
        result.openFileManagers     = new HashMap<String, FileManager>();
        result.persistenceData      = new HashMap<String, PersistenceData>();
        
        saveProvider(result);
        return result;
    }
    
    public static FledPersistenceProvider getProviderForDirectory(String directory)
            throws FledPersistenceException, IOException
    {
        File location = new File(directory + "/" + FILE_NAME);
        if (!location.exists())
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString());
        }
        
        FileInputStream stream = new FileInputStream(location);
        byte[] data = new byte[(int) location.length()];
        stream.read(data);
        stream.close();
        
        FledPersistenceProvider result = null;
        
        try
        {
            result = (FledPersistenceProvider) ByteSerializer.deserialize(data);
        }
        catch(ClassNotFoundException ex) 
        {
            // this really shouldnt happen...
            // @TODO
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
        
        result.directory = directory;
        
        return result;
    }
    
    /**
     * the root directory. this provider needs to have complete control
     * over the file structure from here.
     */
    private String directory;
    
    /**
     * all open file managers are here. temporary and non-temporary.
     */
    private HashMap<String, FileManager> openFileManagers;
    
    /**
     * all the open persistences. temporary and non-temporary.
     */
    private HashMap<String, Persistence> openPersistences;
    
    /**
     * this is where all the persistence contexts are saved. 
     */
    private HashMap<String, PersistenceData> persistenceData;
    
    /**
     * theses are the file managers that come with FILE_SYSTEM_NO_TREE flag
     * are saved and kept track of. 
     */
    private HashMap<String, FileManagerData> fileManagerData;
    
    private Observer FileManagerObserver = new Observer() 
    {
        @Override
        public void update(Observable o, Object arg) 
        {
            if (!(arg instanceof FileManagerUpdate))
            {
                return;
            }
            FileManagerUpdate update = (FileManagerUpdate) arg;
            if (o instanceof FileManager_FileSystemNoTree)
            {
                switch(update.updateType)
                {
                    case RECORD_COUNT_AT:
                        FileManagerData data = fileManagerData.get(update.context);
                        if (data == null)
                        {
                            throw new IllegalArgumentException(
                                    "invalid update.context");
                        }
                        data.recordCountAt = (Long) update.info;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "invalid update.updateType");
                }
            }
            else if (o instanceof FileManager_InMemory)
            {
                // nothing to update
            }
        }
    };
    
    private FledPersistenceProvider(){ }
    
    public synchronized Persistence loadPersistence(String context)
    {
        Persistence result = null;
        
        return result;
    }
    
    /**
     * 
     * @param context
     * @param file
     * @param fileCache
     * @param temporary
     * @param hints 
     */
    public void createFileManager(
            String context, 
            FILE_MANAGER fileManager,
            FILE_CACHE fileCache,
            boolean temporary,
            Map<String, Object> hints)
            throws FledPersistenceException
    {
        if (fileManagerData.containsKey(context))
        {
            throw new FledPersistenceException(context + " " + 
                    LanguageStatements.CONTEXT_ALREADY_EXISTS.toString());
        }
        
        if (fileCache != FILE_CACHE.MEMORY &&
            fileCache != FILE_CACHE.NONE)
        {
            throw new IllegalArgumentException("fileCache");
        }
        
        if (fileManager != FILE_MANAGER.MEMORY &&
            fileManager != FILE_MANAGER.FILE_SYSTEM_NO_TREE)
        {
            throw new IllegalArgumentException("fileManager");
        }
        
        FileManagerData manager = new FileManagerData();
        manager.context         = context;
        manager.recordCountAt   = 0;
        manager.temporary       = temporary;
        manager.file            = fileManager;
        manager.fileCache       = fileCache;
        manager.hints           = hints;
        
        fileManagerData.put(context, manager);
    }
    
    /**
     * 
     * @param context
     * @param name
     * @param type
     * @param recordCache
     * @param transactional
     * @param temporary
     * @param hints
     * @return 
     */
    public void createPersistance(
            String context, 
            String name,
            DATA_STRUCTURE_TYPE dataStructureType, 
            RECORD_CACHE recordCache,
            boolean transactional,
            boolean temporary,
            Map<String, Object> hints)
            throws FledPersistenceException
    {
        if (context == null || context.isEmpty())
        {
            throw new IllegalArgumentException("context");
        }
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("name");
        }
        
        if (!fileManagerData.containsKey(context))
        {
            throw new FledPersistenceException(context + " " + 
                    LanguageStatements.CONTEXT_DOES_NOT_EXISTS.toString());
        }
        
        if (dataStructureType != DATA_STRUCTURE_TYPE.BPLUSTREE)
        {
            throw new IllegalArgumentException("type");
        }
        
        if (recordCache != RECORD_CACHE.NONE)
        {
            throw new IllegalArgumentException("recordCache");
        }
        
        PersistenceData persistence = new PersistenceData();
        persistence.context         = context;
        persistence.name            = name;
        persistence.dataStructureType= dataStructureType;
        persistence.recordCache     = recordCache;
        persistence.temporary       = temporary;
        persistence.transactional   = transactional;
        persistence.hints           = hints;
        
        persistenceData.put(context + name, persistence);
    }
    
    /**
     * 
     * @param context
     * @param name
     * @param type
     * @param file
     * @param recordCache
     * @param fileCache
     * @param transactional
     * @param temporary
     * @param hints
     * @return
     * @throws FledPresistanceException 
     */
    public synchronized Persistence create(
            String name,
            DATA_STRUCTURE_TYPE type, 
            RECORD_CACHE recordCache,
            boolean transactional,
            boolean temporary,
            Map<String, Object> hints) throws FledPersistenceException
    {
        
        Persistence result = null;
        
        switch(type)
        {
            case BPLUSTREE:
                // 64 is default
                int recordsPerPage = 64;
                if (hints.containsKey(HINT_KEYS.RECORDS_PER_PAGE.name()))
                {
                    recordsPerPage = (Integer) 
                            hints.get(HINT_KEYS.RECORDS_PER_PAGE.name());
                }
                // null is default
                Serializer<byte[]> valueSerializer = null;
                if (hints.containsKey(HINT_KEYS.VALUE_SERIALIZER.name()))
                {
                    valueSerializer = (Serializer<byte[]>) 
                            hints.get(HINT_KEYS.VALUE_SERIALIZER.name());
                }
                // null is default
                Serializer<byte[]> pageSerializer = null;
                if (hints.containsKey(HINT_KEYS.PAGE_SERIALIZER.name()))
                {
                    pageSerializer = (Serializer<byte[]>) 
                            hints.get(HINT_KEYS.PAGE_SERIALIZER.name());
                }
                result = BPlusTree.createBPlusTree(
                        null, directory, 
                        recordsPerPage, 
                        valueSerializer, 
                        pageSerializer);
                break;
            default:
                throw new IllegalArgumentException("type");
        }
        
        return result;
    }

    @Override
    public void writeExternal(ObjectOutput out) 
            throws IOException 
    {
        out.writeObject(persistenceData);
        out.writeObject(fileManagerData);
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws IOException, ClassNotFoundException 
    {
        persistenceData = (HashMap<String, PersistenceData>) in.readObject();
        fileManagerData = (HashMap<String, FileManagerData>) in.readObject();
    }
}
