/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.tokenizer;

/**
 *
 * @author REx
 */
public enum TokenType
{
    PARATHESIS,
    COMMA     ,
    SEPERATOR ,
    BRACKET   ,
    
    STRING    ,
    BOOLEAN   ,
    INTEGER   ,
    DOUBLE    ,
    NULL      ,
    
    DECLARATION,
    CONSTRUCT  ,
    VARIABLE   ,
    FUNCTION   ,
    OPERATOR   ,
    ASSIGNMENT;
}
