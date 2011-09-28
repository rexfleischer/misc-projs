/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import com.rf.gotbot.gameplay.GameBot;
import com.rf.gotbot.image.GotBotImage;
import com.rf.gotbot.input.util.ImageMapToAccelerationImageMap;
import com.rf.gotbot.image.types.GotBotDeviation;
import com.rf.gotbot.input.algorithm.AccelerationComparator;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GalaxyOnlineController extends GameBot
{
    private AccelerationComparator checker;
    
    private String workingDir;
    
    public GalaxyOnlineController(String workingDir)
            throws AWTException, IOException
    {
        this(workingDir, 60);
    }
    
    public GalaxyOnlineController(String workingDir, int pauseMilli) 
            throws AWTException, IOException
    {
        super(pauseMilli);
        
        this.workingDir = workingDir;
        checker = new AccelerationComparator(robot);
        
        Map<String, GotBotImage> images = (new GalaxyOnlineLoadImageGreys())
                .loadDirectory(this.workingDir);
        
        Map<String, GotBotDeviation> deltaImages = 
                (new ImageMapToAccelerationImageMap()).transduce(images);
        
        checker.setImages(deltaImages);
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
    public Map<String, ArrayList<Rectangle>> getState(String[] imageCheck)
    {
        checker.initNewScreen();
        ArrayList<String> checking = new ArrayList<>(
                imageCheck == null ? 2 : imageCheck.length + 2);
        if (imageCheck != null)
        {
            checking.addAll(Arrays.asList(imageCheck));
        }
        
        if (!checking.contains(GO2Images.CHECK.BOTTOM))
        {
            checking.add(GO2Images.CHECK.BOTTOM);
        }
        if (!checking.contains(GO2Images.CHECK.EMPTYFACE))
        {
            checking.add(GO2Images.CHECK.EMPTYFACE);
        }
        
        String[] checkPush = new String[checking.size()];
        checkPush = checking.toArray(checkPush);
        
        Map<String, ArrayList<Rectangle>> state = checker.getImages(checkPush);
        checker.finished();
        if (state.containsKey(GO2Images.CHECK.BOTTOM) &&
                state.containsKey(GO2Images.CHECK.EMPTYFACE))
        {
            return state;
        }
        System.err.println(state);
        throw new IllegalStateException(
                "the required images are not found");
    }
    
//    private boolean goToGameState(GalaxyOnlineImage goToState)
//    {
//        robot.delay(600);
//        GalaxyOnlineImage[] theseImages = new GalaxyOnlineImage[]{
//            GalaxyOnlineImage.EXIT_WINDOW, goToState};
//        
//        Map<String, ArrayList<Rectangle>> state = getState(theseImages);
//
//        while(state.containsKey(GalaxyOnlineImage.EXIT_WINDOW.name()))
//        {
//            output.leftClick(state.get(GalaxyOnlineImage.EXIT_WINDOW.name()).get(0));
//            robot.delay(600);
//            state = getState(theseImages);
//        }
//        output.leftClick(state.get(goToState.name()).get(0));
//
//        Rectangle rect = null;
//        if (state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE.name()))
//        {
//            rect = state.get(GalaxyOnlineImage.CHECK_EMPTYFACE.name()).get(0);
//        }
//        else if(state.containsKey(GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name()))
//        {
//            rect = state.get(GalaxyOnlineImage.CHECK_EMPTYFACE_DARK.name()).get(0);
//        }
//        else
//        {
//            throw new IllegalStateException("no emptyface found");
//        }
//        rect.y += 100;
//        output.targetOnMouse(rect);
//        
//        return true;
//    }
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
//        goToGameState(GalaxyOnlineImage.MENU_SPACEBASE);
//        doImageString(new GalaxyOnlineImage[]{
//            GalaxyOnlineImage.SPACE_STATION,
//            GalaxyOnlineImage.SPACE_STATION_INSTANCE,
//            GalaxyOnlineImage.INSTANCE_01,
//            GalaxyOnlineImage.INSTANCE_02,
//            GalaxyOnlineImage.INSTANCE_03,
//            GalaxyOnlineImage.INSTANCE_04,
//            GalaxyOnlineImage.INSTANCE_05,
//            GalaxyOnlineImage.INSTANCE_06,
//            GalaxyOnlineImage.EXIT_WINDOW
//        });
    }
    
    private void testMenus()
    {
        Map<String, ArrayList<Rectangle>> state =  getState(new String[]{
            GO2Images.MENU.BUILD,
            GO2Images.MENU.GROUNDBASE,
            GO2Images.MENU.MILITARY,
            GO2Images.MENU.MYTOOLS,
            GO2Images.MENU.SOCIAL,
            GO2Images.MENU.SPACEBASE
        });
        
        if (!state.containsKey(GO2Images.MENU.BUILD) ||
            !state.containsKey(GO2Images.MENU.GROUNDBASE) ||
            !state.containsKey(GO2Images.MENU.MILITARY) ||
            !state.containsKey(GO2Images.MENU.MYTOOLS) ||
            !state.containsKey(GO2Images.MENU.SOCIAL) ||
            !state.containsKey(GO2Images.MENU.SPACEBASE))
        {
            System.err.println(state);
            throw new IllegalStateException("required state not found");
        }
        
        output.leftClick(state.get(GO2Images.MENU.BUILD).get(0));
        this.doImageString(
                new String[]{
                    GO2Images.MENU.BUILD_.BLUEPRINT,
                    GO2Images.MENU.BUILD_.BUILDING,
                    GO2Images.MENU.BUILD_.RESEARCH,
                    GO2Images.MENU.BUILD_.WARSHIP
                }, 
                false,
                false);
        robot.delay(600);
        
//        output.targetOnMouse(state.get(GO2Images.MENU.GROUNDBASE).get(0));
//        robot.delay(600);
//        
//        output.targetOnMouse(state.get(GO2Images.MENU.MILITARY).get(0));
//        robot.delay(600);
//        
//        output.targetOnMouse(state.get(GO2Images.MENU.MYTOOLS).get(0));
//        robot.delay(600);
//        
//        output.targetOnMouse(state.get(GO2Images.MENU.SOCIAL).get(0));
//        robot.delay(600);
//        
//        output.targetOnMouse(state.get(GO2Images.MENU.SPACEBASE).get(0));
//        robot.delay(600);
    }
    //</editor-fold>
    
}
