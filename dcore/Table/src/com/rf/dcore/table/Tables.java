/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table;

import com.rf.dcore.table.definition.TableColumnInit;
import com.rf.dcore.table.exception.TableException;
import com.rf.dcore.table.impls.StandAloneTable;
import java.util.Map;

/**
 *
 * @author REx
 */
public enum Tables
{
    STANDALONE
    {
        public Table getInstance(String master)
            throws TableException
        {
            Table result = new StandAloneTable();
            result.init(master);
            return result;
        }

        public Table getInstance(
            TableColumnInit[] definition,
            Map<String, Object> data)
            throws TableException
        {
            Table result = new StandAloneTable();
            result.create(definition, data);
            return result;
        }
    };

    public abstract Table getInstance(
            String master)
            throws TableException;

    public abstract Table getInstance(
            TableColumnInit[] definition,
            Map<String, Object> data)
            throws TableException;
}
