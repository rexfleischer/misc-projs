/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Param.ReverseParam;

/**
 *
 * @author REx
 */
public class RoutedParam
{
    public RoutedParam(ReverseParam type, String name)
    {
        this.type = type;
        this.name = name;
    }
    
    public ReverseParam type;
    
    public String name;
}
