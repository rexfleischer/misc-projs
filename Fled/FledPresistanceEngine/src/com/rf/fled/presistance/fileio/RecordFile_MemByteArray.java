/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.fileio;

import com.rf.fled.presistance.util.ByteArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * the order in the file goes as such
 * 
 * blocksUsed,   [4 bytes]
 * blockCount,   [4 bytes]
 * blockStarts,  [4 * blockStarts.length bytes]
 * meta.length,  [4 bytes]
 * meta          [meta.length bytes]
 * objects
 * 
 * @author REx
 */
public class RecordFile_MemByteArray implements RecordFile
{
    private String filename;
    
    private ByteArray bytes;
    
    private int blocksUsed;
    
    private int[] blockStarts;
    
    private int metaLength;
    
    /**
     * constructor for an existing record file on disk
     * @param filename 
     */
    public RecordFile_MemByteArray(String filename)
            throws IOException
    {
        rollback();
    }
    
    /**
     * basically RecordFile_MemByteArray(filename, recordCount, new byte[]{})
     * @param filename
     * @param recordCount 
     */
    public RecordFile_MemByteArray(String filename, int recordCount)
    {
        this(filename, recordCount, new byte[]{});
    }
    
    public RecordFile_MemByteArray(String filename, int recordCount, byte[] meta)
    {
        bytes       = new ByteArray();
        blockStarts = new int[recordCount];
        blocksUsed  = 0;
        metaLength  = meta.length;
        
        bytes.writeInt(blocksUsed, 0);
        bytes.writeInt(blockStarts.length, bytes.compacityUsed());
        bytes.write(new byte[recordCount*4], bytes.compacityUsed());
        bytes.writeInt(meta.length, bytes.compacityUsed());
        bytes.write(meta, bytes.compacityUsed());
    }
    
    @Override
    public long fileSize() 
            throws IOException 
    {
        return bytes.compacityUsed();
    }

    @Override
    public int numOfRecords() 
            throws IOException 
    {
        return blocksUsed;
    }
    
    @Override
    public int recordCompacity()
            throws IOException
    {
        return blockStarts.length;
    }

    @Override
    public int recordSize(int index) 
            throws IOException 
    {
        if (index == blocksUsed - 1)
        {
            return bytes.compacityUsed() - blockStarts[blocksUsed - 1];
        }
        return blockStarts[index + 1] - blockStarts[index];
    }

    @Override
    public void setMeta(byte[] meta)
            throws IOException 
    {
        int metaPos = metaPosition();
        if (meta.length == metaLength)
        {
            bytes.write(meta, metaPos);
        }
        else
        {
            int diff = meta.length - metaLength;
            bytes.insertSome(meta, metaPos, metaLength);
            metaLength = meta.length;
            bytes.writeInt(metaLength, metaPos - 4);
            
            for (int i = 0; i < blocksUsed; i++)
            {
                blockStarts[i] += diff;
                bytes.writeInt(blockStarts[i], 8 + 4*i);
            }
        }
    }

    @Override
    public byte[] getMeta() 
            throws IOException 
    {
        byte[] result = new byte[metaLength];
        bytes.read(result, metaPosition());
        return result;
    }

    @Override
    public byte[] read(int index)
            throws IOException 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[] result = new byte[recordSize(index)];
        bytes.read(result, blockStarts[index]);
        return result;
    }

    @Override
    public void write(byte[] src, int index) 
            throws IOException 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        int diff = recordSize(index) - src.length;
        
        if (diff == 0)
        {
            bytes.write(src, blockStarts[index]);
        }
        else
        {
            bytes.insertSome(src, blockStarts[index], recordSize(index));
            
            for (int i = index+1; i < blocksUsed; i++)
            {
                blockStarts[i] += diff;
                bytes.writeInt(blockStarts[i], 8 + 4*i);
            }
        }
    }

    @Override
    public void remove(int index) 
            throws IOException 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        int size = recordSize(index);
        
        bytes.delete(blockStarts[index], recordSize(index));
        blocksUsed--;
        
        for(int i = index+1; i < blocksUsed; i++)
        {
            blockStarts[i] = (blockStarts[i+1] - size);
            bytes.writeInt(blockStarts[i], 8 + 4*i);
        }
    }

    @Override
    public void insert(byte[] src, int index)
            throws IOException 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (isFull())
        {
            throw new IllegalStateException("overflow");
        }
        
        if (index == blocksUsed)
        {
            blockStarts[index] = bytes.compacityUsed();
            bytes.insert(src, bytes.compacityUsed());
        }
        else
        {
            bytes.insert(src, blockStarts[index]);
            
            for(int i = blocksUsed-1; i > index; i--)
            {
                blockStarts[i] = blockStarts[i-1]+src.length;
                bytes.writeInt(blockStarts[i], 8 + 4*i);
            }
        }
        blocksUsed++;
    }
    
    @Override
    public void commit() 
            throws IOException 
    {
        FileOutputStream stream = new FileOutputStream(filename);
        stream.write(bytes.array());
    }

    @Override
    public void rollback() 
            throws IOException
    {
        byte[] streamedBytes = new byte[(int)(new File(filename)).length()];
        FileInputStream stream = new FileInputStream(filename);
        stream.read(streamedBytes);
        
        bytes = new ByteArray(streamedBytes);
        
        blocksUsed  = bytes.readInt(0);
        int blockCount = bytes.readInt(4);
        blockStarts = new int[blockCount];
        metaLength  = bytes.readInt(metaPosition() - 4);
        for(int i = 0; i < blocksUsed; i++)
        {
            blockStarts[i] = bytes.readInt(8 + 4*i);
        }
    }

    @Override
    public boolean isFull() 
            throws IOException 
    {
        return (blockStarts.length-1) == blocksUsed;
    }
    
    private int metaPosition()
    {
        return  12 + // blocksUsed + blockCount + meta.length
                (4 * blockStarts.length);
    }
}
