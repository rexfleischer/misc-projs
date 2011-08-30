/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

/**
 *
 * @author REx
 */
public enum GalaxyOnlineImage 
{
    
    BUILDING_RESOURCEWAREHOUSE,
    BUILDING_RESOURCEWAREHOUSE_HARVEST,
    
    CHECK_CLOCK,
    CHECK_CLOCK_DARK,
    CHECK_EMPTYFACE,
    CHECK_EMPTYFACE_DARK,
    
    MENU_BUILD,
    MENU_BUILD_BLUEPRINT,
    MENU_BUILD_WARSHIP,
    MENU_BUILD_RESEARCH,
    MENU_BUILD_CONSTRUCT,
    
    MENU_MILITARY,
    MENU_MILITARY_BUILDFLEET,
    MENU_MILITARY_COMMANDER,
    MENU_MILITARY_DESIGNSHIP,
    MENU_MILITARY_SUPPLYFLEET,
    MENU_MILITARY_SUPPLYFLEET_ALLCONFIRMED,
    MENU_MILITARY_SUPPLYFLEET_SUPPLYALL,
    
    MENU_MYTOOLS,
    MENU_MYTOOLS_BAG,
    MENU_MYTOOLS_MAIL,
    MENU_MYTOOLS_MAIL_DELETE,
    MENU_MYTOOLS_MAIL_INSTANCEREWARD,
    MENU_MYTOOLS_QUEST,
    MENU_MYTOOLS_TERRITORY,
    
    MENU_SOCIAL,
    
    MENU_SPACEBASE,
    
    MENU_GROUNDBASE,
    MENU_CANCEL,
    MENU_COMPLETE,
    
    EXIT_WINDOW()
    {
        @Override public boolean[] getIgnores()
        {
            return new boolean[]{
                        true, true, true, true, false,true, true, true, true,
                        true, true, true, true, true, true, true, true, true,
                        true, true, true, true, true, true, true, true, true,
                        true, true, true, false,false,false,true, true, true,  
                        false,true, true, true, false,true, true, true, false,
                        true, true, true, false,false,false,true, true, true,
                        true, true, true, true, true, true, true, true, true,
                        true, true, true, true, true, true, true, true, true,
                        true, true, true, true, false,true, true, true, true};
        }
    },
                    
    HOT_BUTTON_CHECKMAIL,
    
    SPACE_STATION,
    SPACE_STATION_ENTER,
    SPACE_STATION_INSTANCE,
    SPACE_STATION_INSTANCE_GO,
    SPACE_STATION_INSTANCE_INCREASEFLEET,
    SPACE_STATION_INSTANCE_INCREASEFLEET_ENTERFLEET,
    SPACE_STATION_INSTANCE_INCREASEFLEET_OK,
    SPACE_STATION_LEAGUE,
    INSTANCE_01,
    INSTANCE_02,
    INSTANCE_03,
    INSTANCE_04,
    INSTANCE_05,
    INSTANCE_06,
    INSTANCE_REWARD_BOX();
    
    
    public String[] getImages()
    {
        return new String[]{ name() };
    }
    
    public boolean[] getIgnores()
    {
        return null;
    }
}
