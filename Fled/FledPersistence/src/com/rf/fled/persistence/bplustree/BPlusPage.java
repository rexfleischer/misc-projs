/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.transaction.Transactionable;
import com.rf.fled.persistence.fileio.ByteArray;
import com.rf.fled.persistence.fileio.RecordFile;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class BPlusPage implements Transactionable
{
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
        for(int i = 0; i < bytes.compacity(); i++)
        {
            array.writeLong(keys[i], 22 + 8 * i);
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
        for(int i = 0; i < bytes.compacity(); i++)
        {
            keys[i] = array.readLong(22 + 8 * i);
        }
    }

    /**
     * first insert constructor
     * @param aThis
     * @param id
     * @param record 
     */
    BPlusPage(BPlusTree bplustree, long id, Object record) 
            throws IOException, FledPersistenceException 
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
            throws IOException, FledPersistenceException
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
            throws IOException, FledPersistenceException
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
     * for deep copy
     * @param origin 
     */
    private BPlusPage(BPlusPage origin)
    {
        bytes       = new RecordFile(origin.bytes.array());
        bplustree   = origin.bplustree;
        thisId      = origin.thisId;
        nextId      = origin.nextId;
        prevId      = origin.prevId;
        isLeaf      = origin.isLeaf;
        keys        = new long[origin.keys.length];
        System.arraycopy(origin.keys, 0, keys, 0, keys.length);
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

    byte[] getValue(int index)
    {
        return bytes.read(index);
    }

    int compacityUsed()
    {
        return bytes.compacityUsed();
    }

    Browser<KeyValuePair<Long, Object>> browse() 
            throws IOException, FledPersistenceException
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

    Browser<KeyValuePair<Long, Object>> browse(long id) 
            throws FledPersistenceException, IOException 
    {
        int index = findFirstLessOrEqualChild(id);
        if (isLeaf)
        {
            return new BPlusBrowser(this, bplustree, index);
        }
        else
        {
            return bplustree.loadPage(getChildId(index)).browse(id);
        }
    }

    Object select(long id)
            throws FledPersistenceException, IOException 
    {
        int index = findFirstLessOrEqualChild(id);
        if (isLeaf)
        {
            if (keys[index] == id)
            {
                return bplustree.deserializeValue(bytes.read(index));
            }
            return null;
        }
        else
        {
            return bplustree.loadPage(getChildId(index)).select(id);
        }
    }

    InsertResult insert(long key, Object record, boolean replace) 
            throws IOException, FledPersistenceException
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
                result.existing = bplustree.deserializeValue(getValue(index));
                
                if (replace)
                {
                    bytes.write(bplustree.serializeValue(record), index);
                    bplustree.savePage(this);
                }
                return result;
            }
        }
        else
        {
            BPlusPage child = bplustree.loadPage(getChildId(index));
            result = child.insert(key, record, replace);
            
            // return if there is a exiting value because that means
            // that we found an existing key meaning we dont
            // have to reform anything
            if (result.existing != null)
            {
                return result;
            }
            
            // this means there was an insert, but no overflow,
            // so we dont have to reform anything
            if (result.overflowPage == null)
            {
                if (keys[index] != child.getKey(0))
                {
                    keys[index] = child.getKey(0);
                    bplustree.savePage(this);
                }
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
            
            keys[index] = child.getKey(0);
            
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
                // special case to when the index is 0, but
                // sense the +1 is there for general inserst,
                // it never gets placed in the zeroith place
                // even if it should be
                if (key < keys[0])
                {
                    insertEntry(key, record, 0);
                }
                else
                {
                    insertEntry(key, record, index+1);
                }
            }
            else
            {
                if (key < keys[0])
                {
                    insertChild(key, overFlowId, 0);
                }
                else
                {
                    insertChild(key, overFlowId, index+1);
                }
            }
            
            bplustree.savePage(this);
            return result;
        }
        
        // if we get here, then we know that this page is overflowing 
        // so we must split it.
        int halfPageSize = bplustree.maxRecords >> 1;
        BPlusPage newPage = new BPlusPage(bplustree, isLeaf);
        
        // move half the data over to the new page
        System.arraycopy(this.keys, halfPageSize, newPage.keys, 0, halfPageSize);
        for(int i = 0; i < halfPageSize; i++)
        {
            newPage.bytes.insert(this.bytes.read(this.bytes.compacityUsed() - 1), 0);
            this.bytes.remove(this.bytes.compacityUsed() - 1);
            this.keys[halfPageSize + i] = BPlusTree.NULL_PAGE;
        }
        
        // basically, if the new insert is in the bottom half, then its
        // going there.. but if its in the upper half, then its going there
        if (index < halfPageSize)
        {
            // means we put it 'this' page
            if (isLeaf)
            {
                if (key < keys[0])
                {
                    insertEntry(key, record, 0);
                }
                else
                {
                    insertEntry(key, record, index+1);
                }
            }
            else
            {
                if (key < keys[0])
                {
                    insertChild(key, overFlowId, 0);
                }
                else
                {
                    insertChild(key, overFlowId, index+1);
                }
            }
        }
        else
        {
            // means we put it in the new page
            index = newPage.findFirstLessOrEqualChild(key);
            if (isLeaf)
            {
                if (key < newPage.keys[0])
                {
                    newPage.insertEntry(key, record, 0);
                }
                else
                {
                    newPage.insertEntry(key, record, index+1);
                }
            }
            else
            {
                if (key < keys[0])
                {
                    newPage.insertChild(key, overFlowId, 0);
                }
                else
                {
                    newPage.insertChild(key, overFlowId, index+1);
                }
            }
        }
        
        // if this is a leaf, then we need to connect them together
        if (isLeaf)
        {
            if (nextId != BPlusTree.NULL_PAGE)
            {
                // if there is a next bucket, then we need to link it
                BPlusPage nextPage = bplustree.loadPage(nextId);
                
                // basically, this is what needs to happen
                // 
                // split -> newPage
                // split <- newPage -> nextPage
                // newPage <- nextPage
                
                nextPage.prevId = newPage.thisId;
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

    DeleteResult delete(long key) throws IOException, FledPersistenceException
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
            result.removedValue = bplustree.deserializeValue(getValue(index));
            deleteEntry(index);
            bplustree.savePage(this);
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
                        // stolen from the brother page. the formula just
                        // makes both pages have the same amount of entries
                        int stealing = (brother.compacityUsed() - half + 1) / 2;
                        
                        System.arraycopy(brother.keys, 0, child.keys, half - 1, stealing);
                        System.arraycopy(brother.keys, stealing, brother.keys, 0, brother.compacityUsed() - stealing);
                        for(int i = 0; i < stealing; i++)
                        {
                            child.bytes.insert(brother.bytes.read(0), half - 1 + i);
                            brother.bytes.remove(0);
                            brother.keys[brother.compacityUsed()] = BPlusTree.NULL_PAGE;
                        }
                        
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
                            throw new FledPersistenceException(
                                    LanguageStatements.NONE.toString());
                        }
                        
                        System.arraycopy(brother.keys, 0, brother.keys, half - 1, half);
                        System.arraycopy(child.keys, 0, brother.keys, 0, half - 1);
                        while(child.compacityUsed() > 0)
                        {
                            brother.bytes.insert(child.bytes.read(child.bytes.compacityUsed() - 1), 0);
                            child.bytes.remove(child.bytes.compacityUsed() - 1);
                        }
                        
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
                            /* the brother is always going to be the
                             * next child and loading and saving twice
                             * makes this block not actually saved
                             * when the brother gets saved below and 
                             * no caching layer for loaded files
                            BPlusPage next = bplustree.loadPage(child.nextId);
                            
                            next.prevId = child.prevId;
                            
                            bplustree.savePage(next);
                             */
                            if (child.nextId != brother.thisId)
                            {
                                // @TODO statement
                                throw new FledPersistenceException(
                                        LanguageStatements.NONE.toString());
                            }
                            else
                            {
                                brother.prevId = child.prevId;
                            }
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
                        
                        System.arraycopy(child.keys, 0, child.keys, stealing, child.compacityUsed());
                        System.arraycopy(brother.keys, brother.compacityUsed() - stealing - 1, child.keys, 0, stealing);
                        for(int i = 0; i < stealing; i++)
                        {
                            child.bytes.insert(brother.bytes.read(brother.bytes.compacityUsed() - 1), 0);
                            brother.bytes.remove(brother.bytes.compacityUsed() - 1);
                            brother.keys[brother.bytes.compacityUsed()] = BPlusTree.NULL_PAGE;
                        }
                        
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
                            // so far so its not completely corrupt
                            bplustree.savePage(this);
                            // @TODO statement
                            throw new FledPersistenceException(
                                    LanguageStatements.NONE.toString());
                        }
                        
