/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 *
 * @author REx
 */
public class BasicVTOList<T extends BasicVTO> implements Iterable<T>
{
    private List wrapping;
    
    private Constructor constructor;
    
    public BasicVTOList(List wrapping, Class clazz)
    {
        this.wrapping = wrapping;
        try
        {
            this.constructor = clazz.getConstructor(Map.class);
        }
        catch(NoSuchMethodException | SecurityException ex)
        {
            throw new BasicVTOException(
                    "could not get class constructor",
                    ex);
        }
    }

    public int size()
    {
        return wrapping.size();
    }

    public boolean isEmpty()
    {
        return wrapping.isEmpty();
    }

    public boolean add(T e)
    {
        return wrapping.add(e.getDBObject());
    }

    public void clear()
    {
        wrapping.clear();
    }

    public T get(int index)
    {
        try
        {
            return (T) constructor.newInstance(wrapping.get(index));
        }
        catch(InstantiationException | 
              IllegalAccessException | 
              IllegalArgumentException | 
              InvocationTargetException ex)
        {
            throw new BasicVTOException(ex);
        }
    }

    public void add(int index, T element)
    {
        wrapping.add(index, element.getDBObject());
    }

    public T remove(int index)
    {
        try
        {
            return (T) constructor.newInstance(wrapping.remove(index));
        }
        catch(InstantiationException | 
              IllegalAccessException | 
              IllegalArgumentException | 
              InvocationTargetException ex)
        {
            throw new BasicVTOException(ex);
        }
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>() 
        {
            int position = 0;
            
            @Override
            public boolean hasNext()
            {
                return position < size();
            }

            @Override
            public T next()
            {
                return get(position++);
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}
