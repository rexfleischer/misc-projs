/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.queue;

import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class WorkQueue_InMemory<_Ty> implements WorkQueue<_Ty>
{
    LinkedList<_Ty> queue;
    
    WorkQueue_InMemory()
    {
        queue = new LinkedList<_Ty>();
    }
    
    @Override
    public synchronized void push(_Ty value)
    {
        queue.push(value);
    }
    
    @Override
    public synchronized _Ty pop()
    {
        return queue.pollLast();
    }
    
    @Override
    public synchronized int numberOfElements()
    {
        return queue.size();
    }
}
