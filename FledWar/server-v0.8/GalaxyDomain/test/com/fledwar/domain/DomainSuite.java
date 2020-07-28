/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.domain;

import com.fledwar.dao.DAOFactoryTest;
import com.fledwar.dao.GalaxyScopeDAOTest;
import com.fledwar.generate.BasicClusterScripts;
import com.fledwar.generate.BasicGenerateScripts;
import com.fledwar.generate.BasicSystemScripts;
import com.fledwar.generate.SpecificGenerateTests;
import com.fledwar.vto.user.SessionIdTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author REx
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    DAOFactoryTest.class,
    GalaxyScopeDAOTest.class,
    
    BasicClusterScripts.class,
    BasicGenerateScripts.class,
    BasicSystemScripts.class,
    SpecificGenerateTests.class,
    
    SessionIdTest.class,
})
public class DomainSuite
{
    
}
