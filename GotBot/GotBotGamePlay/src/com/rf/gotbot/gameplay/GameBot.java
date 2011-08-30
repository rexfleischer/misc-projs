/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.gameplay;

/**
 *
 * @author REx
 */
public interface GameBot 
{
    
    public boolean performAction(String action);
    
    public void pause(int milli);
    
}
