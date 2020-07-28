/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server;

import com.fledwar.server.svc.AdminServletTest;
import com.fledwar.server.svc.UserServletTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author REx
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  FledWarServerTest.class,
  AdminServletTest.class,
  UserServletTest.class
})
public class TestSuite
{
  
}
