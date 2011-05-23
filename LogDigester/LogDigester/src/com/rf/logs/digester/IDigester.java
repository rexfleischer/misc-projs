/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester;

import com.rf.logs.digester.digest.ApacheRegexDigester;
import com.rf.logs.metrics.MetricCollection;

/**
 *
 * @author REx
 */
public interface IDigester
{
    public enum Digesters
    {
        W3CRegexDigester()
        {
            public IDigester getDigester()
            {
                return new ApacheRegexDigester();
            }
        };

        public abstract IDigester getDigester();
    }

    public MetricCollection digest(MetricCollection collection, String content);
}
