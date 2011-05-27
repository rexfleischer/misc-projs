/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.inputstreamsets;

import com.rf.memory.persistence.FileTypes;
import com.rf.memory.persistence.interfaces.IInputStream;
import com.rf.memory.persistence.interfaces.IInputStreamSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author REx
 */
public class DirectoryFileRegexChecker extends IInputStreamSet
{
    public class TextFileInputStream extends IInputStream
    {
        protected TextFileInputStream(String context)
        {
            this.context = context;
            try
            {
                input = new FileInputStream(context);
            }
            catch(Exception ex)
            {
                exception = ex;
                input = new InputStream()
                {
                    @Override
                    public int read() throws IOException
                    {
                        return -1;
                    }
                };
            }
        }
    }

    public class GZipInputStream extends IInputStream
    {
        protected GZipInputStream(String context)
        {
            this.context = context;
            try
            {
                input = new GZIPInputStream(
                        new FileInputStream(context));
            }
            catch(Exception ex)
            {
                exception = ex;
                input = new InputStream()
                {
                    @Override
                    public int read() throws IOException
                    {
                        return -1;
                    }
                };
            }
        }
    }

    private int position;

    private FileTypes type;

    private ArrayList<String> fileNames;

    private final Object lock;

    public DirectoryFileRegexChecker(String dir, Pattern fileCheck, FileTypes type)
    {
        lock = new Object();
        this.type = type;

        // get the list of all the files in the dir
        File[] files = (new File(dir)).listFiles();
        fileNames = new ArrayList<String>();

        for (int i = 0; i < files.length; i++)
        {
            String filename = files[i].getAbsolutePath();
            if (fileCheck == null)
            {
                fileNames.add(filename);
            }
            else
            {
                Matcher matcher = fileCheck.matcher(filename);
                if (matcher.find())
                {
                    fileNames.add(filename);
                }
            }
        }
    }

    public IInputStream next()
    {
        String file = "";
        synchronized(lock)
        {
            if (fileNames.size() <= position)
            {
                return null;
            }
            file = fileNames.get(position);
            position++;
        }
        switch(type)
        {
            case TEXT:
                return new TextFileInputStream(file);
            case GZIP:
                return new GZipInputStream(file);
            default:
                return null;
        }
    }

    public ArrayList<String> getContextList()
    {
        synchronized(lock)
        {
            return (ArrayList<String>) fileNames.clone();
        }
    }

    public void reset()
    {
        synchronized(lock)
        {
            position = 0;
        }
    }
}
