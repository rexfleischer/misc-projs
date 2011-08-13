/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.datatype;

import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public interface DataType
{
    public int numOfBytes();

    public boolean indexable();

    public Long convert(Object data) throws Exception;

    public void write(ByteBuffer buffer, Object data);

    public Object read(ByteBuffer buffer);
}
