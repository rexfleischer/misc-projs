/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.config;

import java.util.Map;

/**
 *
 * @author REx
 */
public class PipeDefinition
{
    public final String clazz;
    
    public final String context;
    
    public final String input;
    
    public final Map<String, String> outputs;
    
    public PipeDefinition(String clazz,
                          String context,
                          String input,
                          Map<String, String> outputs)
    {
        this.clazz      = clazz;
        this.context    = context;
        this.input      = input;
        this.outputs    = outputs;
    }
}
