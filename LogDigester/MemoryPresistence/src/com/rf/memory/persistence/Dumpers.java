/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence;

import com.rf.memory.persistence.dumper.BufferedStreamDumpToPresistence;
import com.rf.memory.persistence.interfaces.IDumper;
import com.rf.memory.persistence.interfaces.ILimitOutputBuffer;
import com.rf.memory.persistence.interfaces.IPersistence;

/**
 *
 * @author REx
 */
public enum Dumpers
{
    BufferedStreamDump()
    {
        public IDumper getDumper(IPersistence presistence, ILimitOutputBuffer buffer)
        {
            return new BufferedStreamDumpToPresistence(presistence, buffer);
        }
    };
}
