/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.pagination;

import com.rf.dcore.data.error.DataOrderingException;

/**
 *
 * @author REx
 */
public enum DataOrderings
{

    TS_ORDERED_PAGINATION()
    {
        public DataOrdering getInstance()
            throws DataOrderingException
        {
            return null;
        }
        public DataOrdering getInstance(
                String master)
            throws DataOrderingException
        {
            return null;
        }
        public DataOrdering getInstance(
                String indexerName,
                String workingDir,
                String namingPrefix,
                int recordPerFile,
                int recordByteSize)
            throws DataOrderingException
        {
            return null;
        }
    };

    public abstract DataOrdering getInstance()
            throws DataOrderingException;

    public abstract DataOrdering getInstance(String master)
            throws DataOrderingException;

    public abstract DataOrdering getInstance(
                    String indexerName,
                    String workingDir,
                    String namingPrefix,
                    int recordPerFile,
                    int recordByteSize)
            throws DataOrderingException;

}
