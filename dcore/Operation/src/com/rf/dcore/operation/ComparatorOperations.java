/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

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
            return null;
        }
    },
    Equal()
    {
        protected ComparatorOperation getNew()
        {
            return null;
        }
    },
    Range()
    {
        protected ComparatorOperation getNew()
        {
            return null;
        }
    },
    GreaterThan()
    {
        protected ComparatorOperation getNew()
        {
            return null;
        }
    },
    LessThan()
    {
        protected ComparatorOperation getNew()
        {
            return null;
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
