/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 * 
 * @author REx
 */
public interface Transactionable 
{
    public Transactionable deepCopy(FileManager newManager);
}
