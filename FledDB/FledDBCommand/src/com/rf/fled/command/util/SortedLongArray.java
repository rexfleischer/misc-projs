/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.command.util;

import java.util.Arrays;
import java.util.Iterator;

/**
 * this is used for collections of ids when doing queries.
 * @author REx
 */
public class SortedLongArray implements Iterable<Long>
{
    public static final long NULL = Long.MIN_VALUE;
    
    public static final int DEFAULT_SIZE = 16;
    
    private long[] list;
    
    private int count;
    
    public SortedLongArray(long[] list, int compacity)
    {
        if (list.length < compacity)
        {
            throw new IllegalArgumentException(
                    "compacity cannot be less than list.length");
        }
        count = list.length;
        this.list = new long[compacity];
        System.arraycopy(list, 0, this.list, 0, count);
        Arrays.sort(this.list, 0, count);
    }

    public SortedLongArray() 
    {
        count   = 0;
        list    = new long[DEFAULT_SIZE];
    }
    
    public int size()
    {
        return count;
    }

    public Long get(int index)
    {
        return list[index];
    }
    
    public int add(Long value)
    {
        int index = contains(value);
        
        // if it's already in the array, then do not insert it... we do
        // not allow duplicate keys
        if (index < 0)
        {
            index *= -1;
            if (count >= list.length)
            {
                resize(count + 1);
            }
            System.arraycopy(list, index, list, index + 1, count - index);
            list[index] = value;
            count++;
        }
        
        return index;
    }
    
    public Long remove(int index)
    {
        long result = list[index];
        System.arraycopy(list, index + 1, list, index, count - index);
        count--;
        list[count] = NULL;
        return result;
    }
    
    public int contains(Long target)
    {
        return Arrays.binarySearch(list, 0, count, target);
    }
    
    public long[] array()
    {
        return list;
    }
    
    public long[] copyUsed()
    {
        long[] result = new long[count];
        System.arraycopy(list, 0, result, 0, count);
        return result;
    }

    @Override
    public Iterator<Long> iterator() 
    {
        return new SortedLongArrayIterator();
    }
    
    private void resize(int size)
    {
        if (size == list.length)
        {
            return;
        }
        long[] _bytes = new long[size];
        System.arraycopy(list, 0, _bytes, 0, 
                (list.length < size) ? list.length : size);
        list = _bytes;
    }
    
    public class SortedLongArrayIterator implements Iterator<Long>
    {
        private int position;
        
        private SortedLongArrayIterator()
        {
            position = 0;
        }
        
        @Override
        public boolean hasNext() 
        {
            return position < SortedLongArray.this.count;
        }

        @Override
        public Long next() 
        {
            long result = SortedLongArray.this.list[position];
            position++;
            return result;
        }

        @Override
        public void remove() 
        {
            throw new UnsupportedOperationException();
        }
    }
}
