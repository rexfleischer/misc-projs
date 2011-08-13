/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.indexer;

import java.util.ArrayList;

/**
 *
 * @author REx
 */
public interface TableIndexer
{
    public static final String EXTENSION = "indexer";

    public void create(String name, String workingDir, int indexesPerFile);

    public void init(String master);

    public String getMaster();

    public void close();

    public ArrayList<Integer> select_NotEqual(ArrayList<Long> comparators);

    public ArrayList<Integer> select_Equal(ArrayList<Long> comparators);

    public ArrayList<Integer> select_Range(Long comparatorLeast, Long comparatorMost);

    public ArrayList<Integer> select_GreaterThan(Long comparator);

    public ArrayList<Integer> select_LessThan(Long comparator);

    public void delete(Long comparator);

    public void delete(Integer key);

    public void insert(Long comparator, Integer key);

    public void ensureDensity(int percent);
}
