/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.persistence.filemanager.IFileManager;
import com.rf.fled.persistence.tree.TreeFactory;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.transaction.TransactionType;
import com.rf.fled.persistence.transaction.TransactionFactory;
import com.rf.fled.persistence.filemanager.FileManagerFactory;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * this is the root of this helper package. 
 * @author REx
 */
public class Provider 
{
    public static IPersistence loadPersistence(
            String treepath,
            RecordCacheType recordCacheType,
            TransactionType transaction)
            throws  FledPersistenceException, 
                    FileNotFoundException, 
                    ClassNotFoundException, 
                    IOException
    {
        /**
         * load from the tree factory
         */
        IPersistence persistence = TreeFactory.load(treepath, recordCacheType);
        
        /**
         * wrap with the transaction factory
         */
        IPersistence result = TransactionFactory.create(persistence, transaction);
        
        return result;
    }
    
    public static IPersistence createPersistence(
            String directory,
            String treeName,
            TreeType persistenceType,
            RecordCacheType recordCacheType,
            FileManagerType fileManagerType,
            FileManagerCacheType fileCacheType,
            TransactionType transaction,
            Map<String, Object> hints)
            throws FledPersistenceException
    {
        /**
         * build the FileManager context object
         */
        FileManagerContext fContext = new FileManagerContext();
        fContext.cacheType  = fileCacheType;
        fContext.directory  = directory;
        fContext.managerType= fileManagerType;
        fContext.context    = treeName;
        fContext.counter    = 0l;
        IFileManager fileManager = FileManagerFactory.create(fContext, hints);
        
        /**
         * initial create of persistence implementation. this should save
         * itself in the FileManager, be it an in memory file manager or
         * on the file system or whatever.
         */
        IPersistence persistence = TreeFactory.create(
                treeName,
                persistenceType, 
                recordCacheType, 
                fileManager, 
                fContext,
                hints);
        
        /**
         * wrap with transaction object
         */
        IPersistence result = TransactionFactory.create(
                persistence, 
                transaction);
        
        return result;
    }
}
