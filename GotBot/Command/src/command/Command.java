/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import com.rf.gotbot.GotBots;

/**
 *
 * @author REx
 */
public class Command 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            //GotBots.GALAXY_ONLINE_2.main();
            GotBots.TEST_SCREEN_SHOT.main();
        }
        catch(Exception ex)
        {
            System.err.println(
                    "an error occurred out of main: " + ex.getMessage());
        }
    }
}
