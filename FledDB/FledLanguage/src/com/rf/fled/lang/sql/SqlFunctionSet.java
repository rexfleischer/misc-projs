/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.sql;

import com.rf.fled.lang.IFunction;
import com.rf.fled.lang.util.OrderedNamedValueArray;

/**
 *
 * @author REx
 */
class SqlFunctionSet 
{
    public static OrderedNamedValueArray<IFunction> get()
    {
        OrderedNamedValueArray<IFunction> result = 
                new OrderedNamedValueArray<IFunction>(10);
        
        return result;
    }
}
