/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.interfaces.Browser;
import com.rf.fled.presistance.RecordFile;
import com.rf.fled.util.Pair;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class BPlusPage
{
    public static final String EXTENSION = "bpage";
    
    private RecordFile fileImpl;
    
    private long[] keys;

    BPlusPage(RecordFile file) throws IOException 
    {
        fileImpl = file;
        byte[] rawKeys = fileImpl.getMeta();
        keys = new long[rawKeys.length / 8];
        
        for(int i = 0, j = 0; i < rawKeys.length; i += 8, j++)
        {
            keys[j] =   (((long)(rawKeys[i]  ) << 56) |
                         ((long)(rawKeys[i+1]) << 48) |
                         ((long)(rawKeys[i+2]) << 40) |
                         ((long)(rawKeys[i+3]) << 32) |
                         ((long)(rawKeys[i+4]) << 24) |
                         ((long)(rawKeys[i+5]) << 16) |
                         ((long)(rawKeys[i+6]) <<  8) |
                         ((long)(rawKeys[i+7])));
        }
    }

    Object select(long id) 
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Browser<Pair<Long, Object>> browse() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Browser<Pair<Long, Object>> browse(long id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    RecordFile getRecordFile() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
