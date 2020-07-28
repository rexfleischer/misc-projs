/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook;
import com.rf.fledrest.Param;
import com.rf.fledrest.Requirement;

/**
 *
 * @author REx
 */
@Requirement.Path.Variable("hello")
public class _single
{
    @Requirement.Method.GET
    @Requirement.Path.Variable("/world/{note}")
    @Hook.Security(RouteSecurity.class)
    public Object hello(@Param.Path("note") String note)
    {
        return String.format("hello world! %s", note);
    }
}
