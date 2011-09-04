/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation.sets;

import com.rf.dcore.operation.KeySetOperation;
import com.rf.dcore.util.record.IndexedRecord;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author REx
 */
public class KeySetOr implements KeySetOperation
{
    @Override
    public  IndexedRecord[] exec(
            IndexedRecord[] left, 
            IndexedRecord[] right) 
    {
        IndexedRecord[] must = null;
        IndexedRecord[] checking = null;
        
        if (left.length > right.length)
        {
            must = left;
            checking = right;
        }
        else
        {
            must = right;
            checking = left;
        }
        
        ArrayList<IndexedRecord> buffer = new ArrayList<>(checking.length);
        for (IndexedRecord check : checking)
        {
            if (Arrays.binarySearch(must, check) < 0)
            {
                buffer.add(check);
            }
        }
        
        IndexedRecord[] bufferedCheck = (IndexedRecord[])buffer.toArray();
        IndexedRecord[] result = new IndexedRecord[must.length + bufferedCheck.length];
        
        System.arraycopy(must, 0, result, 0, must.length);
        System.arraycopy(bufferedCheck, 0, result, must.length, bufferedCheck.length);
        
        Arrays.sort(result);
        
        return result;
    }
}
