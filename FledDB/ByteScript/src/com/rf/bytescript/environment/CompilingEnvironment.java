/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.exception.ByteScriptInitException;

/**
 *
 * @author REx
 */
public class CompilingEnvironment extends ExecutionEnvironment
{
    public FunctionSet_Lang langFunctions;
    
    public CompilingEnvironment() throws ByteScriptInitException
    {
        super();
        langFunctions = new FunctionSet_Lang();
    }
}
