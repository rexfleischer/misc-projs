/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester;

import com.rf.logs.digester.digest.AkamaiRegexDigester;
import com.rf.logs.digester.digest.ApacheRegexDigester;
import com.rf.logs.digester.interfaces.IDigester;

/**
 *
 * @author REx
 */
public enum Digesters
{
    W3CRegexDigester()
    {
        public IDigester getDigester()
        {
            return new ApacheRegexDigester();
        }
    },
    AkamaiDigester()
    {
        public IDigester getDigester()
        {
            return new AkamaiRegexDigester();
        }
    };

    public abstract IDigester getDigester();
}
