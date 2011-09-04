
package com.rf.dcore.table.keyquery.impls;

import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.table.keyquery.KeyQueryAlgorithm;
import com.rf.dcore.table.keyquery.exceptions.IllegalOperationException;
import com.rf.dcore.table.keyquery.exceptions.IndexerNotFoundException;
import com.rf.dcore.table.query.commands.ComparatorCommand;
import com.rf.dcore.table.query.logictree.CommandNode;
import com.rf.dcore.util.locks.DataLock;
import com.rf.dcore.util.record.IndexedRecord;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class PrimaryKeysOnly implements KeyQueryAlgorithm<int[]>
{

    @Override
    public int[] queryForKeys(
            CommandNode[] query, 
            Map<String, TableIndexer> indexers, 
            DataLock lock) 
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] queryForKeys(
            CommandNode[] query, 
            Map<String, TableIndexer> indexers) 
            throws  IllegalOperationException,
                    IndexerNotFoundException
    {
        LinkedList<Object> stack = new LinkedList<>();
        for(CommandNode command : query)
        {
            if (command.isComparator())
            {
                ComparatorCommand comparator = command.getAsComparator();
                
                if (!indexers.containsKey(comparator.columnName))
                {
                    throw new IndexerNotFoundException("1");
                }
                
                IndexedRecord[] thisResult = null;
                
                switch(comparator.operation)
                {
                    case NotEqual:
                        indexers.get(comparator.columnName).select_NotEqual(null, null);
                        break;
                    case Equal:
                        
                        break;
                    case Range:
                        
                        break;
                    case GreaterThan:
                        
                        break;
                    case LessThan:
                        
                        break;
                    default:
                        throw new IllegalOperationException("2");
                }
            }
            else if (command.isKeySetOperation())
            {
                
            }
            else
            {
                throw new IllegalOperationException("1");
            }
        }
        
        int[] result = null;
        return result;
    }
}
