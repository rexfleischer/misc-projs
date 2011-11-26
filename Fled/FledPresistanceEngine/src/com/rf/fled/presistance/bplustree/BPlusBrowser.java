/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.interfaces.Browser;
import com.rf.fled.util.Pair;

/**
 *
 * @author REx
 */
public class BPlusBrowser implements Browser<Pair<Long, Object>>
{

    @Override
    public boolean valid() 
            throws FledPresistanceException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean curr(Pair<Long, Object> obj)
            throws FledPresistanceException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean prev(Pair<Long, Object> obj) 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean next(Pair<Long, Object> obj) 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
