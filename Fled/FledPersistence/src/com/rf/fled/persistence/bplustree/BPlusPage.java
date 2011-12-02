/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.exceptions.FledIOException;
import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.ByteArray;
import com.rf.fled.persistence.RecordFile;
import com.rf.fled.util.Pair;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class BPlusPage 
{
    public static final String EXTENSION = "bpage";
    
    private RecordFile bytes;
    
    protected BPlusTree bplustree;
    
    protected long thisId;
    
    protected long nextId;
        
    protected long prevId;

    protected boolean isLeaf;
    
    protected long[] keys;
    
    private byte[] buildMeta() throws IOException
    {
        ByteArray array = new ByteArray();
        array.writeLong(nextId, 0);
        array.writeLong(prevId, 8);
        array.writeBoolean(isLeaf, 16);
        array.writeInt(keys.length, 17);
        for(int i = 0; i < bytes.compacityUsed(); i++)
        {
            array.writeLong(keys[i], 21 + 8 * i);
        }
        return array.copyUsedBytes();
    }
    
    private void setMeta(byte[] data) throws IOException
    {
        ByteArray array = new ByteArray(data);
        nextId = array.readLong(0);
        prevId = array.readLong(8);
        isLeaf = array.readBoolean(16);
        keys = new long[array.readInt(17)];
        for(int i = 0; i < bytes.compacityUsed(); i++)
        {
            keys[i] = array.readLong(21 + 8 * i);
        }
    }

    /**
     * first insert constructor
     * @param aThis
     * @param id
     * @param record 
     */
    BPlusPage(BPlusTree bplustree, long id, Object record) 
            throws FledIOException, IOException 
    {
        this.isLeaf = true;
        this.bplustree = bplustree;
        
        this.keys   = new long[bplustree.maxRecords];
        this.nextId = BPlusTree.NULL_PAGE;
        this.prevId = BPlusTree.NULL_PAGE;
        this.bytes  = new RecordFile(keys.length);
        
        this.insertEntry(id, record, 0);
        
        this.thisId = bplustree.fileManager.saveFile(this, bplustree.pageSerializer);
    }

    /**
     * when the root page is split
     * @param aThis
     * @param rootPage
     * @param overflowPage 
     */
    BPlusPage(BPlusTree bplustree, BPlusPage rootPage, BPlusPage overflowPage) 
            throws FledIOException
    {
        this.isLeaf = false;
        this.bplustree = bplustree;
        
        this.keys   = new long[bplustree.maxRecords];
        this.nextId = BPlusTree.NULL_PAGE;
        this.prevId = BPlusTree.NULL_PAGE;
        this.bytes  = new RecordFile(keys.length);
        
        this.insertChild(rootPage.getKey(0), rootPage.thisId, 0);
        this.insertChild(overflowPage.getKey(0), overflowPage.thisId, 1);
        
        this.thisId = bplustree.fileManager.saveFile(this, bplustree.pageSerializer);
    }
    
    /**
     * 
     * @param bplustree
     * @param isLeaf 
     */
    BPlusPage(BPlusTree bplustree, boolean isLeaf) 
            throws FledIOException
    {
        this.isLeaf = isLeaf;
        this.bplustree = bplustree;
        
        this.keys   = new long[bplustree.maxRecords];
        this.nextId = BPlusTree.NULL_PAGE;
        this.prevId = BPlusTree.NULL_PAGE;
        this.bytes  = new RecordFile(keys.length);
        
        this.thisId = bplustree.fileManager.saveFile(this, bplustree.pageSerializer);
    }
    
    /**
     * for serialization
     * @param buffer 
     */
    BPlusPage(byte[] buffer) throws IOException
    {
        bytes = new RecordFile(buffer);
        setMeta(bytes.getMeta());
    }

    long getThisId() 
    {
        return thisId;
    }

    long getNextId() 
    {
        return nextId;
    }
    
    long getPrevId()
    {
        return prevId;
    }

    int size() 
    {
        return bytes.compacityUsed();
    }

    boolean indexInRange(int index) 
    {
        return (index >= 0) && (index < bytes.compacityUsed());
    }

    Long getKey(int index) 
    {
        return keys[index];
    }

    long getChildId(int index) 
    {
        byte[] raw = bytes.read(index);
        if (raw.length != 8)
        {
            return BPlusTree.NULL_PAGE;
        }
        return 
            (((long)(raw[0]) << 56) |
             ((long)(raw[1]) << 48) |
             ((long)(raw[2]) << 40) |
             ((long)(raw[3]) << 32) |
             ((long)(raw[4]) << 24) |
             ((long)(raw[5]) << 16) |
             ((long)(raw[6]) <<  8) |
             ((long)(raw[7])));
    }

    byte[] getValue(int index)
    {
        return bytes.read(index);
    }

    int compacityUsed()
    {
        return bytes.compacityUsed();
    }

    Browser<Pair<Long, Object>> browse() throws FledIOException
    {
        if (isLeaf)
        {
            return new BPlusBrowser(this, bplustree, 0);
        }
        else
        {
            return bplustree.loadPage(getChildId(0)).browse();
        }
    }

    Browser<Pair<Long, Object>> browse(long id) throws FledIOException 
    {
        int index = findFirstLessOrEqualChild(id);
        if (isLeaf)
        {
            return new BPlusBrowser(this, bplustree, index);
        }
        else
        {
            return bplustree.loadPage(getChildId(index)).browse();
        }
    }

    Object select(long id) throws FledIOException 
    {
        int index = findFirstLessOrEqualChild(id);
        if (isLeaf)
        {
            if (keys[index] == id)
            {
                try
                {
                    return bplustree
                            .valueSerailizer
                            .deserialize(bytes.read(index));
                }
                catch(Exception ex)
                {
                    // @TODO statement
                    throw new FledIOException(LanguageStatements.NONE, ex);
                }
            }
            return null;
        }
        else
        {
            return bplustree.loadPage(getChildId(index)).select(id);
        }
    }

    InsertResult insert(long key, Object record, boolean replace) 
            throws IOException, FledIOException 
    {
        long overFlowId = BPlusTree.NULL_PAGE;
        InsertResult result;
        int index = findFirstLessOrEqualChild(key);
        
        if (isLeaf)
        {
            result = new InsertResult();
            
            if (key == keys[index])
            {
                // if we get here, then that means we are 
                // replacing the key
                
                // for returning stuff and things
                result.existing = bplustree
                        .valueSerailizer
                        .deserialize(getValue(index));
                
                if (replace)
                {
                    bytes.write(bplustree.valueSerailizer.serialize(record), index);
                    bplustree.savePage(this);
                }
                return result;
            }
        }
        else
        {
            BPlusPage child = bplustree.loadPage(getChildId(index));
            result = child.insert(key, record, replace);
            
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
            overFlowId = result.overflowPage.thisId;
            
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
                insertEntry(key, record, index+1);
            }
            else
            {
                insertChild(key, overFlowId, index+1);
            }
            
            return result;
        }
        
        // if we get here, then we know that this page is overflowing 
        // so we must split it.
        int halfPageSize = bplustree.maxRecords >> 1;
        BPlusPage newPage = new BPlusPage(bplustree, isLeaf);
        
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
                moveValues(halfPageSize, newPage, 0, halfPageSize);
                insertEntry(key, record, index-halfPageSize+1);
            }
            else
            {
                moveValues(halfPageSize, newPage, 0, halfPageSize);
                insertChild(key, overFlowId, index-halfPageSize+1);
            }
        }
        else
        {
            // with 10 entries, plus the new one, the transform is as such
            // this page before:    ||||||||||
            // now, this is-->
            // this page after:     |||||----
            // overflow page:       |||N||-----
            if (isLeaf) 
            {
                moveValues(halfPageSize, newPage, 0, halfPageSize);
                newPage.insertEntry(key, record, index-halfPageSize+1);
            }
            else 
            {
                moveValues(halfPageSize, newPage, 0, halfPageSize);
                newPage.insertChild(key, overFlowId, index-halfPageSize+1);
            }
        }
        
        // if this is a leaf, then we need to connect them together
        if (isLeaf)
        {
            if (nextId != BPlusTree.NULL_PAGE)
            {
                // if there is a next bucket, then we need to link it
                BPlusPage nextPage = bplustree.loadPage(nextId);
                
                nextPage.prevId = newPage.nextId;
                newPage.nextId = nextPage.thisId;
                
                bplustree.savePage(nextPage);
            }
            newPage.prevId = thisId;
            this.nextId = newPage.thisId;
        }
        
        bplustree.savePage(this);
        bplustree.savePage(newPage);
        
        result.overflowPage = newPage;
        return result;
    }

    DeleteResult delete(long key) throws FledIOException, IOException, FledPresistanceException
    {
        DeleteResult result = null;
        int half = bplustree.maxRecords >> 1;
        int index = findFirstLessOrEqualChild(key);

        if (isLeaf)
        {
            result = new DeleteResult();
            if (keys[index] != key)
            {
                result.removedValue = null;
                result.underflow    = false;
                return result;
            }

            result.underflow    = false;
            result.removedValue = bplustree.valueSerailizer.deserialize(getValue(index));
            deleteEntry(index);
        }
        else
        {
            BPlusPage child = bplustree.loadPage(getChildId(index));
            result = child.delete(key);
            
            keys[index] = child.getKey(0);
            
            if (result.underflow)
            {
                if (index < half)
                {
                    BPlusPage brother = bplustree.loadPage(getChildId(index + 1));
                    
                    if (brother.compacityUsed() > half)
                    {
                        // this is the formual for how much is going to be
                        // stolen from the brother page. the formula ensures
                        // that the amount of records stolen from the brother
                        // wont leave the brother less than half empty
                        int stealing = (brother.compacityUsed() - half + 1) / 2;
                        
                        brother.moveValues(0, child, half - 1, stealing);
                        
                        // update the children pages again
                        keys[index] = child.getKey(0);
                        keys[index + 1] = brother.getKey(0);
                        
                        bplustree.savePage(this);
                        bplustree.savePage(child);
                        bplustree.savePage(brother);
                    }
                    else
                    {
                        // getting here means that the brother page is half 
                        // full, and sense the child page is under half full,
                        // we are just going to dump the child page into
                        // the bother page.
                        
                        // @TODO check the other side
                        if (brother.compacityUsed() != half)
                        {
                            // this means that something really, really
                            // bad happened, but save what we've done
                            // so far.
                            bplustree.savePage(this);
                            // @TODO statement
                            throw new FledPresistanceException(LanguageStatements.NONE);
                        }
                        
                        child.moveValues(0, brother, 0, half - 1);
                        
                        this.deleteChild(index);
                        this.keys[index] = brother.getKey(0);
                        
                        if (child.prevId != BPlusTree.NULL_PAGE)
                        {
                            BPlusPage prev = bplustree.loadPage(child.prevId);
                            
                            prev.nextId = child.nextId;
                            
                            bplustree.savePage(prev);
                        }
                        
                        if (child.nextId != BPlusTree.NULL_PAGE)
                        {
                            BPlusPage next = bplustree.loadPage(child.nextId);
                            
                            next.prevId = child.prevId;
                            
                            bplustree.savePage(next);
                        }
                        
                        bplustree.savePage(this);
                        bplustree.savePage(brother);
                        bplustree.deletePage(child);
                    }
                }
                else
                {
                    BPlusPage brother = bplustree.loadPage(getChildId(index-1));
                    
                    if (brother.compacityUsed() > half)
                    {
                        int stealing = (brother.compacityUsed() - half + 1) / 2;
                        
                        brother.moveValues(brother.compacityUsed() - stealing, child, 0, stealing);
                        
                        // update the children pages again
                        keys[index] = child.getKey(0);
                        keys[index - 1] = brother.getKey(0);
                        
                        bplustree.savePage(this);
                        bplustree.savePage(child);
                        bplustree.savePage(brother);
                    }
                    else
                    {
                        // @TODO check the other side
                        if (brother.compacityUsed() != half)
                        {
                            // this means that something really, really
                            // bad happened, but save what we've done
                            // so far.
                            bplustree.savePage(this);
                            // @TODO statement
                            throw new FledPresistanceException(LanguageStatements.NONE);
                        }
                        
                        child.moveValues(0, brother, half + 1, child.bytes.compacityUsed());
                        
                        this.deleteChild(index);
                        
                        if (child.prevId != BPlusTree.NULL_PAGE)
                        {
                            BPlusPage prev = bplustree.loadPage(child.prevId);
                            
                            prev.nextId = child.nextId;
                            
                            bplustree.savePage(prev);
                        }
                        
                        if (child.nextId != BPlusTree.NULL_PAGE)
                        {
                            BPlusPage next = bplustree.loadPage(child.nextId);
                            
                            next.prevId = child.prevId;
                            
                            bplustree.savePage(next);
                        }
                        
                        bplustree.savePage(this);
                        bplustree.savePage(child);
                        bplustree.savePage(brother);
                    }
                }
            }
            else
            {
                bplustree.savePage(this);
            }
        }

        // check if there is an underflow
        result.underflow = bytes.compacityUsed() < half;

        return result;
    }
    
    protected int findFirstLessOrEqualChild(long key)
    {
        int min = 0;
        int max = bytes.compacityUsed() - 1;

        // do a binary search
        while(max > min)
        {
            int mid = (max + min) / 2;
            if (keys[mid] == key)
            {
                return mid;
            }
            if (keys[mid] > key) 
            {
                max = mid;
            }
            else 
            {
                min = mid + 1;
            }
        }
        if (keys[max] > key && max > 0)
        {
            return max - 1;
        }
        return max;
    }

    protected boolean isFull() 
    {
        return bytes.isFull();
    }

    private void insertEntry(long key, Object record, int index) 
            throws IOException 
    {
        byte[] raw = bplustree.valueSerailizer.serialize(record);
        bytes.insert(raw, index);
        System.arraycopy(keys, index, keys, index + 1, keys.length - index - 1);
        keys[index] = key;
    }

    private void insertChild(long key, long childId, int index) 
    {
        byte[] raw = new byte[8];
        raw[0] = (byte)(0xff & (childId >> 56));
        raw[1] = (byte)(0xff & (childId >> 48));
        raw[2] = (byte)(0xff & (childId >> 40));
        raw[3] = (byte)(0xff & (childId >> 32));
        raw[4] = (byte)(0xff & (childId >> 24));
        raw[5] = (byte)(0xff & (childId >> 16));
        raw[6] = (byte)(0xff & (childId >>  8));
        raw[7] = (byte)(0xff & childId);
        bytes.insert(raw, index);
        System.arraycopy(keys, index, keys, index + 1, keys.length - index - 1);
        keys[index] = key;
    }

    private void deleteEntry(int index) 
    {
        bytes.remove(index);
        System.arraycopy(keys, index + 1, keys, index, keys.length - index - 1);
        keys[bytes.compacityUsed()] = 0;
    }

    private void deleteChild(int index)
    {
        bytes.remove(index);
        System.arraycopy(keys, index + 1, keys, index, keys.length - index - 1);
        keys[bytes.compacityUsed()] = 0;
    }

    private void moveValues(int start, BPlusPage dest, int destStart, int count) 
    {
        for(int i = 0; i < count; i++)
        {
            byte[] raw = bytes.read(start);
            bytes.remove(start);
            
            dest.bytes.insert(raw, destStart + i);
        }
        // first shift over the dest values so there is room
        System.arraycopy(dest.keys, destStart, dest.keys, destStart + count, dest.keys.length - (destStart + count));
        
        // now move over the keys from this.keys to dest.keys
        System.arraycopy(keys, start, dest.keys, destStart, count);
        
        // shitf this.keys backwards to fill in the empty gap now
        System.arraycopy(keys, start + count, keys, start, count - start);
        
        // null the now unused this.keys
        for(int i = bytes.compacityUsed(); i < bytes.compacity(); i++)
        {
            keys[i] = BPlusTree.NULL_PAGE;
        }
    }
    
    public static class BPlusPageSerializer implements Serializer<byte[]>
    {
        @Override
        public Object deserialize(byte[] buffer) 
                throws IOException 
        {
            return new BPlusPage(buffer);
        }

        @Override
        public byte[] serialize(Object obj) 
                throws IOException
        {
            BPlusPage page = (BPlusPage) obj;
            page.bytes.setMeta(page.buildMeta());
            return page.bytes.getBytes();
        }
    }
}
