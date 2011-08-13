/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.util.locks;

/**
 * the point of this is to let a lower-level system lock what is asked
 * in the way that is implemented and then the locks be released when
 * desired. it extends flexibility of the locking system without much risk.
 * @author REx
 */
public interface DataLock
{

    /**
     * releases the lock that was obtained by a lower level system.
     */
    public void unlock();

}
