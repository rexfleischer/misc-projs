/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 * optional extension to Persistence
 * @author REx
 */
public interface Transactionable 
{
    /**
     * 
     * @param newManager
     * @return 
     */
    public Transactionable deepCopy(FileManager newManager);
}
