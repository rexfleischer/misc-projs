/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.logconsumer;

import java.util.Collection;

/**
 *
 * @author REx
 */
public interface ConsumedAccess<_Ty>
{
    public boolean log(_Ty data);
    
    public Collection<_Ty> query(String query);
}
