package com.rf.fled.presistance.bplustree.old;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.presistance.bplustree;
//
//import com.rf.fled.exceptions.FledPresistanceException;
//import com.rf.fled.interfaces.Serializer;
//import com.rf.fled.language.LanguageStatements;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.nio.ByteBuffer;
//
///**
// *
// * @author REx
// */
//public class BPlusPage
//{
//    public static final String EXTENSION = "bpage";
//    
//    public static final Integer NULL_BUCKET = 0;
//    
//    /**
//     * the main controller of the tree
//     */
//    private BPlusTree treeManager;
//    
//    /**
//     * the id that represents this page from the page manager. if you ask
//     * the page manager for this id, then it will return this instance.
//     */
//    private long thisId;
//    public long getThisBuckedId() { return thisId; }
//    
//    /**
//     * this tells the page if it is a leaf or not. mostly for internal class
//     * use with serialization
//     */
//    private boolean isLeaf;
//    public boolean isLeaf() { return isLeaf; }
//    
//    /**
//     * sense this is a bplustree, then at the leaf nodes there are next
//     * and previous pages for browsing and such.
//     * 
//     * NOTE: depending on how this implementation turns out, the two 
//     * abstractions of the base page may be pulled out if it has
//     * a performance boost.
//     */
//    private long nextId;
//    private long prevId;
//    public long getNextBucketId() { return nextId; }
//    public long getPreviousBucketId() { return prevId; }
//    public void setNextBucketId(long id) { nextId = id; }
//    public void setPreviousBucketId(long id) { prevId = id; }
//    
//    /**
//     * the values of saved objects if this page is a leaf
//     */
//    private Object[] values;
//    public Object getValue(int index) { return values[index]; }
//    
//    /**
//     * shared by leafs and non-leafs.. these are either the first key
//     * to the child page (non-leaf), or the actual keys to the 
//     * objects that are persisted (leaf).
//     */
//    private long[] keys;
//    public int compacity() { return keys.length; }
//    public long getKey(int index) { return keys[index]; }
//    
//    /**
//     * the values of the pages that map the keys. these basically point
//     * to the children page bucket id
//     */
//    private long[] children;
//    public long getChildId(int index) { return children[index]; }
//    
//    /**
//     * this represents the amount of keys used in the array.
//     */
//    protected int keysUsed;
//    public int compacityUsed() { return keysUsed; }
//    
//    /**
//     * makes a new empty page
//     */
//    public BPlusPage(BPlusTree treeManager, boolean leaf)
//    {
//        isLeaf = leaf;
//        this.treeManager = treeManager;
//        
//        this.keysUsed   = 0;
//        this.keys       = new long[treeManager.getRecordsPerPage()];
//        this.nextId     = NULL_BUCKET;
//        this.prevId     = NULL_BUCKET;
//        this.thisId     = treeManager.incrementPageCount();
//        
//        if (isLeaf)
//        {
//            children    = null;
//            values      = new Object[treeManager.getRecordsPerPage()];
//        }
//        else
//        {
//            children    = new long[treeManager.getRecordsPerPage()];
//            values      = null;
//        }
//    }
//    
//    /**
//     * the first insert constructor. only for leaf
//     */
//    public BPlusPage(
//            BPlusTree treeManager,
//            Long key,
//            Object value)
//    {
//        this.isLeaf = true;
//        this.treeManager = treeManager;
//        this.thisId = treeManager.incrementPageCount();
//        
//        keysUsed    = 1;
//        nextId      = NULL_BUCKET;
//        prevId      = NULL_BUCKET;
//        keys        = new long[treeManager.getRecordsPerPage()];
//        values      = new Object[treeManager.getRecordsPerPage()];
//        children    = null; // not used if leaf
//        
//        keys[0]     = key;
//        values[0]   = value;
//    }
//    
//    /**
//     * new root construct.. this cannot be a leaf. if it is, then first insert
//     * construct needs to be used.
//     * @param treeManager
//     * @param rootPage
//     * @param flowPage 
//     */
//    public BPlusPage(
//            BPlusTree treeManager, 
//            BPlusPage rootPage, 
//            BPlusPage flowPage)
//    {
//        this.isLeaf = false;
//        this.treeManager = treeManager;
//        this.thisId = treeManager.incrementPageCount();
//        
//        keysUsed    = 2;
//        nextId      = NULL_BUCKET;
//        prevId      = NULL_BUCKET;
//        keys        = new long[treeManager.getRecordsPerPage()];
//        values      = null;
//        children    = new long[treeManager.getRecordsPerPage()];
//        
//        keys[0] = rootPage.getKey(0);
//        keys[1] = flowPage.getKey(0);
//        children[0] = rootPage.getThisBuckedId();
//        children[1] = flowPage.getThisBuckedId();
//    }
//    
//    /**
//     * for Serializer stuff
//     */
//    public BPlusPage()
//    {
//        
//    }
//    
//    public boolean indexInRange(int index)
//    {
//        return (0 <= index) && (index < keysUsed);
//    }
//    
//    public boolean isFull()
//    {
//        return keysUsed == keys.length;
//    }
//
//    boolean isEmpty() 
//    {
//        return keysUsed == 0;
//    }
//    
//    public Object select(long key) 
//            throws FledPresistanceException
//    {
//        int index = findFirstLessOrEqualChild(key);
//        if (isLeaf)
//        {
//            if (keys[index] == key)
//            {
//                return values[index];
//            }
//            return null;
//        }
//        else
//        {
//            return ((BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(children[index]), 
//                        treeManager.getPageSerializer()))
//                    .select(key);
//        }
//    }
//    
//    public BPlusBrowser browse(long key)
//            throws FledPresistanceException
//    {
//        int index = findFirstLessOrEqualChild(key);
//        if (isLeaf)
//        {
//            return new BPlusBrowser(this, treeManager.getPageManager(), index);
//        }
//        else
//        {
//            return ((BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(children[index]), 
//                        treeManager.getPageSerializer()))
//                    .browse(key);
//        }
//    }
//    
//    public BPlusBrowser browse()
//            throws FledPresistanceException
//    {
//        if (isLeaf)
//        {
//            return new BPlusBrowser(this, treeManager.getPageManager(), 0);
//        }
//        else
//        {
//            return ((BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(children[0]), 
//                        treeManager.getPageSerializer()))
//                    .browse();
//        }
//    }
//    
//    public InsertResult insert(long key, Object data, boolean replace) 
//            throws FledPresistanceException
//    {
//        long overFlowId = -1;
//        InsertResult result;
//        int index = findFirstLessOrEqualChild(key);
//        
//        if (isLeaf)
//        {
//            result = new InsertResult();
//            
//            if (key == keys[index])
//            {
//                // if we get here, then that means we are 
//                // replacing the key
//                
//                // for returning stuff and things
//                result.existing = values[index];
//                
//                if (replace)
//                {
//                    values[index] = data;
//                    treeManager.getPageManager().savePage(
//                            treeManager.buildPageId(thisId), 
//                            this, 
//                            treeManager.getPageSerializer());
//                }
//                return result;
//            }
//        }
//        else
//        {
//            BPlusPage child = (BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(children[index]), 
//                        treeManager.getPageSerializer());
//            result = child.insert(key, data, replace);
//            
//            if (result.existing != null)
//            {
//                // return if there is a exiting value because that means
//                // that we found an existing key meaning we dont
//                // have to reform anything
//                return result;
//            }
//            
//            if (result.overflowPage == null)
//            {
//                // this means there was an insert, but no overflow,
//                // so we dont have to reform anything
//                return result;
//            }
//            
//            // if we get here, then that means that there has been an overflow
//            // from a child page. so we need to insert the new page as
//            // a child of this one, OR, if there is an overflow here too,
//            // split this page
//            
//            // we get this key because this will be the first key of the
//            // new page. 
//            key = result.overflowPage.getKey(0);
//            overFlowId = result.overflowPage.getThisBuckedId();
//            
//            // clear this because this is a recursive implementation of this
//            // algorithm, so we dont want to trick the previous calls
//            result.overflowPage = null;
//        }
//        
//        if (!isFull())
//        {
//            // if we get into here, then we know that we dont have
//            // to split this page
//            if (isLeaf)
//            {
//                insertEntry(key, data, index+1);
//            }
//            else
//            {
//                insertChild(key, overFlowId, index+1);
//            }
//            treeManager.getPageManager().savePage(
//                    treeManager.buildPageId(thisId), 
//                    this, 
//                    treeManager.getPageSerializer());
//            return result;
//        }
//        
//        // if we get here, then we know that this page is overflowing 
//        // so we must split it.
//        int halfPageSize = treeManager.getRecordsPerPage() >> 1;
//        BPlusPage newPage = new BPlusPage(treeManager, isLeaf);
//        
//        // basically, if the new insert is in the bottom half, then its
//        // going there.. but if its in the upper half, then its going there
//        if (index < halfPageSize)
//        {
//            // we need to move the entries (plus the new one) to the overflow
//            // page. to do this we need to look at how we are going to do this:
//            // 
//            // with 10 entries, plus the new one, the transform is as such
//            // this page before:    ||||||||||
//            // now, this is-->
//            // this page after:     |||N||----
//            // overflow page:       |||||-----
//            //
//            // the algorithm works for child and enteries
//            if (isLeaf)
//            {
//                copyEntries(halfPageSize, newPage, 0, halfPageSize);
//                newPage.keysUsed = halfPageSize;
//                this.keysUsed = halfPageSize;
//                nullEntries(halfPageSize, halfPageSize);
//                insertEntry(key, data, index-halfPageSize+1);
//            }
//            else
//            {
//                copyChildren(halfPageSize, newPage, 0, halfPageSize);
//                newPage.keysUsed = halfPageSize;
//                this.keysUsed = halfPageSize;
//                nullChildren(halfPageSize, halfPageSize);
//                insertChild(key, overFlowId, index-halfPageSize+1);
//            }
//        }
//        else
//        {
//            // with 10 entries, plus the new one, the transform is as such
//            // this page before:    ||||||||||
//            // now, this is-->
//            // this page after:     |||||----
//            // overflow page:       |||N||-----
//            if (isLeaf) 
//            {
//                copyEntries(halfPageSize, newPage, 0, halfPageSize);
//                newPage.keysUsed = halfPageSize;
//                this.keysUsed = halfPageSize;
//                nullEntries(halfPageSize, halfPageSize);
//                newPage.insertEntry(key, data, index-halfPageSize+1);
//            }
//            else 
//            {
//                copyChildren(halfPageSize, newPage, 0, halfPageSize);
//                newPage.keysUsed = halfPageSize;
//                this.keysUsed = halfPageSize;
//                nullChildren(halfPageSize, halfPageSize);
//                newPage.insertChild(key, overFlowId, index-halfPageSize+1);
//            }
//        }
//        
//        // if this is a leaf, then we need to connect them together
//        if (isLeaf)
//        {
//            if (nextId != NULL_BUCKET)
//            {
//                // if there is a next bucket, then we need to link it
//                BPlusPage nextPage = (BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(nextId), 
//                        treeManager.getPageSerializer());
//                
//                nextPage.setPreviousBucketId(newPage.getThisBuckedId());
//                newPage.setNextBucketId(nextPage.getThisBuckedId());
//                
//                treeManager.getPageManager().savePage(
//                        treeManager.buildPageId(nextPage.thisId),
//                        nextPage, 
//                        treeManager.getPageSerializer());
//            }
//            newPage.setPreviousBucketId(thisId);
//            setNextBucketId(newPage.getThisBuckedId());
//        }
//        
//        treeManager.getPageManager().savePage(
//                treeManager.buildPageId(thisId),
//                this,
//                treeManager.getPageSerializer());
//        treeManager.getPageManager().savePage(
//                treeManager.buildPageId(newPage.thisId), 
//                newPage, 
//                treeManager.getPageSerializer());
//        
//        result.overflowPage = newPage;
//        return result;
//    }
//    
//    public DeleteResult delete(long key)
//            throws FledPresistanceException
//    {
//        DeleteResult result = null;
//        int half = treeManager.getRecordsPerPage() >> 1;
//        int index = findFirstLessOrEqualChild(key);
//
//        if (isLeaf)
//        {
//            result = new DeleteResult();
//            if (keys[index] != key)
//            {
//                result.removedValue = null;
//                result.underflow    = false;
//                return result;
//            }
//
//            result.underflow    = false;
//            result.removedValue = values[index];
//            deleteEntry(index);
//        }
//        else
//        {
//            BPlusPage child = ((BPlusPage) treeManager
//                    .getPageManager()
//                    .getPage(
//                        treeManager.buildPageId(children[index]), 
//                        treeManager.getPageSerializer()));
//            result = child.delete(key);
//            
//            keys[index] = child.getKey(0);
//            
//            if (result.underflow)
//            {
//                if (index < half)
//                {
//                    BPlusPage brother = ((BPlusPage) treeManager
//                            .getPageManager()
//                            .getPage(
//                                treeManager.buildPageId(children[index + 1]),
//                                treeManager.getPageSerializer()));
//                    
//                    if (brother.compacityUsed() > half)
//                    {
//                        // this is the formual for how much is going to be
//                        // stolen from the brother page. the formula ensures
//                        // that the amount of records stolen from the brother
//                        // wont leave the brother less than half empty
//                        int stealing = (brother.compacityUsed() - half + 1) / 2;
//                        
//                        brother.keysUsed -= stealing;
//                        child.keysUsed += stealing;
//                        
//                        if (child.isLeaf)
//                        {
//                            // put the first entries at the end of this one
//                            brother.copyEntries(0, child, half, stealing);
//                            
//                            // now, shift the array to fill the unused spots now
//                            brother.copyEntries(stealing, brother, 0, brother.keysUsed);
//                            
//                            // null the now unused entries
//                            brother.nullEntries(brother.keysUsed, stealing);
//                        }
//                        else
//                        {
//                            brother.copyChildren(0, child, half, stealing);
//                            brother.copyChildren(stealing, brother, 0, brother.keysUsed);
//                            brother.nullChildren(brother.keysUsed, stealing);
//                        }
//                        
//                        // update the children pages again
//                        keys[index] = child.getKey(0);
//                        keys[index + 1] = brother.getKey(0);
//                        
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(this.thisId), 
//                                this, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(child.thisId),
//                                child, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(brother.thisId), 
//                                brother, 
//                                treeManager.getPageSerializer());
//                    }
//                    else
//                    {
//                        // getting here means that the brother page is half 
//                        // full, and sense the child page is under half full,
//                        // we are just going to dump the child page into
//                        // the bother page.
//                        
//                        // @TODO check the other side
//                        if (brother.compacityUsed() != half)
//                        {
//                            // this means that something really, really
//                            // bad happened, but save what we've done
//                            // so far.
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(this.thisId), 
//                                    this, 
//                                    treeManager.getPageSerializer());
//                            // @TODO statement
//                            throw new FledPresistanceException(LanguageStatements.NONE);
//                        }
//                        
//                        brother.keysUsed = brother.keys.length - 1;
//                        
//                        // copy over everything from child to brother
//                        if (child.isLeaf)
//                        {
//                            // shift the entries to make room
//                            brother.copyEntries(0, brother, half - 1, half);
//                            
//                            // now copy what is in the child to the brother
//                            child.copyEntries(0, brother, 0, half - 1);
//                        }
//                        else
//                        {
//                            brother.copyChildren(0, brother, half - 1, half);
//                            child.copyChildren(0, brother, 0, half - 1);
//                        }
//                        
//                        this.deleteChild(index);
//                        this.keys[index] = brother.getKey(0);
//                        
//                        if (child.getPreviousBucketId() != NULL_BUCKET)
//                        {
//                            BPlusPage prev = (BPlusPage) treeManager
//                                    .getPageManager()
//                                    .getPage(
//                                        treeManager.buildPageId(child.getPreviousBucketId()),
//                                        treeManager.getPageSerializer());
//                            prev.setNextBucketId(child.getNextBucketId());
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(prev.thisId),
//                                    prev, 
//                                    treeManager.getPageSerializer());
//                        }
//                        
//                        if (child.getNextBucketId() != NULL_BUCKET)
//                        {
//                            BPlusPage next = (BPlusPage) treeManager
//                                    .getPageManager()
//                                    .getPage(
//                                        treeManager.buildPageId(child.getNextBucketId()),
//                                        treeManager.getPageSerializer());
//                            next.setPreviousBucketId(child.getPreviousBucketId());
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(next.thisId),
//                                    next, 
//                                    treeManager.getPageSerializer());
//                        }
//                        
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(this.thisId), 
//                                this, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(brother.thisId), 
//                                brother, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().deletePage(
//                                treeManager.buildPageId(child.getThisBuckedId()));
//                    }
//                }
//                else
//                {
//                    BPlusPage brother = (BPlusPage) treeManager
//                            .getPageManager()
//                            .getPage(
//                                treeManager.buildPageId(children[index-1]), 
//                                treeManager.getPageSerializer());
//                    
//                    if (brother.compacityUsed() > half)
//                    {
//                        int stealing = (brother.compacityUsed() - half + 1) / 2;
//                        
//                        brother.keysUsed -= stealing;
//                        child.keysUsed += stealing;
//                        
//                        if (child.isLeaf)
//                        {
//                            // shift child to make room for new entries
//                            child.copyEntries(0, child, stealing, child.keysUsed - stealing);
//                            
//                            // copy from brother to child
//                            brother.copyEntries(0, child, 0, stealing);
//                            
//                            // shift brother
//                            brother.copyEntries(stealing, brother, 0, brother.keysUsed);
//                            
//                            // null the now unused entries
//                            brother.nullEntries(brother.keysUsed, stealing);
//                        }
//                        else
//                        {
//                            child.copyChildren(0, child, stealing, child.keysUsed - stealing);
//                            brother.copyChildren(0, child, 0, stealing);
//                            brother.copyChildren(stealing, brother, 0, brother.keysUsed);
//                            brother.nullChildren(brother.keysUsed, stealing);
//                        }
//                        
//                        // update the children pages again
//                        keys[index] = child.getKey(0);
//                        keys[index - 1] = brother.getKey(0);
//                        
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(this.thisId), 
//                                this, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(child.thisId),
//                                child, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(brother.thisId), 
//                                brother, 
//                                treeManager.getPageSerializer());
//                    }
//                    else
//                    {
//                        // @TODO check the other side
//                        if (brother.compacityUsed() != half)
//                        {
//                            // this means that something really, really
//                            // bad happened, but save what we've done
//                            // so far.
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(this.thisId), 
//                                    this, 
//                                    treeManager.getPageSerializer());
//                            // @TODO statement
//                            throw new FledPresistanceException(LanguageStatements.NONE);
//                        }
//                        
//                        brother.keysUsed = brother.keys.length - 1;
//                        
//                        // copy over everything from child to brother
//                        if (child.isLeaf)
//                        {
//                            // copy everything over
//                            child.copyEntries(0, brother, half + 1, child.keysUsed);
//                        }
//                        else
//                        {
//                            child.copyChildren(0, brother, half + 1, child.keysUsed);
//                        }
//                        
//                        this.deleteChild(index);
//                        
//                        if (child.getPreviousBucketId() != NULL_BUCKET)
//                        {
//                            BPlusPage prev = (BPlusPage) treeManager
//                                    .getPageManager()
//                                    .getPage(
//                                        treeManager.buildPageId(child.getPreviousBucketId()),
//                                        treeManager.getPageSerializer());
//                            prev.setNextBucketId(child.getNextBucketId());
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(prev.thisId),
//                                    prev, 
//                                    treeManager.getPageSerializer());
//                        }
//                        
//                        if (child.getNextBucketId() != NULL_BUCKET)
//                        {
//                            BPlusPage next = (BPlusPage) treeManager
//                                    .getPageManager()
//                                    .getPage(
//                                        treeManager.buildPageId(child.getNextBucketId()),
//                                        treeManager.getPageSerializer());
//                            next.setPreviousBucketId(child.getPreviousBucketId());
//                            treeManager.getPageManager().savePage(
//                                    treeManager.buildPageId(next.thisId),
//                                    next, 
//                                    treeManager.getPageSerializer());
//                        }
//                        
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(this.thisId), 
//                                this, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().savePage(
//                                treeManager.buildPageId(brother.thisId), 
//                                brother, 
//                                treeManager.getPageSerializer());
//                        treeManager.getPageManager().deletePage(
//                                treeManager.buildPageId(child.getThisBuckedId()));
//                    }
//                }
//            }
//            else
//            {
//                treeManager.getPageManager().savePage(
//                        treeManager.buildPageId(this.thisId), 
//                        this,
//                        treeManager.getPageSerializer());
//            }
//        }
//
//        // check if there is an underflow
//        result.underflow = keysUsed < half;
//
//        return result;
//    }
//    
//    /**
//     * this is used for non-leafs, and gets the first 'fitting' key for
//     * the next page. 
//     * @param key
//     * @return 
//     */
//    protected int findFirstLessOrEqualChild(long key)
//    {
//        int min = 0;
//        int max = keysUsed - 1;
//
//        // do a binary search
//        while(max > min)
//        {
//            int mid = (max + min) / 2;
//            if (keys[mid] == key)
//            {
//                return mid;
//            }
//            if (keys[mid] > key) 
//            {
//                max = mid;
//            }
//            else 
//            {
//                min = mid + 1;
//            }
//        }
//        if (keys[max] > key && max > 0)
//        {
//            return max - 1;
//        }
//        return max;
//    }
//    
//    protected void insertEntry(long key, Object value, int insertIndex)
//    {
//        System.arraycopy(
//                keys, 
//                insertIndex,
//                keys, 
//                insertIndex+1, 
//                keysUsed-insertIndex);
//        System.arraycopy(
//                values, 
//                insertIndex,
//                values, 
//                insertIndex+1, 
//                keysUsed-insertIndex);
//        keysUsed++;
//        keys[insertIndex] = key;
//        values[insertIndex] = value;
//    }
//    
//    protected void insertChild(long key, long value, int insertIndex)
//    {
//        System.arraycopy(
//                keys, 
//                insertIndex,
//                keys, 
//                insertIndex+1, 
//                keysUsed-insertIndex);
//        System.arraycopy(
//                children, 
//                insertIndex,
//                children, 
//                insertIndex+1, 
//                keysUsed-insertIndex);
//        keysUsed++;
//        keys[insertIndex] = key;
//        children[insertIndex] = value;
//    }
//    
//    protected void deleteChild(int index)
//    {
//        System.arraycopy(
//                keys, 
//                index + 1, 
//                keys, 
//                index, 
//                keysUsed - index);
//        System.arraycopy(
//                children, 
//                index + 1, 
//                children, 
//                index, 
//                keysUsed - index);
//        keys[keysUsed - 1] = NULL_BUCKET;
//        children[keysUsed - 1] = NULL_BUCKET;
//        keysUsed--;
//    }
//    
//    protected void deleteEntry(int index)
//    {
//        System.arraycopy(
//                keys, 
//                index + 1, 
//                keys, 
//                index, 
//                keysUsed - index);
//        System.arraycopy(
//                values, 
//                index + 1, 
//                values, 
//                index, 
//                keysUsed - index);
//        keys[keysUsed - 1] = NULL_BUCKET;
//        values[keysUsed - 1] = NULL_BUCKET;
//        keysUsed--;
//    }
//    
//    protected void copyEntries(
//            int index, 
//            BPlusPage dest, 
//            int destIndex, 
//            int count)
//    {
//        System.arraycopy(
//                keys, 
//                index, 
//                dest.keys, 
//                destIndex, 
//                count);
//        System.arraycopy(
//                values, 
//                index, 
//                dest.values, 
//                destIndex, 
//                count);
//    }
//    
//    protected void copyChildren(
//            int index, 
//            BPlusPage dest, 
//            int destIndex, 
//            int count)
//    {
//        System.arraycopy(
//                keys, 
//                index, 
//                dest.keys, 
//                destIndex, 
//                count);
//        System.arraycopy(
//                children, 
//                index, 
//                dest.children, 
//                destIndex, 
//                count);
//    }
//    
//    protected void nullEntries(int start, int count)
//    {
//        for(int i = start; i < start + count; i++)
//        {
//            keys[i] = NULL_BUCKET;
//            values[i] = null;
//        }
//    }
//    
//    protected void nullChildren(int start, int count)
//    {
//        for(int i = start; i < start + count; i++)
//        {
//            keys[i] = NULL_BUCKET;
//            children[i] = NULL_BUCKET;
//        }
//    }
//    
//    public static class BPlusPageSerializer implements Serializer<byte[]>
//    {
//        private BPlusTree btree;
//        
//        protected BPlusPageSerializer(BPlusTree btree)
//        {
//            this.btree = btree;
//        }
//        
////        @Override
////        public ByteBuffer serialize(Object object) 
////            throws IOException
////        {
////            BPlusPage page = (BPlusPage) object;
////            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
////            ObjectOutputStream out = new ObjectOutputStream(byteOut);
////
////            out.writeLong(page.thisId);
////            out.writeLong(page.prevId);
////            out.writeLong(page.nextId);
////            out.writeBoolean(page.isLeaf);
////            out.writeInt(page.keysUsed);
////
////            for(int i = 0; i < page.keysUsed; i++)
////            {
////                out.writeLong(page.keys[i]);
////            }
////
////            for(int i = 0; i < page.keysUsed; i++)
////            {
////                if (page.isLeaf)
////                {
////                    byte[] serialized = btree
////                            .getValueSerializer()
////                            .serialize(page.values[i]);
////                    out.writeInt(serialized.length);
////                    out.write(serialized);
////                }
////                else
////                {
////                    out.writeLong(page.children[i]);
////                }
////            }
////
////            ByteBuffer buffer = ByteBuffer.allocate(byteOut.size());
////            buffer.put(byteOut.toByteArray());
////            buffer.position(0);
////            return buffer;
////        }
//
////        @Override
////        public Object deserialize(ByteBuffer buffer) 
////                throws IOException
////        {
////            BPlusPage result = new BPlusPage();
////            result.treeManager = btree;
////            
////            byte[] bytes = new byte[buffer.capacity()];
////            buffer.get(bytes);
////            ByteArrayInputStream _buffer = new ByteArrayInputStream(bytes);
////            ObjectInputStream in = new ObjectInputStream(_buffer);
////
////            result.thisId = in.readLong();
////            result.prevId = in.readLong();
////            result.nextId = in.readLong();
////            result.isLeaf = in.readBoolean();
////            result.keysUsed = in.readInt();
////
////            result.keys = new long[btree.getRecordsPerPage()];
////            if (result.isLeaf)
////            {
////                result.values = new Object[btree.getRecordsPerPage()];
////            }
////            else
////            {
////                result.children = new long[btree.getRecordsPerPage()];
////            }
////
////            for(int i = 0; i < result.keysUsed; i++)
////            {
////                result.keys[i] = in.readLong();
////            }
////
////            for(int i = 0; i < result.keysUsed; i++)
////            {
////                if (result.isLeaf)
////                {
////                    int objectSize = in.readInt();
////                    byte[] deserialized = new byte[objectSize];
////                    in.read(deserialized);
////                    result.values[i] = btree
////                            .getValueSerializer()
////                            .deserialize(deserialized);
////                }
////                else
////                {
////                    result.children[i] = in.readLong();
////                }
////            }
////
////            return result;
////        }
//
//        @Override
//        public Object deserialize(byte[] buffer) 
//                throws IOException 
//        {
//            BPlusPage result = new BPlusPage();
//            result.treeManager = btree;
//            
//            ByteArrayInputStream _buffer = new ByteArrayInputStream(buffer);
//            ObjectInputStream in = new ObjectInputStream(_buffer);
//
//            result.thisId = in.readLong();
//            result.prevId = in.readLong();
//            result.nextId = in.readLong();
//            result.isLeaf = in.readBoolean();
//            result.keysUsed = in.readInt();
//
//            result.keys = new long[btree.getRecordsPerPage()];
//            if (result.isLeaf)
//            {
//                result.values = new Object[btree.getRecordsPerPage()];
//            }
//            else
//            {
//                result.children = new long[btree.getRecordsPerPage()];
//            }
//
//            for(int i = 0; i < result.keysUsed; i++)
//            {
//                result.keys[i] = in.readLong();
//            }
//
//            for(int i = 0; i < result.keysUsed; i++)
//            {
//                if (result.isLeaf)
//                {
//                    int objectSize = in.readInt();
//                    byte[] deserialized = new byte[objectSize];
//                    in.read(deserialized);
//                    result.values[i] = btree
//                            .getValueSerializer()
//                            .deserialize(deserialized);
//                }
//                else
//                {
//                    result.children[i] = in.readLong();
//                }
//            }
//
//            return result;
//        }
//
//        @Override
//        public byte[] serialize(Object obj) 
//                throws IOException 
//        {
//            BPlusPage page = (BPlusPage) obj;
//            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//            ObjectOutputStream out = new ObjectOutputStream(byteOut);
//
//            out.writeLong(page.thisId);
//            out.writeLong(page.prevId);
//            out.writeLong(page.nextId);
//            out.writeBoolean(page.isLeaf);
//            out.writeInt(page.keysUsed);
//
//            for(int i = 0; i < page.keysUsed; i++)
//            {
//                out.writeLong(page.keys[i]);
//            }
//
//            for(int i = 0; i < page.keysUsed; i++)
//            {
//                if (page.isLeaf)
//                {
//                    byte[] serialized = btree
//                            .getValueSerializer()
//                            .serialize(page.values[i]);
//                    out.writeInt(serialized.length);
//                    out.write(serialized);
//                }
//                else
//                {
//                    out.writeLong(page.children[i]);
//                }
//            }
//            
//            return byteOut.toByteArray();
//        }
//    }
//    
//    
//}