/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.result;

/**
 *
 * @author REx
 */
public interface IResultSet
{
    public boolean next();
    
    public Object getObject(int column);
    
    public Object getObject(String column);
}
