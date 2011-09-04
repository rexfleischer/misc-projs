/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot;

/**
 *
 * @author REx
 */
public class StartBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            //GotBots.GALAXY_ONLINE_2.main();
            //GotBots.TEST_SCREEN_SHOT.main();
            GotBots.TEST_SCREEN_SHOT_DELTA.main();
        }
        catch(Exception ex)
        {
            System.err.println(
                    "an error occurred out of main: " + ex.getMessage());
        }
    }
}
