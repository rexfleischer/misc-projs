/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

/**
 *
 * @author REx
 */
public class KeyBoardState
{
    public static final KeyBoardState EMPTY_STATE = new KeyBoardState(
            KeyBoardInputAdapter.MAX_REGISTER_KEYS);
    
    int[] codepoints;

    KeyBoardState(int size)
    {
        codepoints = new int[size];
    }
    
    public boolean isKeyDown(int code)
    {
        return isInCodePoints(code);
    }
    
    public boolean isKeyUp(int code)
    {
        return !isInCodePoints(code);
    }
    
    public boolean isInCodePoints(int codepoint)
    {
        for(int i = 0; i < codepoints.length; i++)
        {
            if (codepoints[i] == codepoint)
            {
                return true;
            }
        }
        return false;
    }
}
