/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lock;

import com.rf.fled.exceptions.FledIOException;
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
    class Range
    {
        public final int lower;
        
        public final int upper;
        
        public final int key;
        
        public final LockType type;
        
        protected int height;
        
        public Range(int lower, int upper, int key, LockType type)
        {
            height      = 0;
            this.lower  = lower;
            this.upper  = upper;
            this.key    = key;
            this.type   = type;
        }
        
        public boolean collides(Range range)
        {
            return (lower <= range.lower && range.lower <= upper) ||
                   (lower <= range.upper && range.upper <= upper);
        }
    }
    
    class RangedLock implements Unlockable
    {
        private int key;
        
        private LockType type;
        
        public RangedLock(int key, LockType type)
        {
            this.key  = key;
            this.type = type;
        }

        @Override
        public void unlock() throws FledLockException
        {
            KeyRangeReadWriteLock.this.unlock(key, type);
        }
    }
    
    private int keyAt;
    
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
    
    protected void unlock(int key, LockType type)
            throws FledLockException
    {
        switch(type)
        {
            case READ:
                
                break;
            case WRITE:
                
                break;
            default:
                throw new FledLockException(
                        LanguageStatements.INVALID_LOCK_TYPE);
        }
    }
    
    public Unlockable lockRange(int lower, int upper, LockType type)
    {
        if (type == null)
        {
            throw new NullPointerException("type");
        }
        Unlockable result = null;
        synchronized(LOCK)
        {
            
        }
        return result;
    }
    
    private int readCollision(Range range)
    {
        if (!reads.isEmpty())
        {
            return rangeCheck(range, reads.iterator());
        }
        return 0;
    }
    
    private int writeCollision(Range range)
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
}
