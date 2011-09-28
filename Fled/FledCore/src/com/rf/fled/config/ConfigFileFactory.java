/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.config;

import com.rf.fled.language.LanguageStatements;
import com.rf.fled.engine.DataCoreEngine;
import com.rf.fled.engine.DataCoreExtension;
import com.rf.fled.exceptions.FledException;
import com.rf.fled.util.FileSerializer;
import com.rf.fled.util.MultitonFactory;
import java.io.File;

/**
 *
 * @author REx
 */
public class ConfigFileFactory extends MultitonFactory<ConfigFile>
{
    private String createNameFromContext(String context)
    {
        return DataCoreEngine.get().configDirectory()
                + "/" + context
                + "." + DataCoreExtension.CONFIG;
    }
    
    @Override
    protected boolean _contextExists(String context) 
    {
        return (new File(createNameFromContext(context))).isFile();
    }

    @Override
    protected ConfigFile _create(String context) 
            throws FledException 
    {
        try
        {
            ConfigFile result = new ConfigFile(context);
            FileSerializer.serialize(createNameFromContext(context), result);
            return result;
        }
        catch(Exception ex)
        {
            throw new FledException(
                    LanguageStatements.FILE_CANNOT_WRITE, ex);
        }
    }

    @Override
    protected void _delete(String context) 
            throws FledException 
    {
        (new File(createNameFromContext(context))).delete();
    }

    @Override
    protected ConfigFile _load(String context) 
            throws FledException 
    {
        try
        {
            return (ConfigFile) FileSerializer.unserialize(
                    createNameFromContext(context));
        }
        catch(Exception ex)
        {
            throw new FledException(
                    LanguageStatements.FILE_CANNOT_OPEN, ex);
        }
    }

    @Override
    protected void _save(String context, ConfigFile data) 
            throws FledException 
    {
        try
        {
            FileSerializer.serialize(createNameFromContext(context), data);
        }
        catch(Exception ex)
        {
            throw new FledException(
                    LanguageStatements.FILE_CANNOT_WRITE, ex);
        }
    }
}
