/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot;

import com.rf.gotbot.galaxyonline.GalaxyOnlineCommand;
import com.rf.gotbot.galaxyonline.GalaxyOnlineController;
import com.rf.gotbot.gameplay.GameBot;
import com.rf.gotbot.gameplay.GameCycle;
import com.rf.gotbot.image.util.ConvertToGreyScale;
import com.rf.gotbot.image.util.DeltaImage;
import com.rf.gotbot.image.util.ScreenShot;
import com.rf.gotbot.image.util.ShowImage;
import java.awt.Robot;
import java.awt.image.BufferedImage;

/**
 *
 * @author REx
 */
public enum GotBots 
{
    TEST_SCREEN_SHOT()
    {
        @Override
        public void main()
        {
            try
            {
//                BufferedImage image = ImageIO.read(new File(
//                        "C:/Users/REx/Desktop/galaxy online images/CHECK_CLOCK.png"));
                BufferedImage image = ScreenShot.shoot(new Robot());
                
                DeltaImage delta = new DeltaImage(image);
                //image = ConvertToGreyScale.convert(image);
                ShowImage.show(delta.toDeltaBufferedImage());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    },
    SAVE_SCREEN_SHOT()
    {

        @Override
        public void main() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    },
    GALAXY_ONLINE_2()
    {
        @Override
        public void main()
        {
            try
            {
                GameBot galaxyonline = new GalaxyOnlineController(
                            "C:/Users/REx/Desktop/galaxy online images");

                GameCycle cycle = new GameCycle(galaxyonline, 300, 1000);
                cycle.initiate(GalaxyOnlineCommand.DO_TEST_INSTANCE);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    };
    
    public abstract void main();
}
