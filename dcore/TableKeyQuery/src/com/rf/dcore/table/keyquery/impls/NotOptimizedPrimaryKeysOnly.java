
package com.rf.dcore.table.keyquery.impls;

import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.table.keyquery.KeyQueryAlgorithm;
import com.rf.dcore.table.keyquery.exceptions.IllegalOperationException;
import com.rf.dcore.table.keyquery.exceptions.IndexerNotFoundException;
import com.rf.dcore.table.query.commands.ComparatorCommand;
import com.rf.dcore.table.query.commands.KeySetCommand;
import com.rf.dcore.table.query.logictree.CommandNode;
import com.rf.dcore.util.locks.DataLock;
import com.rf.dcore.util.record.IndexedRecord;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class NotOptimizedPrimaryKeysOnly implements KeyQueryAlgorithm<int[]>
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
        LinkedList<IndexedRecord[]> stack = new LinkedList<>();
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
                        ArrayList<Long> value1 = (ArrayList<Long>)comparator.value;
                        thisResult = indexers.get(comparator.columnName)
                                .select_NotEqual(value1, null);
                        break;
                    case Equal:
                        ArrayList<Long> value2 = (ArrayList<Long>)comparator.value;
                        thisResult = indexers.get(comparator.columnName)
                                .select_Equal(value2, null);
                        break;
                    case Range:
                        Long[] value3 = (Long[])comparator.value;
                        thisResult = indexers.get(comparator.columnName)
                                .select_Range(value3[0], value3[1], null);
                        break;
                    case GreaterThan:
                        Long value4 = (Long)comparator.value;
                        thisResult = indexers.get(comparator.columnName)
                                .select_GreaterThan(value4, null);
                        break;
                    case LessThan:
                        Long value5 = (Long)comparator.value;
                        thisResult = indexers.get(comparator.columnName)
                                .select_LessThan(value5, null);
                        break;
                    default:
                        throw new IllegalOperationException("2");
                }
                stack.addFirst(thisResult);
            }
            else if (command.isKeySetOperation())
            {
                KeySetCommand operator = command.getAsKeySetOperator();
                IndexedRecord[] left    = stack.pollFirst();
                IndexedRecord[] right   = stack.pollFirst();
                
                IndexedRecord[] result = operator.operation.exec(left, right);
                stack.addFirst(result);
            }
            else
            {
                throw new IllegalOperationException("1");
            }
        }
        
        if (stack.size() != 1)
        {
            throw new IllegalOperationException("3");
        }
        
        IndexedRecord[] finalRecords = stack.pollFirst();
        
        int count = finalRecords.length;
        int[] result = new int[count];
        
        for(int i = 0; i < count; i++)
        {
            result[i] = finalRecords[i].key;
        }
        
        return result;
    }
}
