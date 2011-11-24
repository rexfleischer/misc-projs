/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.interfaces;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author REx
 */
public interface Serializer<_Ty> extends Serializable
{
    public Object deserialize(_Ty buffer)
            throws IOException;
    
    public _Ty serialize(Object obj)
            throws IOException;
}
