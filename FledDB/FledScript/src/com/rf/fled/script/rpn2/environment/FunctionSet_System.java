/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.language.FunctionSet;
import java.util.List;

/**
 *
 * @author REx
 */
public class FunctionSet_System extends FunctionSet
{

    @Override
    protected void getFunctionList (List<FunctionPair> buffer)
    {
        
    }
    
    @Override
    public ISystemFunction getFunctionSafe(String alias)
    {
        return (ISystemFunction) super.getFunctionSafe(alias);
    }
    
}
