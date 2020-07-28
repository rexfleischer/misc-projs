/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.FledScriptInitException;

/**
 *
 * @author REx
 */
public class CompilingEnvironment extends ExecutionEnvironment
{
    public FunctionSet_Lang langFunctions;
    
    public CompilingEnvironment() throws FledScriptInitException
    {
        super();
        langFunctions = new FunctionSet_Lang();
    }
}
