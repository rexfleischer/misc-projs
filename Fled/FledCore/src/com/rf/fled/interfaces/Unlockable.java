/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.interfaces;

import com.rf.fled.exceptions.FledLockException;

/**
 *
 * @author REx
 */
public interface Unlockable 
{
    public void unlock() throws FledLockException;
}
