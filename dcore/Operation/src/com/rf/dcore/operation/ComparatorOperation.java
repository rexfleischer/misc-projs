/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

/**
 *
 * @author REx
 */
public abstract class ComparatorOperation implements Comparable<ComparatorOperation>
{
    /*
     * this means that if one comparator has a lower order then the another,
     * then we must do the lower ordered one to try to optimize the query
     * process as much as possible.
     */
    public abstract int order();

    public int compareTo(ComparatorOperation o)
    {
        return o.order() - order();
    }
    
}
