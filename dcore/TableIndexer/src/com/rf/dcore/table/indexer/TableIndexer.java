/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.indexer;

import com.rf.dcore.util.record.IndexedRecord;
import com.rf.dcore.util.locks.DataLock;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface TableIndexer
{
    public static final String EXTENSION = "indexer";

    /**
     * 
     * @param name
     * @param workingDir
     * @param indexesPerFile 
     */
    public void create(String name, String workingDir, int indexesPerFile);

    /**
     * 
     * @param master 
     */
    public void init(String master);

    /**
     * returns the master file for this indexer
     * @return 
     */
    public String getMaster();

    /**
     * close the table from being able to be queried.
     */
    public void close();
    
    /**
     * returns the locking object that is handed into the select statements.
     * 
     * NOTE: after you are finished with the lock, make sure to call unlock()
     * on the lock object
     * 
     * @return 
     */
    public DataLock readyLocking();

    /**
     * 
     * @param comparators
     * @param lock
     * @return 
     */
    public IndexedRecord[] select_NotEqual(ArrayList<Long> comparators, DataLock lock);

    /**
     * 
     * @param comparators
     * @param lock
     * @return 
     */
    public IndexedRecord[] select_Equal(ArrayList<Long> comparators, DataLock lock);

    /**
     * 
     * @param comparatorLeast
     * @param comparatorMost
     * @param lock
     * @return 
     */
    public IndexedRecord[] select_Range(Long comparatorLeast, Long comparatorMost, DataLock lock);

    /**
     * 
     * @param comparator
     * @param lock
     * @return 
     */
    public IndexedRecord[] select_GreaterThan(Long comparator, DataLock lock);

    /**
     * 
     * @param comparator
     * @param lock
     * @return 
     */
    public IndexedRecord[] select_LessThan(Long comparator, DataLock lock);
    
    /**
     * deletes the record that has the given information from IndexedRecord 
     * 
     * NOTE: do not create an IndexedRecord and put in here. it will most likely
     * corrupt the data in some way.
     * @param data 
     */
    public void delete(IndexedRecord data);
    
    /**
     * deletes a record without having to do a select first. 
     * 
     * @param comparator
     * @param key 
     */
    public void delete(Long comparator, Integer key);

    /**
     * 
     * @param comparator the numerical value of the piece of data related to
     * the record that is inserting.
     * @param key the key to the record that referenced
     */
    public void insert(Long comparator, Integer key);

    /**
     * this makes sure that some standard unit is as full as percent..
     * for example. if the standard until is 100 records and the density is 80 
     * with tolerance being 5, then for every iteration of 100, there must be
     * between 75 and 85 records. if that is not the case, then the records
     * will be reordered.
     * 
     * NOTE: this takes a very long time and locks the table before starting. 
     * @param percent a value between 1 and 100
     * @param tolerance the amount of deviation from percent
     * 
     * @throws IllegalArgumentException 
     * if percent < 1 || percent > 100
     * 
     * @throws IllegalArgumentException 
     * if (percent - tolerance) < 1 || (percent + tolerance) > 100
     */
    public void ensureDensity(int percent, int tolerance);
}
