/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table;

import com.rf.dcore.table.definition.TableColumnInit;
import com.rf.dcore.table.query.TableQuery;
import com.rf.dcore.table.exception.TableException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface Table
{
    public static final String EXTENSION = "table";

    public static final String WORKING_DIRECTORY    = "working_directory";

    public static final String TABLE_NAME           = "table_name";

    public static final String TABLE_TYPE           = "table_type";

    public void init(final String master)
            throws TableException;

    public void create(
            final TableColumnInit[] definition,
            final Map<String, Object> data)
            throws TableException;

    public void close()
            throws TableException;

    public int insert(final Map<String, Object> data)
            throws TableException;

    public int update(
            final TableQuery query,
            final Map<String, Object> data)
            throws TableException;

    public int delete(final TableQuery query)
            throws TableException;

    public ArrayList<Map<String, Object>> select(final TableQuery query)
            throws TableException;
}
