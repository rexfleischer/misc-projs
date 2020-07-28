/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.util;

/**
 * the order in the file goes as such
 * 
 * blockCount,   [4 bytes]
 * blocksUsed,   [4 bytes]
 * meta.length,  [4 bytes]
 * blockStarts,  [4 * blockStarts.length bytes]
 * meta          [meta.length bytes]
 * objects
 * 
 * @author REx
 */
public class RecordFile
{
    public static final int DEFAULT_RECORDS = 16;
    
    public static final int BLOCK_COUNT_POS = 0;
    
    public static final int BLOCKS_USED_POS = 4;
    
    public static final int META_LENGTH_POS = 8;
    
    public static final int BLOCK_START_POS = 12;
    
    private int blockStartPos(int index)
    {
        return BLOCK_START_POS + 4 * index;
    }
    
    private int metaPosition()
    {
        return  BLOCK_START_POS + 4 * blockStarts.length;
    }
    
    private ByteArray bytes;
    
    private int blocksUsed;
    
    private int[] blockStarts;
    
    private int metaLength;
    
    public RecordFile()
    {
        this(DEFAULT_RECORDS, new byte[]{});
    }
    
    /**
     * constructor for an existing record file on disk
     * @param filename 
     */
    public RecordFile(byte[] data)
    {
        bytes = new ByteArray(data);
        blocksUsed  = bytes.readInt(BLOCKS_USED_POS);
        int blockCount = bytes.readInt(BLOCK_COUNT_POS);
        blockStarts = new int[blockCount];
        metaLength  = bytes.readInt(META_LENGTH_POS);
        for(int i = 0; i < blocksUsed; i++)
        {
            blockStarts[i] = bytes.readInt(blockStartPos(i));
        }
    }
    
    /**
     * basically RecordFile_MemByteArray(filename, recordCount, new byte[]{})
     * @param filename
     * @param recordCount 
     */
    public RecordFile(int recordCount)
    {
        this(recordCount, new byte[]{});
    }
    
    public RecordFile(int recordCount, byte[] meta)
    {
        bytes       = new ByteArray();
        blockStarts = new int[recordCount];
        blocksUsed  = 0;
        metaLength  = meta.length;
        
        bytes.writeInt(blocksUsed, BLOCKS_USED_POS);
        bytes.writeInt(blockStarts.length, BLOCK_COUNT_POS);
        bytes.write(new byte[recordCount*4], BLOCK_START_POS);
        bytes.writeInt(meta.length, META_LENGTH_POS);
        bytes.write(meta, metaPosition());
    }
    
    /**
     * deep copy constructor
     * @param fileId
     * @param bytes
     * @param blocksUsed
     * @param blockStarts
     * @param metaLength 
     */
    private RecordFile(
            byte[] bytes,
            int blocksUsed,
            int[] blockStarts,
            int metaLength)
    {
        this.bytes = new ByteArray(bytes);
        this.blocksUsed = blocksUsed;
        this.metaLength = metaLength;
        this.blockStarts = new int[blockStarts.length];
        System.arraycopy(blockStarts, 0, this.blockStarts, 0, blockStarts.length);
    }
    
    public long fileSize() 
    {
        return bytes.compacityUsed();
    }

    public int compacityUsed() 
    {
        return blocksUsed;
    }
    
    public int compacity()
    {
        return blockStarts.length;
    }

    public int recordSize(int index) 
    {
        if (index == blocksUsed - 1)
        {
            return bytes.compacityUsed() - blockStarts[blocksUsed - 1];
        }
        return blockStarts[index + 1] - blockStarts[index];
    }

    public void setMeta(byte[] meta)
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
            bytes.writeInt(metaLength, META_LENGTH_POS);
            
            for (int i = 0; i < blocksUsed; i++)
            {
                blockStarts[i] += diff;
                bytes.writeInt(blockStarts[i], blockStartPos(i));
            }
        }
    }

    public byte[] getMeta() 
    {
        byte[] result = new byte[metaLength];
        bytes.read(result, metaPosition());
        return result;
    }

    public byte[] read(int index)
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[] result = new byte[recordSize(index)];
        bytes.read(result, blockStarts[index]);
        return result;
    }

    public void write(byte[] src, int index) 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (isFull() && index == blocksUsed)
        {
            throw new IllegalStateException("overflow");
        }
        
        if (index == blocksUsed)
        {
            blockStarts[index] = bytes.compacityUsed();
            bytes.insert(src, bytes.compacityUsed());
            blocksUsed++;
            bytes.writeInt(blocksUsed, BLOCKS_USED_POS);
            bytes.writeInt(blockStarts[index], blockStartPos(index));
        }
        else
        {
            int diff = src.length - recordSize(index);

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
                    bytes.writeInt(blockStarts[i], blockStartPos(i));
                }
            }
        }
    }

    public void remove(int index) 
    {
        if (index < 0 || index > blocksUsed)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        int size = recordSize(index);
        
        bytes.delete(blockStarts[index], recordSize(index));
        blocksUsed--;
        bytes.writeInt(blocksUsed, BLOCKS_USED_POS);
        
        for(int i = index+1; i < blocksUsed; i++)
        {
            blockStarts[i] = (blockStarts[i+1] - size);
            bytes.writeInt(blockStarts[i], blockStartPos(i));
        }
    }

    public void insert(byte[] src, int index)
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
            bytes.writeInt(blockStarts[index], blockStartPos(index));
        }
        else
        {
            bytes.insert(src, blockStarts[index]);
            
            for(int i = blocksUsed; i > index; i--)
            {
                blockStarts[i] = blockStarts[i-1]+src.length;
                bytes.writeInt(blockStarts[i], blockStartPos(i));
            }
        }
        blocksUsed++;
        bytes.writeInt(blocksUsed, BLOCKS_USED_POS);
    }

    public boolean isFull() 
    {
        return blockStarts.length == blocksUsed;
    }
    
    public byte[] array()
    {
        return bytes.array();
    }

    public byte[] getBytes() 
    {
        return bytes.copyUsedBytes();
    }

    public RecordFile getCopy() 
    {
        return new RecordFile(
                bytes.copyUsedBytes(), 
                blocksUsed, 
                blockStarts, 
                metaLength);
    }
}
