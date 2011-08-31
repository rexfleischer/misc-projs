/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image;

/**
 *
 * @author REx
 */
public interface GotBotTransducer<_Iy, _Oy>
{
    public _Oy transduce(_Iy input);
}
