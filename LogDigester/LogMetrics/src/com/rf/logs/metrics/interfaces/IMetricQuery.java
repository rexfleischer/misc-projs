/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface IMetricQuery
{
    public Map<String, Integer> doQuery(IMetricCollection metrics)
            throws FileNotFoundException, IOException, ClassNotFoundException;
}
