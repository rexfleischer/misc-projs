/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.value;

/**
 *
 * @author REx
 */
public enum ValueType
{
    VOID,
    NULL,
    BOOLEAN,
    INTEGER,
    DOUBLE,
    STRING,
    OBJECT,
    UNKNOWN, // meaning it can be any of the above
    REFERENCE,
    VARIABLE,
    CATCH, // lets the system know there is a catch here on the stack

    SYSTEM,  // used for stack popping and pushing
    
    
    /**
     * this stuff here is for compiling. 
     */
    OPERATOR,
    SEPERATOR,
    COMMA,
    FUNCTION,
    PARATHESIS,
    BRACKET,   
    ASSIGNMENT,   
    DECLARATION
}
