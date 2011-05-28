/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics.interfaces;

import com.rf.logs.metrics.Metric;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface IMetricCollection
{
    public void init(IPersistence presistence, int maxMetricChunk);

    public void add(Metric metric) throws IOException;

    public void commit() throws IOException;

    public void beginIteration();

    public Metric next()
            throws FileNotFoundException, IOException, ClassNotFoundException;

    public ArrayList<Metric> nextChunk()
            throws FileNotFoundException, IOException, ClassNotFoundException;
}
