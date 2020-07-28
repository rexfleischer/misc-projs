/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.sql;

/**
 * these types dont necessarily need to correlate with types
 * that get persisted to the database, these are only the types
 * that are being dealt with for comparing on querying.
 * @author REx
 */
public enum SqlValueType
{
    NULL,
    BOOLEAN,
    INTEGER,
    DOUBLE,
    STRING,
}
