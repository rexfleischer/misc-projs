/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation.sets;

import com.rf.dcore.operation.KeySetOperation;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class KeySetOr implements KeySetOperation
{

    public ArrayList<Integer> exec(ArrayList<Integer> left, ArrayList<Integer> right)
    {
        ArrayList<Integer> comp = null;
        ArrayList<Integer> result = null;
        Iterator<Integer> it = null;

        if (left.size() > right.size())
        {
            result = new ArrayList<Integer>(left);
            comp = left;
            it = right.listIterator();
        }
        else
        {
            result = new ArrayList<Integer>(right);
            comp = right;
            it = left.listIterator();
        }
        
        while(it.hasNext())
        {
            Integer curr = it.next();
            if (comp.indexOf(curr) == -1)
            {
                result.add(curr);
            }
        }
        
        return result;
    }
}
