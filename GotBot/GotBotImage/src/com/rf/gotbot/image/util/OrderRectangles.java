/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.util;

import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public class OrderRectangles 
{
    public static ArrayList<Rectangle> order(ArrayList<Rectangle> list)
    {
        if (list == null)
        {
            return null;
        }
        ArrayList<Rectangle> checks = new ArrayList<Rectangle>(list);
        ArrayList<Rectangle> result = new ArrayList<Rectangle>(list.size());
        
        while(!checks.isEmpty())
        {
            int minAmount = Integer.MAX_VALUE;
            int target = -1;
            
            for (int i = 0; i < checks.size(); i++)
            {
                Rectangle rect = checks.get(i);
                if (rect.y * 10000 + rect.x < minAmount)
                {
                    minAmount = rect.y * 10000 + rect.x;
                    target = i;
                }
            }
            
            result.add(checks.remove(target));
        }
        
        return result;
    }
}
