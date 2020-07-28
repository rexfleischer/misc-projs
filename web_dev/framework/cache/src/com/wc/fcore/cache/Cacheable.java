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
public interface Cacheable {

    public String serialize() throws CacheException;

    public void unseriablize(String info) throws CacheException;

}
