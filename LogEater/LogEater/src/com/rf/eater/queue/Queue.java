/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.queue;

/**
 *
 * @author REx
 */
public interface Queue<_Ty>
{
    public void push(_Ty value);
    
    public _Ty pop();
    
    public int numberOfElements();
}
