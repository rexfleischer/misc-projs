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
public class KeySetAnd implements KeySetOperation
{

    public ArrayList<Integer> exec(ArrayList<Integer> left, ArrayList<Integer> right)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        ArrayList<Integer> comp = null;
        Iterator<Integer> it = null;

        if (left.size() > right.size())
        {
            it = right.listIterator();
            comp = left;
        }
        else
        {
            it = left.listIterator();
            comp = right;
        }

        while(it.hasNext())
        {
            Integer curr = it.next();
            if (comp.indexOf(curr) != -1)
            {
                result.add(curr);
            }
        }
        return result;
    }

}
