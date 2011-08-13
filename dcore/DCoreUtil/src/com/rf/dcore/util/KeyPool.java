/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * this should be used for location allocation and such. thats because
 * this class assumes that the first key is always 1 and because
 * ranges are not considered. meaning that if this class is using
 * all the keys from 1 - 100 and 10000 - 10002 and 20000 - 20002, it will have
 * a spot allocated saving every key outside of those ranges for later use.
 * @author REx
 */
public class KeyPool implements Serializable
{
    // if something more advanced needs to happen, then the object
    // can be locked
    public transient final Object KEY_LOCK = new Object();

    private int max;

    private LinkedList<Integer> unused;

    public KeyPool()
    {
        max         = 0;
        unused      = new LinkedList<Integer>();
    }

    public int getMax()
    {
        int result = -1;
        synchronized(KEY_LOCK)
        {
            result = max;
        }
        return result;
    }

    public int getClosestUnused(int key)
    {
        int result = -1;
        synchronized(KEY_LOCK)
        {
            if (unused.contains(key))
            {
                result = unused.remove(unused.indexOf(key));
            }
            else
            {
                int justBelow = 0;
                int justAbove = max + 1;
                Iterator<Integer> it = unused.iterator();
                while(it.hasNext())
                {
                    Integer curr = it.next();
                    if (justBelow < curr && curr < key)
                    {
                        justBelow = curr;
                    }
                    else if (key < curr && curr < justAbove)
                    {
                        justAbove = curr;
                    }
                }

                if (justBelow == 0 && justAbove == max + 1)
                {
                    max++;
                    result = max;
                }
                else if (justBelow == 0)
                {
                    result = justAbove;
                }
                else if (justAbove == max + 1)
                {
                    result = justBelow;
                }
                else
                {
                    if (key - justBelow > justAbove - key)
                    {
                        unused.remove(unused.indexOf(justAbove));
                        result = justAbove;
                    }
                    else
                    {
                        unused.remove(unused.indexOf(justBelow));
                        result = justBelow;
                    }
                }
            }
        }
        return result;
    }

    public void forceKeyUsage(int key)
    {
        synchronized(KEY_LOCK)
        {
            if (key > max)
            {
                for ( ; max < key; max++)
                {
                    unused.add(max);
                }
            }
            else
            {
                if (unused.contains(key))
                {
                    unused.remove(unused.indexOf(key));
                }
            }
        }
    }

    public int getUnusedKey()
    {
        int result = -1;
        synchronized(KEY_LOCK)
        {
            if (unused.size() > 0)
            {
                result = unused.remove(unused.size() - 1);
            }
            else
            {
                max++;
                result = max;
            }
        }
        return result;
    }

    public void unsetKey(int key)
    {
        synchronized(KEY_LOCK)
        {
            if (0 < key && key <= max && !unused.contains(key))
            {
                unused.add(key);
            }
        }
    }

    public boolean isUsed(int key)
    {
        boolean result = false;
        synchronized(KEY_LOCK)
        {
            if (0 < key && key <= max && !unused.contains(key))
            {
                result = true;
            }
        }
        return result;
    }
}
