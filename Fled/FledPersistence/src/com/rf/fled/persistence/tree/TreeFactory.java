/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import com.rf.fled.persistence.ProviderHint;
import java.util.Map;

/**
 *
 * @author REx
 */
public class TreeFactory 
{
    public static Persistence create(
            TreeType type,
            RecordCacheType cacheType,
            FileManager manager, 
            FileManagerContext managerContext,
            Map<String, Object> hints) 
            throws FledPersistenceException
    {
        Persistence result = null;
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
                Serializer<byte[]> valueSerializer = null;
                if (hints.containsKey(ProviderHint.VALUE_SERIALIZER.name()))
                {
                    valueSerializer = (Serializer<byte[]>) 
                            hints.get(ProviderHint.VALUE_SERIALIZER.name());
                }
                // null is default
                Serializer<byte[]> pageSerializer = null;
                if (hints.containsKey(ProviderHint.PAGE_SERIALIZER.name()))
                {
                    pageSerializer = (Serializer<byte[]>) 
                            hints.get(ProviderHint.PAGE_SERIALIZER.name());
                }
                result = BPlusTree.createBPlusTree(
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
    
    public static Persistence cache(Persistence persistence, RecordCacheType cacheType)
            throws FledPersistenceException
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
