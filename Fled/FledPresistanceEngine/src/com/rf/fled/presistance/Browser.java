/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.exceptions.FledPresistanceException;

/**
 *
 * @author REx
 */
public interface Browser<_Ty> 
{
    public boolean prev(_Ty obj)
            throws FledPresistanceException;
    
    public boolean curr(_Ty obj)
            throws FledPresistanceException;
    
    public boolean next(_Ty obj)
            throws FledPresistanceException;
}
