
package com.rf.fled.lock;

import com.rf.fled.interfaces.Unlockable;

/**
 *
 * @author REx
 */
public class ReadWriteLock 
{
    /**
     * this is for locking while trying to figure the locking situation. 
     * usually we would lock the manager, but there are some small
     * optimizations that we can do if we do it like this.
     */
    private final Object LOCK;
    
    /**
     * the amount of read locks that are active.
     * NOTE: read locks can happen at the same time
     */
    private int readLocks;
    
    /**
     * the amount of writes being requested
     * NOTE: only one write can happen at a time
     */
    private int writeLocksRequested;
    
    /**
     * this lets everything know if a write lock is active
     */
    private boolean writeLocked;
    
    public ReadWriteLock()
    {
        writeLocksRequested = 0;
        readLocks   = 0;
        writeLocked = false;
        LOCK        = new Object();
    }
    
    public Unlockable getLock(LockType type)
    {
        if (type == null)
        {
            throw new NullPointerException("type");
        }
        
        // we can create this outside of the synchronized block
        ReadWriteUnlockable result = new ReadWriteUnlockable(type);
        
        synchronized(LOCK)
        {
            if (type == LockType.WRITE)
            {
                // let threads know that a write is requested
                writeLocksRequested++;
                while(writeLocked || readLocks != 0)
                {
                    try 
                    {
                        // when a thread waits on an object like this
                        // it releases the lock while it is waiting so other
                        // threads can enter this synchronized area
                        LOCK.wait();
                    } 
                    // dont do anything, its ok for this to happen.
                    catch (InterruptedException ex) { }
                }
                // let threads know that one less write is requested and that
                // a write lock is now active
                writeLocksRequested--;
                writeLocked = true;
            }
            else if(type == LockType.READ)
            {
                while(writeLocked || writeLocksRequested > 0)
                {
                    try 
                    {
                        LOCK.wait();
                    } 
                    catch (InterruptedException ex) { }
                }
                readLocks++;
            }
        }
        
        return result;
    }
    
    protected void unlock(LockType type)
    {
        if (type == null)
        {
            throw new NullPointerException("type");
        }
        synchronized(LOCK)
        {
            if (type == LockType.WRITE)
            {
                // no more write lock
                writeLocked = false;
            }
            else if (type == LockType.READ)
            {
                // one less read lock
                readLocks--;
            }
            LOCK.notifyAll();
        }
    }
    
    public class ReadWriteUnlockable implements Unlockable
    {
        private LockType lockType;
        
        private boolean unlocked;
        
        protected ReadWriteUnlockable(LockType type)
        {
            lockType = type;
            unlocked = false;
        }
        
        @Override
        public void unlock()
        {
            if (!unlocked)
            {
                unlocked = true;
                ReadWriteLock.this.unlock(lockType);
            }
        }
    }
}
