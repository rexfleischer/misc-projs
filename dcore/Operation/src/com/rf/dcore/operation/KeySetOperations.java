/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

import com.rf.dcore.operation.sets.KeySetAnd;
import com.rf.dcore.operation.sets.KeySetOr;

/**
 *
 * @author REx
 */
public enum KeySetOperations
{
    AND()
    {
        protected KeySetOperation getNew()
        {
            return new KeySetAnd();
        }
    },
    OR()
    {
        protected KeySetOperation getNew()
        {
            return new KeySetOr();
        }
    };

    protected abstract KeySetOperation getNew();

    private KeySetOperation instance = null;

    public KeySetOperation getOperation()
    {
        if (instance == null)
        {
            instance = getNew();
        }
        return instance;
    }
}
