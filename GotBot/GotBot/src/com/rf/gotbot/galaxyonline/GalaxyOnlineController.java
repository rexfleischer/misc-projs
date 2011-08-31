/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import com.rf.gotbot.gameplay.GameBot;
import com.rf.gotbot.input.ImageCheckAlgorithm;
import com.rf.gotbot.input.ImageCheckAlgorithms;
import com.rf.gotbot.input.ImageCheckConfig;
import com.rf.gotbot.image.transducers.RGBBufferedImageToGotBotGrey;
import com.rf.gotbot.output.SystemOutput;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author REx
 */
public class GalaxyOnlineController implements GameBot
{
    private ImageCheckAlgorithm checker;
    
    private SystemOutput output;
    
    private Robot robot;
    
    private String workingDir;
    
    public GalaxyOnlineController(String workingDir)
            throws AWTException, IOException
    {
        this(workingDir, 60, 4, 5);
    }
    
    public GalaxyOnlineController(
            String workingDir,
            int pauseMilli,
            int maxPixelsInError,
            int maxPixelDiffForError) 
            throws AWTException, IOException
    {
        this.workingDir = workingDir;
        robot = new Robot();
        output = new SystemOutput(robot, pauseMilli);
        
        checker = ImageCheckAlgorithms.BASIC_ERROR_COUNT.get(robot);
        checker.setConfig(ImageCheckConfig.MAX_PIXELS_IN_ERROR, maxPixelsInError);
        checker.setConfig(ImageCheckConfig.MAX_PIXEL_DIFF_FOR_ERROR, maxPixelDiffForError);
        
        for(GalaxyOnlineImage image : GalaxyOnlineImage.values())
        {
            putHelper(image);
        }
        
        checker.setConfig(
                ImageCheckConfig.REQUIRED_IMAGE_LIST, 
                new String[]{
                    GalaxyOnlineImage.CHECK_CLOCK.name(),
                    GalaxyOnlineImage.CHECK_CLOCK_DARK.name(),
                    GalaxyOnlineImage.CHECK_EMPTYFACE.name(),
                    GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name()
                });
    }
    
    private void putHelper(GalaxyOnlineImage putting) throws IOException
    {
        RGBBufferedImageToGotBotGrey transducer = 
                new RGBBufferedImageToGotBotGrey();
        String[] images = putting.getImages();
        for(String image : images)
        {
            checker.setImage(
                    putting.name(), 
                    transducer.transduce(
                        ImageIO.read(new File(workingDir + "/" + image + ".png")))
                    .toBufferedImage());
        }
        
        boolean[] ignores = putting.getIgnores();
        if (ignores != null)
        {
            checker.setImageConfig(
                    putting.name(),
                    ImageCheckConfig.PIXELS_TO_IGNORE,
                    ignores);
        }
    }
    
    @Override
    public void pause(int milli)
    {
        robot.delay(milli);
    }
    
