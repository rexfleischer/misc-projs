/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.dumper;

import com.rf.memory.persistence.interfaces.IDumper;
import com.rf.memory.persistence.interfaces.IInputStream;
import com.rf.memory.persistence.interfaces.ILimitOutputBuffer;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.InputStream;

/**
 *
 * @author REx
 */
public class BufferedStreamDumpToPresistence implements IDumper
{
    private IPersistence presistence;
    private ILimitOutputBuffer buffer;

    public BufferedStreamDumpToPresistence(
            IPersistence presistence,
            ILimitOutputBuffer buffer)
    {
        this.presistence = presistence;
        this.buffer = buffer;
    }

    public void dump(IInputStream stream)
    {
        InputStream input = null;
        try
        {
            input = stream.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = input.read(buf)) > 0)
            {
                buffer.write(buf, 0, len);
                if (buffer.isOverflow())
                {
                    presistence.pushContent(buffer.toNormalizedString());
                }
            }
            presistence.pushContent(buffer.toString());
            buffer.restart();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        finally
        {
            try
            {
                input.close();
                buffer.close();
            }
            catch(Exception ex) { }
        }
    }

}
