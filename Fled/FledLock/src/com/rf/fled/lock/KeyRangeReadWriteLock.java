/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lock;

import com.rf.fled.exceptions.FledLockException;
import com.rf.fled.interfaces.Unlockable;
import com.rf.fled.language.LanguageStatements;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class KeyRangeReadWriteLock 
{
    protected class Range
    {
        public final long key;
        
        public final int lower;
        
        public final int upper;
        
        public final LockType type;
        
        public int height;
        
        public Range(long key, int lower, int upper, int height, LockType type)
        {
            this.key    = key;
            this.lower  = lower;
            this.upper  = upper;
            this.height = height;
            this.type   = type;
        }
        
        public boolean collides(Range range)
        {
            return (lower <= range.lower && range.lower <= upper) ||
                   (lower <= range.upper && range.upper <= upper);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 83 * hash + (int) (this.key ^ (this.key >>> 32));
            return hash;
        }
        
        @Override
        public boolean equals(Object o)
        {
            if (o == null)
            {
                return false;
            }
            if (!(o instanceof Range))
            {
                return false;
            }
            Range range = (Range) o;
            return this.key == range.key;
        }
    }
    
    class RangedLock implements Unlockable
    {
        protected Range range;
        
        public RangedLock(Range range)
        {
            this.range  = range;
        }

        @Override
        public void unlock() throws FledLockException
        {
            KeyRangeReadWriteLock.this.unlock(range);
        }
    }
    
    private long keyAt;
    
    private ArrayList<Range> writes;
    
    private ArrayList<Range> reads;
    
    public final Object LOCK;
    
    public KeyRangeReadWriteLock()
    {
        keyAt   = 0;
        writes  = new ArrayList<Range>();
        reads   = new ArrayList<Range>();
        LOCK    = new Object();
    }
    
    protected void unlock(Range range)
            throws FledLockException
    {
        if (range == null)
        {
            throw new NullPointerException("range");
        }
        synchronized(LOCK)
        {
            switch(range.type)
            {
                case READ:
                    if (!reads.remove(range))
                    {
                        throw new FledLockException(
                            LanguageStatements.INVALID_LOCK_KEY);
                    }
                    break;
                case WRITE:
                    if (!writes.remove(range))
                    {
                        throw new FledLockException(
                            LanguageStatements.INVALID_LOCK_KEY);
                    }
                    break;
                default:
                    throw new FledLockException(
                            LanguageStatements.INVALID_LOCK_TYPE);
            }
            
            // let all of the other threads figure it out, this thread
            // is done
            LOCK.notifyAll();
        }
    }
    
    public Unlockable lockRange(int lower, int upper, LockType type)
            throws FledLockException
    {
        if (type == null)
        {
            throw new NullPointerException("type");
        }
        Unlockable result = null;
        synchronized(LOCK)
        {
            Range thisRange = new Range(keyAt, lower, upper, 0, type);
            ++keyAt;
            // check type because we need to know if we should check 
            // collisions for reads. 
            // NOTE: reads can overlap but cannot happen at the same
            //       time as writes, and writes cannot overlap with anything.
            // this means that if it is a write, then we must block off that
            // range, but we must let the things that came first finish...
            
            if (type == LockType.WRITE)
            {
                // first, we need to check if there are any read or 
                // write collisions because if there isnt, then we
                // can just allow the lock.
                int heightForRead   = checkForReadCollision(thisRange);
                int heightForWrite  = checkForWriteCollision(thisRange);
                
                // we need to make sure and put this in so other threads
                // know about it
                thisRange.height = (heightForRead > heightForWrite) ?
                            heightForRead : heightForWrite;
                writes.add(thisRange);
                
                // while the height is not zero, we will just keep
                // going around and around and arou....
                while(thisRange.height != 0)
                {
                    try 
                    {
                        LOCK.wait();
                    }
                    catch (InterruptedException ex) { } // dont do anything
                    
                    // we want to make sure that this thread does not 
                    // go later on the waiting list (basically saying that
                    // we do not want the height to keep going up), so
                    // what we need to do is check for if there is a 
                    // collisions with the heights that are _lower_ then
                    // the current height... 
                    heightForRead   = checkForLowerReadCollision(thisRange);
                    heightForWrite  = checkForLowerWriteCollision(thisRange);
                    thisRange.height = (heightForRead > heightForWrite) ?
                            heightForRead : heightForWrite;
                }
                
                // finally, we're freaking done!
                result = new RangedLock(thisRange);
            }
            else if (type == LockType.READ)
            {
                // we only need to check for write collisions because
                // reads can overlap
                int heightForWrite  = checkForWriteCollision(thisRange);
                
                // we need to make sure and put this in so other threads
                // know about it
                thisRange.height = heightForWrite;
                writes.add(thisRange);
                
                // while the height is not zero, we will 
                while(thisRange.height != 0)
                {
                    try 
                    {
                        LOCK.wait();
                    }
                    catch (InterruptedException ex) { } // dont do anything
                    
                    heightForWrite  = checkForLowerWriteCollision(thisRange);
                    thisRange.height = heightForWrite;
                }
                
                // finally, we're freaking done! that wasnt so bad
                result = new RangedLock(thisRange);
            }
            else
            {
                // idk.. but this is at least a sanity check.
                throw new FledLockException(
                        LanguageStatements.UNEXPECTED_EXCEPTION);
            }
        }
        return result;
    }
    
    private int checkForReadCollision(Range range)
    {
        if (!reads.isEmpty())
        {
            return rangeCheck(range, reads.iterator());
        }
        return 0;
    }
    
    private int checkForWriteCollision(Range range)
    {
        if (!writes.isEmpty())
        {
            return rangeCheck(range, writes.iterator());
        }
        return 0;
    }
    
    private int rangeCheck(Range range, Iterator<Range> it)
    {
        int result = 0;
        while(it.hasNext())
        {
            Range checking = it.next();
            if (result < checking.height && checking.collides(range))
            {
                result = checking.height + 1;
            }
        }
        return result;
    }
    
    private int checkForLowerReadCollision(Range range)
    {
        if (!reads.isEmpty())
        {
            return lowerRangeCheck(range, reads.iterator());
        }
        return 0;
    }
    
    private int checkForLowerWriteCollision(Range range)
    {
        if (!writes.isEmpty())
        {
            return lowerRangeCheck(range, writes.iterator());
        }
        return 0;
    }
    
    private int lowerRangeCheck(Range range, Iterator<Range> it)
    {
        int result = 0;
        while(it.hasNext())
        {
            Range checking = it.next();
            if (result < checking.height && 
                range.height < checking.height &&
                checking.collides(range))
            {
                result = checking.height + 1;
            }
        }
        return result;
    }
}
