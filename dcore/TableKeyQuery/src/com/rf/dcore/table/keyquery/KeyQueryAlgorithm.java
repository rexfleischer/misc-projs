/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.dcore.table.keyquery;

import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.table.keyquery.exceptions.IllegalOperationException;
import com.rf.dcore.table.keyquery.exceptions.IndexerNotFoundException;
import com.rf.dcore.table.query.logictree.CommandNode;
import com.rf.dcore.util.locks.DataLock;
import java.util.Map;

/**
 * 
 * @author REx
 */
public interface KeyQueryAlgorithm<_Ry>
{
    /**
     * 
     * @param query
     * @param columns
     * @param lock
     * @return 
     */
    public _Ry queryForKeys(
            CommandNode[] query, 
            Map<String, TableIndexer> indexers, 
            DataLock lock);
    
    /**
     * 
     * @param query
     * @param columns
     * @return 
     */
    public _Ry queryForKeys(
            CommandNode[] query, 
            Map<String, TableIndexer> indexers)
            throws  IllegalOperationException, 
                    IndexerNotFoundException;
}
