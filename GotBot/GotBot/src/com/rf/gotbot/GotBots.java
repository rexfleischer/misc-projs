/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot;

import com.rf.gotbot.galaxyonline.GalaxyOnlineCommand;
import com.rf.gotbot.galaxyonline.GalaxyOnlineController;
import com.rf.gotbot.galaxyonline.GalaxyOnlineLoadImageDeltas;
import com.rf.gotbot.gameplay.GameBot;
import com.rf.gotbot.gameplay.GameCycle;
import com.rf.gotbot.image.transducers.GotBotGreyToDelta;
import com.rf.gotbot.image.transducers.GotBotGreyToRangedGrey;
import com.rf.gotbot.image.transducers.RGBBufferedImageToGotBotGrey;
import com.rf.gotbot.image.types.GotBotDelta;
import com.rf.gotbot.image.types.GotBotGrey;
import com.rf.gotbot.input.util.ScreenShot;
import com.rf.gotbot.output.util.ShowImage;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author REx
 */
public enum GotBots 
{
    TEST()
    {
        @Override
        public void main()
        {
            try
            {
                GotBotGrey image = 
                        (new GotBotGreyToRangedGrey()).transduce(
                            (new RGBBufferedImageToGotBotGrey())
                                .transduce(ScreenShot.shoot(new Robot())));
                
                ShowImage.show(image.toBufferedImage());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    },
    TEST_SCREEN_SHOT()
    {
        @Override
        public void main()
        {
            try
            {
                RGBBufferedImageToGotBotGrey transducer 
                        = new RGBBufferedImageToGotBotGrey();
                GotBotGrey image = transducer.transduce(
                        ScreenShot.shoot(new Robot()));
                
                ShowImage.show(image.toBufferedImage());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    },
    TEST_SCREEN_SHOT_DELTA()
    {
        @Override
        public void main()
        {
            try
            {
                RGBBufferedImageToGotBotGrey greyTransducer = 
                        new RGBBufferedImageToGotBotGrey();
                GotBotGreyToDelta deltaTransducer = 
                        new GotBotGreyToDelta();
                
                GotBotDelta image = 
                        deltaTransducer.transduce(
                            greyTransducer.transduce(
                                ScreenShot.shoot(new Robot())));
                
                ShowImage.show(image.toBufferedImage());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    },
    LIST_GALAXYONLINE_IMAGES()
    {
        @Override
        public void main()
        {
            try
            {
                Set<String> set = (new GalaxyOnlineLoadImageDeltas())
                        .loadDirectory("C:/Users/REx/Desktop/go2/")
                        .keySet();
                
                ArrayList<String> list = new ArrayList<>(set.size());
                list.addAll(set);
                Collections.sort(list);
                
                Iterator<String> it = list.iterator();
                while(it.hasNext())
                {
                    System.out.println(it.next());
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
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
                            "C:/Users/REx/Desktop/go2");

                GameCycle cycle = new GameCycle(galaxyonline, 300, 1000);
                cycle.initiate(GalaxyOnlineCommand.DO_TEST_MENUS);
            }
            catch(AWTException | IOException ex)
            {
                ex.printStackTrace();
            }
        }
    };
    
    public abstract void main();
}
