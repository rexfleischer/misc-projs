/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.indexer.impls;

import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.util.locks.DataLock;
import com.rf.dcore.util.record.IndexedRecord;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public class OrderedIndex implements TableIndexer
{
    

    @Override
    public void create(String name, String workingDir, int indexesPerFile) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init(String master) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMaster() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void ensureDensity(int percent, int tolerance) 
    {
        
    }

    @Override
    public DataLock readyLocking() 
    {
        return new DataLock() {

            @Override
            public void unlock() {
                
            }
            
        };
    }

    @Override
    public void delete(IndexedRecord data) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long comparator, Integer key)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(Long comparator, Integer key) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexedRecord[] select_Equal(
            ArrayList<Long> comparators, 
            DataLock lock) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexedRecord[] select_GreaterThan(
            Long comparator, 
            DataLock lock)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexedRecord[] select_LessThan(
            Long comparator, 
            DataLock lock) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexedRecord[] select_NotEqual(
            ArrayList<Long> comparators, 
            DataLock lock) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexedRecord[] select_Range(
            Long comparatorLeast, 
            Long comparatorMost, 
            DataLock lock) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
