/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.presistance.Serializer;
import java.io.File;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public class BPlusPage implements Serializer
{
    /**
     * the page manager for opening files
     */
    private BPlusPageManager manager;
    
    /**
     * the id that represents this page from the page manager. if you ask
     * the page manager for this id, then it will return this instance.
     */
    private long thisId;
    public long getThisBuckedId() { return thisId; }
    
    /**
     * this tells the page if it is a leaf or not. mostly for internal class
     * use with serialization
     */
    private boolean isLeaf;
    public boolean isLeaf() { return isLeaf; }
    
    /**
     * every file has a parent except the root. that has an id of -1
     */
    private long parentId;
    public long getParentBucketId() { return parentId; }
    public void setParentBucketId(long id) { parentId = id; }
    
    /**
     * sense this is a bplustree, then at the leaf nodes there are next
     * and previous pages for browsing and such.
     * 
     * NOTE: depending on how this implementation turns out, the two 
     * abstractions of the base page may be pulled out if it has
     * a performance boost.
     */
    private long nextId;
    private long prevId;
    public long getNextBucketId() { return nextId; }
    public long getPreviousBucketId() { return prevId; }
    public void setNextBucketId(long id) { nextId = id; }
    public void setPreviousBucketId(long id) { prevId = id; }
    
    /**
     * the values of saved objects if this page is a leaf
     */
    private Object[] values;
    public Object getValue(int index) { return values[index]; }
    
    /**
     * shared by leafs and non-leafs.. these are either the first key
     * to the child page (non-leaf), or the actual keys to the 
     * objects that are persisted (leaf).
     */
    private long[] keys;
    public int compacity() { return keys.length; }
    public long getKey(int index) { return keys[index]; }
    
    /**
     * the values of the pages that map the keys. these basically point
     * to the children page bucket id
     */
    private long[] childrenPages;
    
    /**
     * this represents the amount of keys used in the array.
     */
    private int keysUsed;
    public int compacityUsed() { return keysUsed; }
    
    /**
     * leaf constructor
     * @param file 
     */
    public BPlusPage(BPlusPageManager manager, File file)
    {
        isLeaf = true;
        this.manager = manager;
    }
    
    /**
     * non-leaf constructor
     */
    public BPlusPage(BPlusPageManager manager)
    {
        isLeaf = false;
        this.manager = manager;
    }
    
    /**
     * for Serializer stuff
     */
    public BPlusPage()
    {
        
    }
    
    public void close()
    {
        
    }
    
    public Object select(long key) 
            throws FledPresistanceException
    {
        int index = findFirstGreaterOrEqualChild(key);
        if (isLeaf)
        {
            if (keys[index] == key)
            {
                return values[index];
            }
            return null;
        }
        else
        {
            return manager.getPage(childrenPages[index]).select(key);
        }
    }
    
    public BPlusBrowser browse(long key)
            throws FledPresistanceException
    {
        int index = findFirstGreaterOrEqualChild(key);
        if (isLeaf)
        {
            return new BPlusBrowser(this, manager, index);
        }
        else
        {
            return manager.getPage(childrenPages[index]).browse(key);
        }
    }
    
    public boolean indexInRange(int index)
    {
        return (0 <= index) && (index < keysUsed);
    }
    
    /**
     * this is used for non-leafs, and gets the first 'fitting' key for
     * the next page. 
     * @param key
     * @return 
     */
    private int findFirstGreaterOrEqualChild(long key)
    {
        int left = 0;
        int right = keysUsed;

        // do a binary search
        while(left < right) 
        {
            int middle = (left + right) / 2;
            if (keys[middle] < key) 
            {
                left = middle + 1;
            }
            else 
            {
                right = middle;
            }
        }
        return right;
    }

    /**
     * 
     * @param buffer
     * @return 
     */
    @Override
    public Object deserialize(ByteBuffer buffer) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public ByteBuffer serialize(Object obj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
