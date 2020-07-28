/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.util.OrderedNamedValueArray;

/**
 *
 * @author REx
 */
public class FunctionSet_System extends OrderedNamedValueArray<ISystemFunction>
{

    public FunctionSet_System ()
    {
        super(10);
        /**
         * add system functions here
         */
    }
    
    @Override
    public boolean setVariable(String name, ISystemFunction value) 
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void removeVariable(String name) throws UndefinedVariableException
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addNamedValueSet(OrderedNamedValueArray<ISystemFunction> adding)
    {
        throw new UnsupportedOperationException();
    }
    
}
