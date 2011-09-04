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
public class KeySetAnd implements KeySetOperation
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
            if (Arrays.binarySearch(must, check) >= 0)
            {
                buffer.add(check);
            }
        }
        
        IndexedRecord[] result = (IndexedRecord[])buffer.toArray();
        Arrays.sort(result);
        return result;
    }
}
