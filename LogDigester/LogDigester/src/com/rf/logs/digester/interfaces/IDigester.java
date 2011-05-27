/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester.interfaces;

import com.rf.logs.metrics.interfaces.IMetricCollection;

/**
 *
 * @author REx
 */
public interface IDigester
{

    public IMetricCollection digest(IMetricCollection collection, String content);

}
