/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface TableData
{
    public String getMasterFile();

    public void close();

    public int insert(ByteBuffer data);
    
    public void update(int key, ByteBuffer data);
    
    public ByteBuffer select(int key);
    
    public ArrayList<ByteBuffer> selectAll(int[] keys);
    
    public ByteBuffer delete(int key);
}
