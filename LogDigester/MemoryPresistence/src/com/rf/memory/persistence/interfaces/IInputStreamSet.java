/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.interfaces;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public abstract class IInputStreamSet implements Iterator<IInputStream>
{
    public abstract ArrayList<String> getContextList();

    public abstract void reset();

    /*
     * the reason this method is not implemented is because of a threading
     * issue that comes up. if one thread calls this before it can call
     * next on the last one while another thread grabs the last one instread,
     * null must be returned from the next() call anyways. so, how this
     * must be checked to see if there is a next is by seeing if null has
     * been returned by next()
     */
    public final boolean hasNext()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * no need for this
     */
    public final void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
