/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.db;

import com.wc.fcore.db.exception.DatabaseException;
import java.sql.ResultSet;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface DBConnect {

    public void connect(Map<String, String> info) throws DatabaseException;

    public void disconnect() throws DatabaseException;

    public void startQuery(String query) throws DatabaseException;

    public void setData(int index, Object data) throws DatabaseException;

    public ResultSet execQuery() throws DatabaseException;

    public int execUpdate() throws DatabaseException;

}
