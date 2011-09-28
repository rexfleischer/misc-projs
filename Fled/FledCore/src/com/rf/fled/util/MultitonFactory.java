/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.util;

import com.rf.fled.exceptions.FledException;
import com.rf.fled.language.LanguageStatements;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class MultitonFactory<_Ty>
{
    private Map<String, _Ty> instances;
    
    public MultitonFactory()
    {
        instances = new HashMap<String, _Ty>();
    }
    
    public synchronized _Ty getNew(String key)
            throws FledException
    {
        if (instances.containsKey(key))
        {
            throw new FledException(
                    LanguageStatements.CONTEXT_ALREADY_EXISTS);
        }
        if (_contextExists(key))
        {
            throw new FledException(
                    LanguageStatements.CONTEXT_ALREADY_EXISTS);
        }
        _Ty result = _create(key);
        instances.put(key, result);
        return result;
    }
    
    
    public synchronized _Ty getInit(String key)
            throws FledException
    {
        if (instances.containsKey(key))
        {
            return instances.get(key);
        }
        if (!_contextExists(key))
        {
            throw new FledException(
                    LanguageStatements.CONTEXT_DOES_NOT_EXISTS);
        }
        _Ty result = _load(key);
        instances.put(key, result);
        return result;
    }
    
    
    public synchronized void delete(String key)
            throws FledException
    {
        if (instances.containsKey(key))
        {
            instances.remove(key);
        }
        _delete(key);
    }
    
    
    public synchronized void close(String key)
            throws FledException
    {
        if (!instances.containsKey(key))
        {
            return;
        }
        _Ty data = instances.get(key);
        _save(key, data);
    }
    
    
    public synchronized void closeAll()
            throws FledException
    {
        Iterator<String> it = instances.keySet().iterator();
        while(it.hasNext())
        {
            String context = it.next();
            _Ty data = instances.get(context);
            _save(context, data);
        }
    }
    
    protected abstract boolean _contextExists(String context);
    
    protected abstract _Ty _create(String context)
            throws FledException;
    
    protected abstract _Ty _load(String context)
            throws FledException;
    
    protected abstract void _save(String context, _Ty data)
            throws FledException;
    
    protected abstract void _delete(String context)
            throws FledException;
}
