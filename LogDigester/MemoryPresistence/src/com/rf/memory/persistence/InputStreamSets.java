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
public enum InputStreamSets
{
    DirectoryRegexChecker_GZipFile()
    {
        public IInputStreamSet getInputStream(String dir, Pattern fileCheck)
        {
            return new DirectoryFileRegexChecker(dir, fileCheck, FileTypes.GZIP);
        }
    },
    DirectoryRegexChecker_TxtFile()
    {
        public IInputStreamSet getInputStream(String dir, Pattern fileCheck)
        {
            return new DirectoryFileRegexChecker(dir, fileCheck, FileTypes.TEXT);
        }
    },
    FullDirectoryGrab_GZipFile()
    {
        public IInputStreamSet getInputStream(String dir)
        {
            return new DirectoryFileRegexChecker(dir, null, FileTypes.GZIP);
        }
    },
    FullDirectoryGrab_TxtFile()
    {
        public IInputStreamSet getInputStream(String dir)
        {
            return new DirectoryFileRegexChecker(dir, null, FileTypes.TEXT);
        }
    }
}
