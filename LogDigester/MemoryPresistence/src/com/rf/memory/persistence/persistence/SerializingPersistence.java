/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.persistence;

import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class SerializingPersistence extends IPersistence
{

    public static final String EXT = "ser";

    private int counter;

    private int iterationCounter;

    private ArrayList<String> fileNames;

    private final Object infoLock;

    private final Object iterLock;

    private String context;

    private String workingDir;

    public SerializingPersistence(String dir, String uniqueKey)
    {
        infoLock = new Object();
        iterLock = new Object();
        iterationCounter = 0;
        counter = 0;
        workingDir = dir;
        workingDir = workingDir.replace("\\", "/");
        this.context = uniqueKey;
        fileNames = new ArrayList<String>();

        // do the actual search
        File[] workingFiles = (new File(workingDir)).listFiles();
        if (workingFiles.length == 0)
        {
            // return if there is nothing to check
            return;
        }

        synchronized(infoLock)
        {
            // set up the pattern
            Pattern pattern = Pattern.compile("/" + context + "([0-9]+)\\." + EXT);
            for (int i = 0; i < workingFiles.length; i++)
            {
                // get the file directory and ready it for pattern matching
                String filename = workingFiles[i].getAbsolutePath();
                filename = filename.replace("\\", "/");
                Matcher matches = pattern.matcher(filename);
                if (matches.find())
                {
                    // get the count of the match to set up for the file counter
                    int count = Integer.parseInt(matches.group(1));
                    if (counter < count)
                    {
                        counter = count;
                    }
                    // add file to the container
                    fileNames.add(filename);
                }
            }
        }
    }

    public void pushContent(Object content)
            throws IOException
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        if (content == null)
        {
            throw new NullPointerException(
                    "content cannot be null");
        }
        String filename = workingDir + "/" + context + getCounter() + "." + EXT;
        serializeObject(filename, content);
        synchronized(infoLock)
        {
            fileNames.add(filename);
        }
        this.notifyObservers(context);
    }

    public Object popContent()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        String filename = safeFileNamePop();
        Object result = deserializeObject(filename);
        synchronized(filename)
        {
            (new File(filename)).delete();
        }
        return result;
    }

    public Object getContent(int index)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        return deserializeObject(safeFileNameGet(index));
    }

    public long getContentSize(int index)
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        return (new File(safeFileNameGet(index))).length();
    }

    public void remove(int index)
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        (new File(safeFileNameRemove(index))).delete();
    }

    public void clear()
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        synchronized(infoLock)
        {
            while(!fileNames.isEmpty())
            {
                (new File(fileNames.remove(0))).delete();
            }
        }
    }

    public int size()
    {
        if (context == null)
        {
            throw new IllegalStateException(
                    "must call init before you call this method");
        }
        int size = 0;
        synchronized(infoLock)
        {
            size = fileNames.size();
        }
        return size;
    }

    public long totalBytes()
    {
        long total = 0;
        synchronized(infoLock)
        {
            Iterator<String> it = fileNames.iterator();
            while(it.hasNext())
            {
                String filename = it.next();
                total += (new File(filename)).length();
            }
        }
        return total;
    }

    private synchronized int getCounter()
    {
        counter++;
        return counter;
    }

    private String safeFileNameGet(int index)
    {
        String result = "";
        synchronized(infoLock)
        {
            result = fileNames.get(index);
        }
        return result;
    }

    private String safeFileNameRemove(int index)
    {
        String result = "";
        synchronized(infoLock)
        {
            result = fileNames.remove(index);
        }
        return result;
    }

    private String safeFileNamePop()
    {
        String result = "";
        synchronized(infoLock)
        {
            result = fileNames.remove(fileNames.size() - 1);
        }
        return result;
    }

    /**
     * this does a very fast write to a file
     * @param filename
     * @param content
     * @throws IOException
     */
    private void serializeObject(String filename, Object content)
            throws IOException
    {
        synchronized(filename)
        {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(content);
            out.close();
            fileOut.close();
        }
    }

    /**
     * this slerps a file and returns all of its content
     * @param filename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private Object deserializeObject(String filename)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if (filename == null)
        {
            throw new NullPointerException("filename");
        }
        if (filename.isEmpty())
        {
            throw new IllegalArgumentException("filename");
        }
        synchronized(filename)
        {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object result = in.readObject();
            in.close();
            fileIn.close();
            return result;
        }
    }

    public void beginIteration()
    {
        synchronized(iterLock)
        {
            iterationCounter = 0;
        }
    }

    public Object next()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        int inCount = 0;
        synchronized(iterLock)
        {
            inCount = iterationCounter;
            iterationCounter++;
        }
        return getContent(inCount);
    }
}
