/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.ContentType;
import com.rf.fledrest.Requirement;

/**
 *
 * @author REx
 */
@Requirement.Path.Variable("hello")
public class _method
{
    @Requirement.Method.POST
    @Requirement.Path.Index
    @ContentType("text/html;charset=UTF-8")
    public String helloWorld()
    {
        return "hello world!";
    }
}
