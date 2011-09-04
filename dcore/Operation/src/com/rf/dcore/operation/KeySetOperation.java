/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

import com.rf.dcore.util.record.IndexedRecord;

/**
 *
 * @author REx
 */
public interface KeySetOperation
{
    /**
     * left and right MUST be sorted!!!!!!
     * @param left
     * @param right
     * @return 
     */
    public  IndexedRecord[] exec(
            IndexedRecord[] left,
            IndexedRecord[] right);
}
