/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.data;

/**
 *
 * @author REx
 */
public enum TableDatas
{
    KEY_ORDERED_DATA
    {
        public TableData createTable(
                String name,
                String workingDir,
                int recordsPerFile, 
                int recordByteSize)
        {
            return null;
        }

        public TableData initTable(String master)
        {
            return null;
        }
    };

    public abstract TableData createTable(
            String name,
            String workingDir,
            int recordsPerFile,
            int recordByteSize);

    public abstract TableData initTable(String master);
}
