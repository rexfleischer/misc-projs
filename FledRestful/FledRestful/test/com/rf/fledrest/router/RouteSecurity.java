/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook;

/**
 *
 * @author REx
 */
public class RouteSecurity implements Hook.ClassSecurity
{

    @Override
    public boolean allowed(Object pojo)
    {
        System.out.println("allowed() called");
        return true;
    }
    
}
