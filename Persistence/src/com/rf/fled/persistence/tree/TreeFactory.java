/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.filemanager.IFileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import com.rf.fled.persistence.ProviderHint;
import com.rf.fled.persistence.util.BasicStreamIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author REx
 */
public class TreeFactory 
{
    public static IPersistence create(
            String treeName,
            TreeType type,
            RecordCacheType cacheType,
            IFileManager manager, 
            FileManagerContext managerContext,
            Map<String, Object> hints) 
            throws FledPersistenceException 
    {
        IPersistence result = null;
        switch(type)
        {
            case BPLUSTREE:
                // 64 is default
                int recordsPerPage = 64;
                if (hints.containsKey(ProviderHint.RECORDS_PER_PAGE.name()))
                {
                    recordsPerPage = (Integer) 
                            hints.get(ProviderHint.RECORDS_PER_PAGE.name());
                }
                // null is default
                ISerializer<byte[]> valueSerializer = null;
                if (hints.containsKey(ProviderHint.VALUE_SERIALIZER.name()))
                {
                    valueSerializer = (ISerializer<byte[]>) 
                            hints.get(ProviderHint.VALUE_SERIALIZER.name());
                }
                // null is default
                ISerializer<byte[]> pageSerializer = null;
                if (hints.containsKey(ProviderHint.PAGE_SERIALIZER.name()))
                {
                    pageSerializer = (ISerializer<byte[]>) 
                            hints.get(ProviderHint.PAGE_SERIALIZER.name());
                }
                result = BPlusTree.createBPlusTree(
                        treeName,
                        recordsPerPage, 
                        manager, 
                        managerContext, 
                        valueSerializer, 
                        pageSerializer);
                break;
            default:
                throw new IllegalArgumentException("type");
        }
        
        return cache(result, cacheType);
    }
    
    public static IPersistence load(
            String filepath, 
            RecordCacheType cacheType)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream input = new FileInputStream(filepath);
        Object object = BasicStreamIO.streamToObject(input);
        if (!(object instanceof IPersistence))
        {
            throw new IllegalArgumentException(
                    "object from file " + filepath + 
                    " must be and instance of Persistence");
        }
        IPersistence persistence = (IPersistence) object;
        return cache(persistence, cacheType);
    }
    
    public static IPersistence cache(
            IPersistence persistence, 
            RecordCacheType cacheType)
    {
        switch(cacheType)
        {
            case IN_MEMORY:
                return new RecordCache_InMemory(persistence);
            case NONE:
                return persistence;
            default:
                throw new IllegalArgumentException("cacheType");
        }
    }
}