    @Override
    public boolean performAction(String action)
    {
        System.out.println("initiating action: " + action);
        
        // first, check to see if the state is valid by looking
        // for the top left face and the bottom right clock thing
        try
        {
            getState(null);
        }
        catch(IllegalStateException ex)
        {
            System.err.println("error at get state: " + ex.getMessage());
            return false;
        }
        
        // now find the command in the enum
        GalaxyOnlineCommand command = null;
        try
        {
            command = GalaxyOnlineCommand.valueOf(action.toUpperCase());
        }
        catch(IllegalStateException ex)
        {
            System.err.println(
                    "an error occurred while checking the command: " 
                    + ex.getMessage());
            return false;
        }
        
        // now do what is asked
        try
        {
            switch(command)
            {
                case HARVEST_RESOURCES:
                    //harvestResources();
                    break;
                case CHECK_MAIL_FOR_INSTANCE:
                    //checkMailForInstance();
                    break;
                case DO_INSTANCE:
                    //doInstance();
                    break;
                case DO_TEST_MENUS:
                    testMenus();
                    break;
                case DO_TEST_INSTANCE:
                    testInstances();
                    break;
            }
        }
        catch(IllegalStateException ex)
        {
            System.err.println(
                    "an error occurred while initaiting the command: " 
                    + ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    //<editor-fold defaultstate="collapsed" desc="iterative helpers">
    private Map<String, ArrayList<Rectangle>> getState(GalaxyOnlineImage[] imageCheck)
    {
        checker.initNewScreen();
        
        Map<String, ArrayList<Rectangle>> state = null;
        if (imageCheck != null)
        {
            String[] imagesToCheck = new String[imageCheck.length];
            for(int i = 0; i < imageCheck.length; i++)
            {
                imagesToCheck[i] = imageCheck[i].name();
            }
            state = checker.getImages(imagesToCheck);
        }
        else
        {
            state = checker.getImages(new String[]{});
        }
        
        if ((state.containsKey(GalaxyOnlineImage.CHECK_CLOCK.name()) &&
             state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE.name())) 
                ||
            (state.containsKey(GalaxyOnlineImage.CHECK_CLOCK_DARK.name()) &&
             state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name())))
        {
            return state;
        }
        System.out.println(state);
        throw new IllegalStateException(
                "the required images are not found");
    }
    
    private boolean goToGameState(GalaxyOnlineImage goToState)
    {
        robot.delay(600);
        GalaxyOnlineImage[] theseImages = new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW, goToState};
        
        Map<String, ArrayList<Rectangle>> state = getState(theseImages);

        while(state.containsKey(GalaxyOnlineImage.EXIT_WINDOW.name()))
        {
            output.leftClick(state.get(GalaxyOnlineImage.EXIT_WINDOW.name()).get(0));
            robot.delay(600);
            state = getState(theseImages);
        }
        output.leftClick(state.get(goToState.name()).get(0));

        Rectangle rect = null;
        if (state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE.name()))
        {
            rect = state.get(GalaxyOnlineImage.CHECK_EMPTYFACE.name()).get(0);
        }
        else if(state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name()))
        {
            rect = state.get(GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name()).get(0);
        }
        else
        {
            throw new IllegalStateException("no emptyface found");
        }
        rect.y += 100;
        output.targetOnMouse(rect);
        
        return true;
    }
    
    private void doImageString(GalaxyOnlineImage[] theseImages)
    {
        for(GalaxyOnlineImage image : theseImages)
        {
            Map<String, ArrayList<Rectangle>> state = getState(new GalaxyOnlineImage[]{image});
            if (!state.containsKey(image.name()))
            {
                throw new IllegalArgumentException("image does not exist: " + image.name());
            }
            output.leftClick(state.get(image.name()).get(0));
        }
    }
    
    private void doFirstImage(GalaxyOnlineImage[] theseImages)
    {
        Map<String, ArrayList<Rectangle>> state = getState(theseImages);
        for(GalaxyOnlineImage image : theseImages)
        {
            if (state.containsKey(image.name()))
            {
                output.leftClick(state.get(image.name()).get(0));
                return;
            }
        }
        throw new IllegalArgumentException(
                "none of the asked images found: "
                + Arrays.toString(theseImages));
    }
    //</editor-fold>
    
    
