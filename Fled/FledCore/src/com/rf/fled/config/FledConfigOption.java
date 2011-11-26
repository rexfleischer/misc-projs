/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.config;

/**
 *
 * @author REx
 */
public enum FledConfigOption 
{
    RECORD_FILE_DEFAULT("recordfile.default"),
    RECORD_FILE_SIZE("recordfile.size"),
    
    FILE_MANAGER_DEFAULT("filemanager.default"),
    
    DATA_ROOT_DIRECTORY("data.rootdirectory"),
    
    LANGUAGE("language");
    
    private String configName;
    
    private FledConfigOption(String configName)
    {
        this.configName = configName;
    }
    
    @Override
    public String toString()
    {
        return configName;
    }
}
