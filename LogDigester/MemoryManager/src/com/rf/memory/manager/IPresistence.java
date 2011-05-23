/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.manager;

import com.rf.memory.manager.presistence.SerializingPresistence;
import com.rf.memory.manager.presistence.TxtFilePresistence;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author REx
 */
public interface IPresistence
{
    public enum Presistences
    {
        TXTFILE()
        {
            public IPresistence getPresistence()
            {
                return new TxtFilePresistence();
            }
        },
        SERIALIZINGFILE()
        {
            public IPresistence getPresistence()
            {
                return new SerializingPresistence();
            }
        };

        public abstract IPresistence getPresistence();
    }

    /**
     * initalizes the object to manage a certain context... it should
     * find all of the existing
     * @param dir this doesnt mean a file system working directory... it could
     * be a url, a file directory, a database table... ect
     * @param context the context that this is in..
     */
    public void init(String dir, String context);

    /**
     * this places content into the persistence layer.
     * @param content
     */
    public void pushContent(Object content)
            throws IOException;

    /**
     * this pops the topmost content block from the stack
     * @return the content of the specified file
     * @throws IOException
     */
    public Object popContent()
            throws FileNotFoundException, IOException, ClassNotFoundException;

    /**
     * gets the content block with the given index
     * @param index
     * @return
     */
    public Object getContent(int index)
            throws FileNotFoundException, IOException, ClassNotFoundException;

    /**
     * this gets the size of the data, in bytes, that would be returned
     * if getContent was called on the same index
     * @param index
     * @return
     */
    public long getContentSize(int index);

    /**
     * this removes the content block with the given index
     * @param index
     */
    public void remove(int index);

    /**
     * deletes everything
     */
    public void clear();

    /**
     * gets the amount of content 'blocks' there are
     * @return
     */
    public int size();

    /**
     * gets the total data it is managing in bytes
     * @return
     */
    public long totalBytes();

}
