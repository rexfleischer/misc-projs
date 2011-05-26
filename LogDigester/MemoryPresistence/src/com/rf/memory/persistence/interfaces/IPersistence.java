/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author REx
 */
public abstract class IPersistence extends Observable
{
    /**
     * this places content into the persistence layer.
     * @param content
     */
    public abstract void pushContent(Object content)
            throws IOException;

    /**
     * this pops the topmost content block from the stack
     * @return the content of the specified file
     * @throws IOException
     */
    public abstract Object popContent()
            throws FileNotFoundException, IOException, ClassNotFoundException;

    /**
     * gets the content block with the given index
     * @param index
     * @return
     */
    public abstract Object getContent(int index)
            throws FileNotFoundException, IOException, ClassNotFoundException;

    /**
     * this gets the size of the data, in bytes, that would be returned
     * if getContent was called on the same index
     * @param index
     * @return
     */
    public abstract long getContentSize(int index);

    /**
     * this removes the content block with the given index
     * @param index
     */
    public abstract void remove(int index);

    /**
     * deletes everything
     */
    public abstract void clear();

    /**
     * gets the amount of content 'blocks' there are
     * @return
     */
    public abstract int size();

    /**
     * gets the total data it is managing in bytes
     * @return
     */
    public abstract long totalBytes();

    /**
     * sets the position for iteration to the beginning
     */
    public abstract void beginIteration();

    /**
     * gets the next iterations of the content blocks
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public abstract Object next()
            throws FileNotFoundException, IOException, ClassNotFoundException;
}
