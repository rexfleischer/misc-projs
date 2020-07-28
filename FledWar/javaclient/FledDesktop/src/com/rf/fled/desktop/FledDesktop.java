/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop;

import com.rf.fled.desktop.app.FledApp;
import com.rf.fled.desktop.game.Game;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author REx
 */
public class FledDesktop
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable
    {
        Game game = new FledApp();
        try
        {
            game.loop();
        }
        catch(Throwable ex)
        {
            Writer out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            System.err.println(out.toString());
            try
            {
                game.shutdown();
            }
            catch(Throwable OMG)
            {
                Writer OMGout = new StringWriter();
                OMG.printStackTrace(new PrintWriter(OMGout));
                System.err.println("error while shutting down");
                System.err.println(out.toString());
            }
        }
        System.exit(0);
    }
}
