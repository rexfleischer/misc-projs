/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.data;

import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface TableData
{
    public String getMasterFile();

    public void close();

    public int insert(ByteBuffer data);
}
