/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script;

/**
 *
 * @author REx
 */
public class FledScriptInitException extends Exception
{

    public FledScriptInitException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public FledScriptInitException (String message)
    {
        super(message);
    }

    public FledScriptInitException ()
    {
    }
    
}
