/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.query.interfaces;

import com.rf.logs.metrics.interfaces.IMetricCollection;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author REx
 */
public interface IMetricQuery
{
    public void doQuery(IMetricCollection metrics)
            throws FileNotFoundException, IOException, ClassNotFoundException;

    public void collide(Object result);

    public Object getResult();
}
