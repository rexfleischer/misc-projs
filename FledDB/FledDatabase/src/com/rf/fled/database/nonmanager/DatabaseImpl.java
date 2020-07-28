
package com.rf.fled.database.nonmanager;

import com.rf.fled.database.DatabaseException;
import com.rf.fled.database.IDatabase;
import com.rf.fled.database.result.IResultSet;
import com.rf.fled.database.table.ITable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class DatabaseImpl implements IDatabase, Externalizable
{
    private String directory;
    
    private String dbName;
    
    private HashMap<String, ITable> tables;

    @Override
    public void init(String directory, String name, Map<String, Object> hints) 
            throws DatabaseException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getHomeFile() 
    {
        return directory + "/" + dbName + "." + EXTENSION;
    }

    @Override
    public String getName() 
    {
        return dbName;
    }

    @Override
    public void begin() 
            throws DatabaseException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit()
            throws DatabaseException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback() 
            throws DatabaseException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public IResultSet execute(String statement)
            throws DatabaseException
    {
        try
        {
            return null;
        }
        catch(Exception ex)
        {
            throw new DatabaseException("unknow error", ex);
        }
    }

    @Override
    public IResultSet query(String query) 
            throws DatabaseException
    {
        try 
        {
//            StatementSeparator statements = new StatementSeparator(query);
//            
//            if (!statements.getKeyword(0).equalsIgnoreCase("select"))
//            {
//                throw new DatabaseException("non-select statement on a query");
//            }
//            
//            // check to make sure the tables that are asked for in
//            // the query are actually there
//            if (!statements.getKeyword(1).equalsIgnoreCase("from"))
//            {
//                throw new DatabaseException("from statement out of place");
//            }
//            for(String token : statements.getTokensForWord(1))
//            {
//                if (token.equalsIgnoreCase(","))
//                {
//                    continue;
//                }
//                if (!tables.containsKey(token))
//                {
//                    throw new DatabaseException("unknown table: " + token);
//                }
//            }
//            
//            // now build the where statement
//            if (!statements.getKeyword(2).equalsIgnoreCase("where"))
//            {
//                throw new DatabaseException("where statement out of place");
//            }
//            RPNQueue whereQueue = new RPNQueue(statements.getTokensForWord(2));
//            Stack<String> stack = new Stack<String>();
//            
//            while(whereQueue.hasNext())
//            {
//                
//            }
            
            return null;
        } 
//        catch (FledSyntaxException ex) 
//        {
//            throw ex;
//        }
//        catch (DatabaseException ex)
//        {
//            throw ex;
//        }
        catch (Exception ex)
        {
            throw new DatabaseException("unexpected error occurred", ex);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) 
            throws IOException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws IOException, ClassNotFoundException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}