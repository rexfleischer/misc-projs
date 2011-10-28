/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.interfaces;

import com.rf.fled.util.Pair;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface Cursor 
{
    public Pair<Long, ByteBuffer> next();
    
    public Pair<Long, ByteBuffer> previous();
}
