/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

import com.rf.dcore.operation.comparator.ComparatorEqual;
import com.rf.dcore.operation.comparator.ComparatorGreaterThan;
import com.rf.dcore.operation.comparator.ComparatorLessThan;
import com.rf.dcore.operation.comparator.ComparatorNotEqual;
import com.rf.dcore.operation.comparator.ComparatorRange;

/**
 *
 * @author REx
 */
public enum ComparatorOperations
{
    NotEqual()
    {
        protected ComparatorOperation getNew()
        {
            return new ComparatorNotEqual();
        }
    },
    Equal()
    {
        protected ComparatorOperation getNew()
        {
            return new ComparatorEqual();
        }
    },
    Range()
    {
        protected ComparatorOperation getNew()
        {
            return new ComparatorRange();
        }
    },
    GreaterThan()
    {
        protected ComparatorOperation getNew()
        {
            return new ComparatorGreaterThan();
        }
    },
    LessThan()
    {
        protected ComparatorOperation getNew()
        {
            return new ComparatorLessThan();
        }
    };

    protected abstract ComparatorOperation getNew();

    private ComparatorOperation instance = null;

    public ComparatorOperation getOperation()
    {
        if (instance == null)
        {
            instance = getNew();
        }
        return instance;
    }
}
