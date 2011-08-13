/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.orderedfiles;

import com.rf.dcore.data.error.OrderedFileException;
import com.rf.dcore.util.locks.DataLock;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public enum OrderedFiles 
{
    TEST()
    {
        public OrderedFile getNewInstance()
        {
            OrderedFile result = new OrderedFile()
            {

                public void close() throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void create(String file) throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public String getFilename() throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void init(String file) throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public boolean isUsed() throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public DataLock lockFile() throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public DataLock lockSection(int location, int size) throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public ByteBuffer read(int location, int size) throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void write(int location, ByteBuffer buffer) throws OrderedFileException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
                
            };
            return result;
        }
    };

    public abstract OrderedFile getNewInstance();
}
