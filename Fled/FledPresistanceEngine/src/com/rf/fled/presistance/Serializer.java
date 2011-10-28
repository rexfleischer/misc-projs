/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface Serializer
{
    public Object deserialize(ByteBuffer buffer);
    
    public ByteBuffer serialize(Object obj);
}
