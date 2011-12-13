/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.tree.TreeFactory;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.transaction.TransactionType;
import com.rf.fled.persistence.transaction.TransactionFactory;
import com.rf.fled.persistence.filemanager.FileManagerFactory;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import java.util.Map;

/**
 * this is the root of this helper package. 
 * @author REx
 */
public class Provider 
{
//    public static Persistence loadPersistence(
//            String directory, 
//            String treeName, 
//            RecordCacheType recordCacheType)
//            throws FledPersistenceException
//    {
//        
//    }
    
    public static Persistence createPersistence(
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
        FileManagerContext fContext = new FileManagerContext();
        fContext.cacheType  = fileCacheType;
        fContext.directory  = directory;
        fContext.context    = treeName;
        fContext.managerType= fileManagerType;
        fContext.counter    = 0l;
        FileManager fileManager = FileManagerFactory.create(fContext, hints);
        
        Persistence persistence = TreeFactory.create(
                persistenceType, 
                recordCacheType, 
                fileManager, 
                fContext,
                hints);
        
        Persistence result = TransactionFactory.create(
                persistence, 
                fileManager,
                transaction);
        
        return result;
    }
}
