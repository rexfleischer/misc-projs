/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author REx
 */
class KeyBoardInputAdapter extends KeyAdapter
{
    static final int MAX_REGISTER_KEYS = 5;
    
    static final int MAX_STATES = 5;
    
    KeyBoardState[] states;
    
    int state;
    
    int[] downkeys;
    
    int position;

    KeyBoardInputAdapter()
    {
        state = 0;
        position = 0;
        downkeys = new int[MAX_REGISTER_KEYS];
        states = new KeyBoardState[MAX_STATES];
        for(int i = 0; i < MAX_STATES; i++)
        {
            states[i] = new KeyBoardState(MAX_REGISTER_KEYS);
        }
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent e) 
    {
        if ((position + 1) < downkeys.length)
        {
            downkeys[position++] = e.getKeyCode();
        }
    }
    
    @Override
    public synchronized void keyReleased(KeyEvent e) 
    {
        for(int i = 0; i < position; i++)
        {
            if (downkeys[i] == e.getKeyCode())
            {
                if (i != position)
                {
                    System.arraycopy(downkeys, 
                                        i + 1, 
                                        downkeys, 
                                        i, 
                                        downkeys.length - i - 1);
                }
                position--;
            }
        }
    }
    
    synchronized KeyBoardState createState()
    {
        KeyBoardState result = getNextState();
        
        System.arraycopy(downkeys, 0, result.codepoints, 0, downkeys.length);
        
        return result;
    }
    
    KeyBoardState getNextState()
    {
        KeyBoardState result = states[state];
        state++;
        if (state == states.length)
        {
            state = 0;
        }
        return result;
    }
}