//    //<editor-fold defaultstate="collapsed" desc="private void harvestResources()">
//    private void harvestResources()
//    {
//        goToGameState(GalaxyOnlineImage.MENU_GROUNDBASE);
//        robot.delay(600);
//        
//        GalaxyOnlineState state = getState(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.BUILDING_RESOURCEWAREHOUSE
//            });
//        output.leftClick(state.getRectangle(
//                GalaxyOnlineImage.BUILDING_RESOURCEWAREHOUSE));
//        robot.delay(600);
//        
//        state = getState(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.BUILDING_RESOURCEWAREHOUSE_HARVEST
//            });
//        output.leftClick(state.getRectangle(
//                GalaxyOnlineImage.BUILDING_RESOURCEWAREHOUSE_HARVEST));
//    }
//    //</editor-fold>
//    
//    
//    //<editor-fold defaultstate="collapsed" desc="private void checkMailForInstance()">
//    private void checkMailForInstance()
//    {
//        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
//        GalaxyOnlineState state = getState(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.HOT_BUTTON_CHECKMAIL
//            });
//        
//        if (!state.containsRectangle(GalaxyOnlineImage.HOT_BUTTON_CHECKMAIL))
//        {
//            throw new IllegalStateException(
//                    "not able to initiate hot button for mail");
//        }
//        
//        output.leftClick(state.getRectangle(
//                GalaxyOnlineImage.HOT_BUTTON_CHECKMAIL));
//        
//        robot.delay(600);
//        boolean gotOne = false;
//        
//        while(true)
//        {
//            try
//            {
//                robot.delay(600);
//                state = getState(new GalaxyOnlineImage[]{
//                    GalaxyOnlineImage.MENU_MYTOOLS_MAIL_INSTANCEREWARD
//                });
//                output.leftClick(state.getRectangle(
//                        GalaxyOnlineImage.MENU_MYTOOLS_MAIL_INSTANCEREWARD));
//                robot.delay(600);
//                
//                state = getState(new GalaxyOnlineImage[]{
//                    GalaxyOnlineImage.INSTANCE_REWARD_BOX,
//                    GalaxyOnlineImage.MENU_MYTOOLS_MAIL_DELETE
//                });
//                output.leftClick(state.getRectangle(
//                        GalaxyOnlineImage.INSTANCE_REWARD_BOX));
//                output.leftClick(state.getRectangle(
//                        GalaxyOnlineImage.MENU_MYTOOLS_MAIL_DELETE));
//                robot.delay(600);
//                
//                gotOne = true;
//            }
//            catch(Exception ex)
//            {
//                break;
//            }
//        }
//        
//        if (!gotOne)
//        {
//            throw new IllegalStateException(
//                    "no instance reward emails");
//        }
//        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
//    }
//    //</editor-fold>
//    
//    
//    //<editor-fold defaultstate="collapsed" desc="private void doInstance()">
//    private void doInstance()
//    {
//        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
//        robot.delay(600);
//        
//        doImageString(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.MENU_MILITARY,
//                GalaxyOnlineImage.MENU_MILITARY_SUPPLYFLEET,
//            });
//        robot.delay(600);
//        doImageString(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.MENU_MILITARY_SUPPLYFLEET_SUPPLYALL,
//                GalaxyOnlineImage.MENU_MILITARY_SUPPLYFLEET_ALLCONFIRMED,
//            });
//        robot.delay(600);
//        
//        doImageString(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.SPACE_STATION,
//                GalaxyOnlineImage.SPACE_STATION_INSTANCE
//            });
//        robot.delay(600);
//        
//        doImageString(new GalaxyOnlineImage[]{
//                GalaxyOnlineImage.INSTANCE_03,
//                GalaxyOnlineImage.SPACE_STATION_INSTANCE_INCREASEFLEET
//            });
//        robot.delay(600);
//        
//        GalaxyOnlineState state = getState(
//                new GalaxyOnlineImage[]{
//                    GalaxyOnlineImage.SPACE_STATION_INSTANCE_INCREASEFLEET_ENTERFLEET,
//                    GalaxyOnlineImage.SPACE_STATION_INSTANCE_INCREASEFLEET_OK});
//        robot.delay(600);
//        
//        ArrayList<Rectangle> rects = state.getOrderedRectangles(
//                GalaxyOnlineImage.
//                SPACE_STATION_INSTANCE_INCREASEFLEET_ENTERFLEET);
//        output.leftClick(rects.get(1));
//        output.leftClick(rects.get(2));
//        
//        output.leftClick(state.getRectangle(
//                GalaxyOnlineImage.SPACE_STATION_INSTANCE_INCREASEFLEET_OK));
//        robot.delay(600);
//        
//        state = getState(new GalaxyOnlineImage[]{
//            GalaxyOnlineImage.SPACE_STATION_INSTANCE_GO});
//        output.leftClick(state.getRectangle(
//                GalaxyOnlineImage.SPACE_STATION_INSTANCE_GO));
//        robot.delay(600);
//        
//        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
//    }
//    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="tests">
    private void testInstances()
    {
        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.SPACE_STATION,
            GalaxyOnlineImage.SPACE_STATION_INSTANCE,
            GalaxyOnlineImage.INSTANCE_01,
            GalaxyOnlineImage.INSTANCE_02,
            GalaxyOnlineImage.INSTANCE_03,
            GalaxyOnlineImage.INSTANCE_04,
            GalaxyOnlineImage.INSTANCE_05,
            GalaxyOnlineImage.INSTANCE_06,
            GalaxyOnlineImage.EXIT_WINDOW
        });
    }
    
    private void testMenus()
    {
        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_BUILD,
            GalaxyOnlineImage.MENU_BUILD_BLUEPRINT,});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_BUILD,
            GalaxyOnlineImage.MENU_BUILD_CONSTRUCT});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_BUILD,
            GalaxyOnlineImage.MENU_BUILD_RESEARCH});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_BUILD,
            GalaxyOnlineImage.MENU_BUILD_WARSHIP});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        
        
        // second menu
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_MILITARY,
            GalaxyOnlineImage.MENU_MILITARY_BUILDFLEET});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_MILITARY,
            GalaxyOnlineImage.MENU_MILITARY_COMMANDER});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_MILITARY,
            GalaxyOnlineImage.MENU_MILITARY_DESIGNSHIP});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
        robot.delay(600);
        doImageString(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.MENU_MILITARY,
            GalaxyOnlineImage.MENU_MILITARY_SUPPLYFLEET});
        robot.delay(600);
        doFirstImage(new GalaxyOnlineImage[]{
            GalaxyOnlineImage.EXIT_WINDOW,
            GalaxyOnlineImage.MENU_CANCEL});
    }
    //</editor-fold>
    
}
