/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence;

import com.rf.memory.persistence.interfaces.IInputStreamSet;
import com.rf.memory.persistence.inputstreamsets.DirectoryFileRegexChecker;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class InputStreamSets
{
    public static IInputStreamSet DirectoryRegexChecker_GZipFile(String dir, Pattern fileCheck)
    {
        return new DirectoryFileRegexChecker(dir, fileCheck, FileTypes.GZIP);
    }

    public static IInputStreamSet DirectoryRegexChecker_TxtFile(String dir, Pattern fileCheck)
    {
        return new DirectoryFileRegexChecker(dir, fileCheck, FileTypes.TEXT);
    }

    public static IInputStreamSet FullDirectoryGrab_GZipFile(String dir)
    {
        return new DirectoryFileRegexChecker(dir, null, FileTypes.GZIP);
    }

    public static IInputStreamSet FullDirectoryGrab_TxtFile(String dir)
    {
        return new DirectoryFileRegexChecker(dir, null, FileTypes.TEXT);
    }
}
