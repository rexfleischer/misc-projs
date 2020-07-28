/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket;

import com.rf.socket.util.ExceptionUtil;
import com.rf.socket.util.SafeYieldingThread;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * this handles high level operations with a socket. the good thing 
 * about this class is it can be used for server or client sockets.
 * 
 * the main high level feature about this class is the internal thread
 * that handles reads. this makes the thread that actually uses an 
 * instance of SocketHandler not blocked by reads. 
 * @author REx
 */
public class SocketHandler 
{
    private class ReaderThread extends SafeYieldingThread
    {
        @Override
        protected void doSingleIteration() throws Exception
        {
            if (socket == null || socket.isClosed())
            {
                signalStop();
                return;
            }
            
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];

            int length;
            do
            {
                try
                {
                    length = in.read(buffer);
                }
                catch(IOException ex)
                {
                    setException(new SocketHandlerException("error during read", ex));
                    return;
                }
                if (length > 0)
                {
                    bytes.write(buffer, 0, length);
                }
            }
            while(length == buffer.length);
            
            synchronized(READ_LOCK)
            {
                reads.offerLast(bytes.toByteArray());
            }
        }
    }
    
    /**
     * used to lock the exception object
     */
    private final Object EXCPETION_LOCK = new Object();
    
    /**
     * for error handling with the reader thread
     */
    private SocketHandlerException exception;
    
    /**
     * used to lock the reads object
     */
    private final Object READ_LOCK = new Object();
    
    /**
     * a queue of reads
     */
    private LinkedList<byte[]> reads;
    
    /**
     * the reader thread
     */
    private ReaderThread reader;
    
    private OutputStream out;
    
    private InputStream in;
    
    private Socket socket;
    
    public SocketHandler(Socket socket) 
            throws IOException
    {
        this.socket = socket;
        this.socket.setKeepAlive(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }
    
    protected void setException(SocketHandlerException ex)
    {
        synchronized(EXCPETION_LOCK)
        {
            exception = ex;
        }
    }
    
    public SocketHandlerException getException()
    {
        synchronized(EXCPETION_LOCK)
        {
            SocketHandlerException result = exception;
            exception = null;
            return result;
        }
    }
    
    public Socket getSocket()
    {
        return socket;
    }
    
    public byte[] read() throws IOException
    {
        synchronized(READ_LOCK)
        {
            checkReader();
            return (!reads.isEmpty())
                    ? reads.removeFirst()
                    : null;
        }
    }
    
    public void write(byte[] bytes) throws IOException
    {
        out.write(bytes);
        out.flush();
    }
    
    private void checkReader()
    {
        if (reads == null)
        {
            initReader();
        }
    }
    
    public void initReader()
    {
        reads = new LinkedList<>();
        reader = new ReaderThread();
        reader.setName(Thread.currentThread().getName() + "-reader");
        reader.start();
    }
    
    public void stopReader()
    {
        if (reader == null)
        {
            try
            {
                reader.signalStop();
                reader.join();
                reads = null;
                reader = null;
            }
            catch(InterruptedException ex)
            {
                ExceptionUtil.logError(
                        String.format("could not join reader thread [%s]", socket), 
                        ex);
            }
        }
    }
    
    public void close()
    {
        stopReader();
        try
        {
            socket.close();
            socket = null;
        }
        catch(Exception ex)
        {
            ExceptionUtil.logError(
                    String.format("could not close socket [%s]", socket), 
                    ex);
        }
    }
    
    @Override
    public String toString()
    {
        return socket.toString();
    }
}
