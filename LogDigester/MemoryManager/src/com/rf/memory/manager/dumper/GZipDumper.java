/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.manager.dumper;

import com.rf.memory.manager.IDumper;
import com.rf.memory.manager.IPresistence;
import com.rf.memory.manager.LimitOutputStream;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author REx
 */
public class GZipDumper extends IDumper
{

    public void dump(String filename, IPresistence presistence, long maxKBChunk)
    {
        GZIPInputStream gzipInputStream = null;
        LimitOutputStream buffer = null;
        try
        {
            gzipInputStream = new GZIPInputStream(new FileInputStream(filename));
            buffer = new LimitOutputStream(maxKBChunk * 1024);

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = gzipInputStream.read(buf)) > 0)
            {
                buffer.write(buf, 0, len);
                if (buffer.isOverflow())
                {
                    presistence.pushContent(buffer.toString());
                    buffer.restart();
                }
            }
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        finally
        {
            try
            {
                gzipInputStream.close();
                buffer.close();
            }
            catch(Exception ex) { }
        }
    }

}