//                        child.copyValues(0, brother, half + 1, child.compacityUsed());
                        
                        System.arraycopy(child.keys, 0, brother.keys, half, half - 1);
                        while(child.compacityUsed() > 0)
                        {
                            brother.bytes.insert(child.bytes.read(child.bytes.compacityUsed() - 1), half);
                            child.bytes.remove(child.bytes.compacityUsed() - 1);
                        }
                        
                        this.deleteChild(index);
                        this.keys[index] = brother.getKey(0);
                        
                        if (child.prevId != BPlusTree.NULL_PAGE)
                        {
                            if (brother.thisId != child.prevId)
                            {
                                // @TODO statement
                                throw new FledPersistenceException(
                                        LanguageStatements.NONE.toString());
                            }
                            else
                            {
                                brother.nextId = child.nextId;
                            }
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
    
    public void truncate(int height) 
            throws FledPersistenceException
    {
        height--;
        if (height == 0)
        {
            for(int i = 0; i < bytes.compacityUsed(); i++)
            {
                bplustree.fileManager.deleteFile(getChildId(i));
            }
        }
        else
        {
            for(int i = 0; i < bytes.compacityUsed(); i++)
            {
                bplustree.loadPage(getChildId(i)).truncate(height);
            }
        }
        bplustree.fileManager.deleteFile(thisId);
    }

    @Override
    public Transactionable deepCopy(FileManager newManager) 
    {
        // dont need to do anything with the FileManager
        return new BPlusPage(this);
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

    Long getKey(int index) 
    {
        return keys[index];
    }

    protected boolean isFull() 
    {
        return bytes.isFull();
    }

    private void insertEntry(long key, Object record, int index) 
            throws IOException 
    {
        byte[] raw = bplustree.serializeValue(record);
        bytes.insert(raw, index);
        System.arraycopy(keys, index, keys, index + 1, keys.length - index - 1);
        keys[index] = key;
    }

    long getChildId(int index) 
    {
        byte[] raw = bytes.read(index);
        if (raw.length != 8)
        {
            return BPlusTree.NULL_PAGE;
        }
        return 
            (((long)(raw[0] & 0xff) << 56) |
             ((long)(raw[1] & 0xff) << 48) |
             ((long)(raw[2] & 0xff) << 40) |
             ((long)(raw[3] & 0xff) << 32) |
             ((long)(raw[4] & 0xff) << 24) |
             ((long)(raw[5] & 0xff) << 16) |
             ((long)(raw[6] & 0xff) <<  8) |
             ((long)(raw[7] & 0xff)));
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
    
    void dump(int height)
    {
        String prefix = "";
        for(int i = 0; i < height; i++) 
        {
           prefix += "    ";
        }
        System.out.println(prefix + "-------------------------------------- BPage recid=" + thisId);
        System.out.println(prefix + "keysUsed: " + bytes.compacityUsed());
        for (int i = 0; i < bytes.compacityUsed(); i++) 
        {
            try
            {
                if (isLeaf) 
                {
                    System.out.println(prefix+"BPlusPage ["+i+"] "+keys[i]+" "+bplustree.deserializeValue(bytes.read(i)).toString());
                } 
                else 
                {
                    System.out.println(prefix+"BPlusPage ["+i+"] "+keys[i]+" "+getChildId(i));
                }
            }
            catch(Exception ex)
            {
                System.out.println(prefix+"BPlusPage ["+i+"] "+keys[i]+" unknown");
            }
        }
        System.out.println(prefix + "--------------------------------------");
    }


    /**
     * Recursively dump the state of the BTree on screen.  This is used for
     * debugging purposes only.
     */
    public void dumpRecursive(int height)
        throws FledPersistenceException
    {
        dump(height);
        height++;
        if (!isLeaf)
        {
            for(int i = 0; i < bytes.compacityUsed(); i++) 
            {
                BPlusPage child = bplustree.loadPage(getChildId(i));
                child.dumpRecursive(height);
            }
        }
    }
    
    public void assertValues() 
            throws Exception
    {
        if (!this.isLeaf)
        {
            bplustree.loadPage(getChildId(0)).assertValues();
            return;
        }
        
        
        for(int i = 0; i < bytes.compacityUsed(); i++)
        {
            try
            {
                bplustree.deserializeValue(bytes.read(i));
            }
            catch(Exception ex)
            {
                bplustree.getRoot().dumpRecursive(0);
                throw new Exception("values are corrupt");
            }
        }
        
        if (this.nextId != BPlusTree.NULL_PAGE)
        {
            BPlusPage next = bplustree.loadPage(this.nextId);
            next.assertValues();
        }
    }

    public void assertOrder(BPlusPage prev, int tolerance) 
            throws Exception
    {
        if (!this.isLeaf)
        {
            bplustree.loadPage(getChildId(0)).assertOrder(null, tolerance);
            return;
        }
        
        // check if the 'last' page's last key is less than
        // the first of this one
        if (prev != null)
        {
            if (prev.keys[prev.bytes.compacityUsed() - 1] >= keys[0] ||
                Math.abs(prev.keys[prev.bytes.compacityUsed() - 1] - keys[0]) > tolerance)
            {
                bplustree.getRoot().dumpRecursive(0);
                throw new Exception("keys out of order (" + prev.keys[prev.bytes.compacityUsed() - 1] + " >= " + keys[0] + ")");
            }
        }
        
        for(int i = 0; i < bytes.compacityUsed() - 1; i++)
        {
            if (keys[i] >= keys[i + 1]||
                Math.abs(keys[i + 1] - keys[i]) > tolerance)
            {
                bplustree.getRoot().dumpRecursive(0);
                throw new Exception("keys out of order (" + keys[i] + " >= " + keys[i + 1] + ")");
            }
        }
        
        if (this.nextId != BPlusTree.NULL_PAGE)
        {
            BPlusPage next = bplustree.loadPage(this.nextId);
            next.assertOrder(this, tolerance);
        }
    }
}
