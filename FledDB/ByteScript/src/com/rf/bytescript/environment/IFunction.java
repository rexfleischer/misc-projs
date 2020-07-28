/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.value.ValueType;

/**
 *
 * @author REx
 */
public interface IFunction
{
    public int getPresedence();
    
    public ValueType getType();
}
