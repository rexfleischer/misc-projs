/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import java.util.Map;

/**
 *
 * @author REx
 */
public class FileManagerFactory 
{
    public static IFileManager create(
            FileManagerContext context, 
            Map<String, Object> hints)
    {
        IFileManager result = null;
        switch(context.managerType)
        {
            case FILE_SYSTEM_NO_TREE:
                result = new FileManager_FileSystemNoTree(
                        context.directory, 
                        context.context,
                        context.counter);
                break;
            case IN_MEMORY:
                result = new FileManager_InMemory();
                break;
            default:
                throw new IllegalArgumentException("context.managerType");
        }
        
        switch(context.cacheType)
        {
            case MEMORY:
                result = new FileManagerCache_InMemory(result);
                break;
            case NONE:
                // nothing to do
                break;
            default:
                throw new IllegalArgumentException("context.cacheType");
        }
        return result;
    }
}
