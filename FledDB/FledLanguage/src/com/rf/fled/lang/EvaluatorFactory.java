/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang;

/**
 *
 * @author REx
 */
public enum EvaluatorFactory
{
    SQL()
    {
        @Override
        public IEvaluator getInstance ()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    public abstract IEvaluator getInstance();
}
