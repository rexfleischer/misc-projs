/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.indexer;

/**
 *
 * @author REx
 */
public enum TableIndexers
{

    TS_ORDERED_INDEX
    {
        public TableIndexer createIndexer(String name, String workingDir, int indexesPerFile)
        {
            return null;
        }

        public TableIndexer initIndexer(String master)
        {
            return null;
        }
    };
    
    public abstract TableIndexer createIndexer(String name, String workingDir, int indexesPerFile);

    public abstract TableIndexer initIndexer(String master);

}
