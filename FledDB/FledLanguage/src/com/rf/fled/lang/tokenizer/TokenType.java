/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.tokenizer;

/**
 *
 * @author REx
 */
public enum TokenType
{
    PARATHESIS,
    COMMA     ,
    SEPERATOR ,
    
    STRING    ,
    BOOLEAN   ,
    INTEGER   ,
    DOUBLE    ,
    NULL      ,
    
    DECLARATION,
    VARIABLE   ,
    FUNCTION   ,
    OPERATOR   ,
    ASSIGNMENT;
}
