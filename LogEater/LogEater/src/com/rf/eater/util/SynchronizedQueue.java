/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.util;

import java.util.LinkedList;

/**
 * this is where data goes when they are outputted by some
 * work object and are waiting for the next work object to
 * be ready. its basically a filo synchronized list that can
 * tell a manager some type of state.
 * @author REx
 */
public class SynchronizedQueue<_Ty>
{
    LinkedList<_Ty> queue;
    
    public SynchronizedQueue()
    {
        queue = new LinkedList<_Ty>();
    }
    
    public synchronized void push(_Ty value)
    {
        queue.push(value);
    }
    
    public synchronized _Ty pop()
    {
        return queue.pollLast();
    }
    
    public synchronized int numberOfElements()
    {
        return queue.size();
    }
}
