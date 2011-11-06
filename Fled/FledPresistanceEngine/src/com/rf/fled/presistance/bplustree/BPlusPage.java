/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author REx
 */
public class BPlusPage implements Externalizable
{
    /**
     * the main controller of the tree
     */
    private BPlusTree treeManager;
    
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
     * makes a new empty page
     */
    public BPlusPage(BPlusTree treeManager, boolean leaf)
    {
        isLeaf = leaf;
        this.treeManager = treeManager;
        
        keysUsed    = 0;
        keys        = new long[treeManager.getRecordsPerPage()];
        nextId      = 0;
        prevId      = 0;
        this.thisId = treeManager.incrementPageCount();
        
        if (isLeaf)
        {
            childrenPages   = null;
            values          = new Object[treeManager.getRecordsPerPage()];
        }
        else
        {
            childrenPages   = new long[treeManager.getRecordsPerPage()];
            values          = null;
        }
    }
    
    /**
     * the first insert constructor. only for leaf
     */
    public BPlusPage(
            BPlusTree treeManager,
            Long key,
            Object value)
    {
        this.isLeaf = true;
        this.treeManager = treeManager;
        this.thisId = treeManager.incrementPageCount();
        
        keysUsed = 1;
        keys            = new long[treeManager.getRecordsPerPage()];
        values          = new Object[treeManager.getRecordsPerPage()];
        childrenPages   = null; // not used if leaf
    }
    
    public BPlusPage(
            BPlusTree treeManager, 
            BPlusPage rootPage, 
            BPlusPage flowPage)
    {
        this.isLeaf = true;
        this.treeManager = treeManager;
        this.thisId = treeManager.incrementPageCount();
        
        keysUsed = 1;
        keys            = new long[treeManager.getRecordsPerPage()];
        values          = null;
        childrenPages   = new long[treeManager.getRecordsPerPage()];
        
        keys[0] = rootPage.getKey(0);
        keys[1] = flowPage.getKey(0);
        childrenPages[0] = rootPage.getThisBuckedId();
        childrenPages[1] = flowPage.getThisBuckedId();
    }
    
    /**
     * for Serializer stuff
     */
    public BPlusPage()
    {
        
    }
    
    public boolean indexInRange(int index)
    {
        return (0 <= index) && (index < keysUsed);
    }
    
