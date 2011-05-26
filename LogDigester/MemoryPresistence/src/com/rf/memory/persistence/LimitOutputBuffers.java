/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence;

import com.rf.memory.persistence.interfaces.ILimitOutputBuffer;
import com.rf.memory.persistence.limitoutputbuffer.RegexLineEndStream;

/**
 *
 * @author REx
 */
public enum LimitOutputBuffers
{
    RegexLineEnd()
    {
        public ILimitOutputBuffer getOutputBuffer(long maxSize)
        {
            return new RegexLineEndStream(maxSize);
        }
    };

    public abstract ILimitOutputBuffer getOutputBuffer(long maxSize);
}
