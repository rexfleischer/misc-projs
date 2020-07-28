/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.cache;

import com.wc.fcore.cache.exception.CacheException;

/**
 *
 * @author REx
 */
public interface CachePresistance {

    public void admit(String key, Cacheable obj) throws CacheException;
    
    public Cacheable recover(String key) throws CacheException;

    public void remove(String key) throws CacheException;

}
