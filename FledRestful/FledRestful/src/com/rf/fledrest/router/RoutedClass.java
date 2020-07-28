/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook.ClassSecurity;
import java.util.List;

/**
 *
 * @author REx
 */
public class RoutedClass
{
    public String basepath;
    
    public List<Class<? extends ClassSecurity>> security;
    
    public List<RoutedMethod> methods;
}
