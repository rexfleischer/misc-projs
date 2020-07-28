/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.sql;

import com.rf.fled.lang.IFunction;

/**
 *
 * @author REx
 */
public interface ISqlFunction extends IFunction
{

    public SqlValue compute (SqlValue[] params);
    
}