    public boolean isFull()
    {
        return keysUsed == keys.length;
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
            return treeManager
                    .getPageManager()
                    .getPage(childrenPages[index])
                    .select(key);
        }
    }
    
    public BPlusBrowser browse(long key)
            throws FledPresistanceException
    {
        int index = findFirstGreaterOrEqualChild(key);
        if (isLeaf)
        {
            return new BPlusBrowser(this, treeManager.getPageManager(), index);
        }
        else
        {
            return treeManager
                    .getPageManager()
                    .getPage(childrenPages[index])
                    .browse(key);
        }
    }
    
    public BPlusBrowser browse()
            throws FledPresistanceException
    {
        if (isLeaf)
        {
            return new BPlusBrowser(this, treeManager.getPageManager(), 0);
        }
        else
        {
            return treeManager
                    .getPageManager()
                    .getPage(childrenPages[0])
                    .browse();
        }
    }
    
    public InsertResult insert(long key, Object data, boolean replace) 
            throws FledPresistanceException
    {
        long overFlowId = -1;
        InsertResult result;
        int index = findFirstGreaterOrEqualChild(key);
        
        if (isLeaf)
        {
            result = new InsertResult();
            
            if (key == keys[index])
            {
                // if we get here, then that means we are 
                // replacing the key
                
                // for returning stuff and things
                result.existing = key;
                
                if (replace)
                {
                    values[index] = data;
                    treeManager.getPageManager().savePage(this);
                }
                return result;
            }
        }
        else
        {
            BPlusPage child = treeManager
                                .getPageManager()
                                .getPage(childrenPages[index]);
            result = child.insert(key, data, replace);
            
            if (result.existing != null)
            {
                // return if there is a exiting value because that means
                // that we found an existing key meaning we dont
                // have to reform anything
                return result;
            }
            
            if (result.overflowPage == null)
            {
                // this means there was an insert, but no overflow,
                // so we dont have to reform anything
                return result;
            }
            
            // if we get here, then that means that there has been an overflow
            // from a child page. so we need to insert the new page as
            // a child of this one, OR, if there is an overflow here too,
            // split this page
            
            // we get this key because this will be the first key of the
            // new page. 
            key = result.overflowPage.getKey(0);
            overFlowId = result.overflowPage.getThisBuckedId();
            
            // clear this because this is a recursive implementation of this
            // algorithm, so we dont want to trick the previous calls
            result.overflowPage = null;
        }
        
        if (!isFull())
        {
            // if we get into here, then we know that we dont have
            // to split this page
            if (isLeaf)
            {
                insertEntry(key, data, index);
            }
            else
            {
                insertChild(key, overFlowId, index);
            }
            treeManager.getPageManager().savePage(this);
            return result;
        }
        
        // if we get here, then we know that this page is overflowing 
        // so we must split it.
        int halfPageSize = treeManager.getRecordsPerPage() >> 1;
        BPlusPage newPage = new BPlusPage(treeManager, isLeaf);
        
        // basically, if the new insert is in the bottom half, then its
        // going there.. but if its in the upper half, then its going there
        if (index < halfPageSize)
        {
            // we need to move the entries (plus the new one) to the overflow
            // page. to do this we need to look at how we are going to do this:
            // 
            // with 10 entries, plus the new one, the transform is as such
            // this page before:    ||||||||||
            // now, this is-->
            // this page after:     |||N||----
            // overflow page:       |||||-----
            //
            // the algorithm works for child and enteries
            if (isLeaf)
            {
                copyEntries(halfPageSize, newPage, 0, halfPageSize);
                nullEntries(halfPageSize, halfPageSize);
                insertEntry(key, data, index);
            }
            else
            {
                copyChildren(halfPageSize, newPage, 0, halfPageSize);
                nullChildren(halfPageSize, halfPageSize);
                insertChild(key, overFlowId, index);
            }
        }
        else
        {
            if (isLeaf) 
            {
                copyEntries(halfPageSize, newPage, 0, halfPageSize);
                nullEntries(halfPageSize, halfPageSize);
                newPage.insertEntry(key, data, index);
            }
            else 
            {
                copyChildren(halfPageSize, newPage, 0, halfPageSize);
                nullChildren(halfPageSize, halfPageSize);
                newPage.insertChild(key, overFlowId, index);
            }
        }
        
        if (isLeaf)
        {
            newPage.setPreviousBucketId(thisId);
            if (nextId != 0)
            {
                BPlusPage nextPage = treeManager.getPageManager().getPage(nextId);
                nextPage.setPreviousBucketId(newPage.getThisBuckedId());
                newPage.setNextBucketId(nextPage.getThisBuckedId());
                treeManager.getPageManager().savePage(nextPage);
            }
        }
        
        setNextBucketId(newPage.getThisBuckedId());
        newPage.setPreviousBucketId(getThisBuckedId());
        
        treeManager.getPageManager().savePage(this);
        treeManager.getPageManager().savePage(newPage);
        
        result.overflowPage = newPage;
        return result;
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
    
    private void insertEntry(long key, Object value, int insertIndex)
    {
        System.arraycopy(
                keys, 
                insertIndex,
                keys, 
                insertIndex+1, 
                keysUsed-insertIndex);
        System.arraycopy(
                values, 
                insertIndex,
                values, 
                insertIndex+1, 
                keysUsed-insertIndex);
        keysUsed++;
        keys[insertIndex] = key;
        values[insertIndex] = value;
    }
    
    private void insertChild(long key, long value, int insertIndex)
    {
        System.arraycopy(
                keys, 
                insertIndex,
                keys, 
                insertIndex+1, 
                keysUsed-insertIndex);
        System.arraycopy(
                childrenPages, 
                insertIndex,
                childrenPages, 
                insertIndex+1, 
                keysUsed-insertIndex);
        keysUsed++;
        keys[insertIndex] = key;
        childrenPages[insertIndex] = value;
    }
    
    private void copyEntries(int index, BPlusPage dest, int destIndex, int count)
    {
        
    }
    
    private void copyChildren(int index, BPlusPage dest, int destIndex, int count)
    {
        
    }
    
    public void setChild(int index, long key, long childId)
    {
        
    }
    
    public void setEntry(int index, long key, Object value)
    {
        
    }
    
    public void nullEntries(int start, int count)
    {
        
    }
    
    public void nullChildren(int start, int count)
    {
        
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws  IOException, 
                    ClassNotFoundException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
