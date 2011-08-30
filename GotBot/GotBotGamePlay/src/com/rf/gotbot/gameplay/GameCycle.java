/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.gameplay;

import java.util.Random;

/**
 *
 * @author REx
 */
public class GameCycle 
{
    private GameBot bot;
    
    private int waitMin;
    
    private int waitMax;
    
    public GameCycle(GameBot bot, int waitMin, int waitMax)
    {
        if (bot == null)
        {
            throw new IllegalArgumentException();
        }
        this.bot = bot;
        this.waitMin = waitMin;
        this.waitMax = waitMax;
    }
    
    public void initiate(CommandCycle initial)
    {
        initiate(initial, Integer.MAX_VALUE);
    }
    
    public void initiate(CommandCycle initial, int commandsAllowed) 
    {
        int currentAmount = 0;
        CommandCycle current = initial;
        while(true)
        {
            currentAmount++;
            if (current == null || currentAmount > commandsAllowed)
            {
                return;
            }
            String command = current.currentCommand();
            try
            {
                if (bot.performAction(command))
                {
                    current = current.onSuccess();
                }
                else
                {
                    current = current.onFail();
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
//                System.exit(0);
                System.err.println(ex.getMessage());
                current = current.onFail();
            }
            bot.pause((new Random()).nextInt(waitMax - waitMin) + waitMin);
        }
    }
}
