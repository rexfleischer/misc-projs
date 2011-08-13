/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.operation;

import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface KeySetOperation
{
    public  ArrayList<Integer> exec(
            ArrayList<Integer> left,
            ArrayList<Integer> right);
}
