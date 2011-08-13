/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class FileSearch
{
    private ArrayList<File> files;
    
    public FileSearch(String workingDir, String regax)
    {
        files = new ArrayList<File>();

        // do the actual search
        File[] workingFiles = (new File(workingDir)).listFiles();
        if (workingFiles == null)
        {
            // return if there is nothing to check
            return;
        }

        Pattern pattern = Pattern.compile(regax);
        for (int i = 0; i < workingFiles.length; i++)
        {
            // get the file directory and ready it for pattern matching
            String filename = workingFiles[i].getAbsolutePath();
            filename = filename.replace("\\", "/");
            Matcher matches = pattern.matcher(filename);
            if (matches.find())
            {
                // add file to the container
                files.add(new File(filename));
            }
        }
    }

    public ArrayList<File> getList()
    {
        return new ArrayList<File>(files);
    }
}
