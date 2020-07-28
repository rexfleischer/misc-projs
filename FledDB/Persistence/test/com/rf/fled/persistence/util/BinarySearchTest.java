/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.util;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class BinarySearchTest 
{
    @Test
    public void testBinaryTreeSearch() throws Exception 
    {
        for (int l = 1; l < 50; l++)
        {
            long[] array = new long[l];
            long start = 1234;
            for(int i = 0; i < l; i++)
            {
                array[i] = start;
                start += 2;
            }
            start = 1233;
            
            if (l >= 100)
            {
                System.out.print(l + ": ");
            }
            else if (l >= 10)
            {
                System.out.print(" " + l + ": ");
            }
            else
            {
                System.out.print("  " + l + ": ");
            }
            
            for (int i = 0; i < 2*l+1; i++)
            {
                System.out.print(findFirstLessOrEqualChild(array, start) + ",");
                start++;
            }
            System.out.println();
        }
    }
    
    @Test
    public void specificCaseOne()
    {
        long[] keys = new long[]{ 12,1388,4339,6934,9421,11110,14713,18618 };
        Assert.assertEquals(5, findFirstLessOrEqualChild(keys, 11909));
    }
    
    /*
     * 12,1388,4339,6934,9421,11110,14713,18618
     */
    
    public static int findFirstLessOrEqualChild(long[] keys, long key)
    {
        int min = 0;
        int max = keys.length - 1;

        // do a binary search
        while(max > min)
        {
            int mid = (max + min) / 2;
            if (keys[mid] == key)
            {
                return mid;
            }
            if (keys[mid] > key) 
            {
                max = mid;
            }
            else 
            {
                min = mid + 1;
            }
        }
        if (keys[max] > key && max > 0)
        {
            return max - 1;
        }
        return max;
    }
}
