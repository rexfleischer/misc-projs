/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.limitoutputbuffer;

import com.rf.memory.persistence.interfaces.ILimitOutputBuffer;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class RegexLineEndStream extends ILimitOutputBuffer
{
    private Pattern pattern;

    private StringBuilder string;

    private long maxSize;

    private long sizeAt;
    
    public RegexLineEndStream(long maxSize)
    {
        if (maxSize < 1)
        {
            throw new IllegalArgumentException("maxSize must be greater then 1");
        }
        this.string  = new StringBuilder();
        this.maxSize = maxSize;
        this.sizeAt = 0;
        this.pattern = Pattern.compile("^(.*)\\z", Pattern.MULTILINE);
    }

    @Override
    public void write(int b) throws IOException
    {
        this.string.append((char)b);
        sizeAt++;
    }

    @Override
    public boolean isOverflow()
    {
        return maxSize <= sizeAt;
    }

    @Override
    public void restart()
    {
        sizeAt = 0;
        string  = new StringBuilder();
    }

    @Override
    public String toNormalizedString()
    {
        Matcher matcher = pattern.matcher(string);
        StringBuilder result = string;
        if (matcher.find())
        {
            String match = matcher.group(1);
            string.delete(
                    string.length() - match.length() - 1,
                    string.length()
                );
            string = new StringBuilder(match);
            sizeAt = string.length();
        }
        return result.toString();
    }

    @Override
    public String toString()
    {
        return this.string.toString();
    }

}
