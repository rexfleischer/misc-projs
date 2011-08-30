/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.gameplay;

/**
 *
 * @author REx
 */
public interface CommandCycle
{
    public String currentCommand();
    
    public CommandCycle onFail();
    
    public CommandCycle onSuccess();
}
