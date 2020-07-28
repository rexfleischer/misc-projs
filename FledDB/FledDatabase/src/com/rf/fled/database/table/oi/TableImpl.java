
package com.rf.fled.database.table.oi;

import com.rf.fled.database.DatabaseException;
import com.rf.fled.database.table.TableColumn;
import com.rf.fled.database.table.TableColumnDefinition;
import com.rf.fled.database.table.DataType;
import com.rf.fled.database.table.DataTypeDefinition;
import com.rf.fled.persistence.util.LongSerializer;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.ProviderHint;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.transaction.TransactionType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.util.LongList;
import com.rf.fled.database.table.ITable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author REx
 */
public class TableImpl implements ITable, Externalizable
{
    private TableColumn[] columns;
    
    private IPersistence dataTable;
    
    private IPersistence primaryKeys;
    
    private Map<String, IPersistence> indexes;
    
    private String name;
    
    private Long maxKey;
    
    private long getCount(){ synchronized(maxKey){return ++maxKey;} }
    
    
    //<editor-fold defaultstate="collapsed" desc="public void init() create new">
    public void init(
            String directory,
            String name,
            TableColumnDefinition[] columns,
            Map<String, Object> hints)
            throws DatabaseException
    {
        this.maxKey = 0l;
        this.name   = name;
        DataTypeDefinition datatypes = this.getDataTypeDefinition();
        
        // check the columns and put them in place as we go
        this.columns = new TableColumn[columns.length];
        ArrayList<String> columnNames = new ArrayList<String>(columns.length);
        for(int i = 0; i < columns.length; i++)
        {
            if (columnNames.contains(columns[i].name))
            {
                throw new DatabaseException(
                        columns[i].name + " is defined twice");
            }
            if (columns[i].name.equalsIgnoreCase(PRIMARY_KEY))
            {
                throw new DatabaseException(
                        PRIMARY_KEY + " is reserved");
            }
            
            DataType datatype = null;
            try
            {
                datatype = datatypes.getDataType(columns[i].datatype);
            }
            catch(Exception ex)
            {
                throw new DatabaseException(
                        columns[i].datatype + " is not a valid datatype", ex);
            }
            
            if (!(datatype instanceof DataTypeImpl))
            {
                throw new DatabaseException(
                        "internal error: " + datatype +
                        " is not an instance of OrderedIndexDataType");
            }
            DataTypeImpl oDatatype = (DataTypeImpl) datatype;
            
            if (columns[i].indexed && !oDatatype.indexable())
            {
                throw new DatabaseException(
                        columns[i].name + " is marked as indexed, but datatype "
                        + columns[i].datatype + " is not indexable");
            }
            
            this.columns[i] = new TableColumn(
                    columns[i].name,
                    oDatatype,
                    columns[i].indexed,
                    columns[i].unique);
        }
        
        try
        {
            // now make the btree for the dataTable
            HashMap<String, Object> dataHints = new HashMap<String, Object>();
            dataHints.put(ProviderHint.VALUE_SERIALIZER.toString(), 
                    new ValueColumnSeralizer(this.columns));
            dataHints.put(ProviderHint.RECORDS_PER_PAGE.toString(), 256);
            dataTable = com.rf.fled.persistence.Provider.createPersistence(
                    directory,
                    this.name + ".data",
                    TreeType.BPLUSTREE,
                    RecordCacheType.NONE,
                    FileManagerType.FILE_SYSTEM_NO_TREE,
                    FileManagerCacheType.NONE,
                    TransactionType.SINGLE_WRITER_IN_MEMORY,
                    dataHints);
        }
        catch (FledPersistenceException ex)
        {
            throw new DatabaseException(
                    "error occurred while creating data tree", ex);
        }
        
        // make the indexer for the keys
        try
        {
            HashMap<String, Object> indexHints = new HashMap<String, Object>();
            indexHints.put(ProviderHint.VALUE_SERIALIZER.toString(), 
                    new LongSerializer());
            indexHints.put(ProviderHint.RECORDS_PER_PAGE.toString(), 2048);
            primaryKeys = com.rf.fled.persistence.Provider.createPersistence(
                    directory,
                    this.name + ".primarykeys",
                    TreeType.BPLUSTREE,
                    RecordCacheType.NONE,
                    FileManagerType.FILE_SYSTEM_NO_TREE,
                    FileManagerCacheType.NONE,
                    TransactionType.SINGLE_WRITER_IN_MEMORY,
                    indexHints);
        }
        catch(FledPersistenceException ex)
        {
            throw new DatabaseException(
                    "error occurred while creating the key indexer", ex);
        }
        
        // now we need to make a indexer for each of the indexed columns
        indexes = new HashMap<String, IPersistence>();
        try
        {
            for(int i = 0; i < this.columns.length; i++)
            {
                if (this.columns[i].indexed)
                {
                    // we make specific hints for the indexes
                    HashMap<String, Object> indexHints = new HashMap<String, Object>();
                    if (this.columns[i].unique)
                    {
                        indexHints.put(ProviderHint.VALUE_SERIALIZER.toString(), 
                                new LongSerializer());
                        indexHints.put(ProviderHint.RECORDS_PER_PAGE.toString(), 1024);
                    }
                    else
                    {
                        indexHints.put(ProviderHint.VALUE_SERIALIZER.toString(), 
                                new IndexBucketSerializer());
                        indexHints.put(ProviderHint.RECORDS_PER_PAGE.toString(), 256);
                    }
                    IPersistence indexer = com.rf.fled.persistence.Provider.createPersistence(
                            directory,
                            this.name + ".index." + this.columns[i].name,
                            TreeType.BPLUSTREE,
                            RecordCacheType.NONE,
                            FileManagerType.FILE_SYSTEM_NO_TREE,
                            FileManagerCacheType.NONE,
                            TransactionType.SINGLE_WRITER_IN_MEMORY,
                            indexHints);
                    indexes.put(this.columns[i].name, indexer);
                }
            }
        }
        catch (FledPersistenceException ex)
        {
            throw new DatabaseException(
                    "error occurred while creating data tree", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public void begin()">
    @Override
    public void begin()
            throws DatabaseException
    {
        try
        {
            // always lock the tables in the same order to avoid dead lock.
            // also, use the tables transactions so concurrency is not
            // implemented here to any major respect. also makes things more
            // flexable.
            dataTable.beginTransaction();
            primaryKeys.beginTransaction();
            Iterator<String> it = indexes.keySet().iterator();
            while(it.hasNext())
            {
                indexes.get(it.next()).beginTransaction();
            }
        }
        catch (FledTransactionException ex)
        {
            try
            {
                // if any fail, we need to unlock anything that has been locked..
                dataTable.rollback();
                primaryKeys.rollback();
                Iterator<String> it = indexes.keySet().iterator();
                while(it.hasNext())
                {
                    indexes.get(it.next()).rollback();
                }
            }
            catch (FledTransactionException ex1){ }
            
            throw new DatabaseException("error while initiating a transaction", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public void commit()">
    @Override
    public void commit()
            throws DatabaseException
    {
        try
        {
            // always lock the tables in the same order to avoid dead lock.
            // also, use the tables transactions so concurrency is not
            // implemented here to any major respect. also makes things more
            // flexable.
            dataTable.commit();
            primaryKeys.commit();
            Iterator<String> it = indexes.keySet().iterator();
            while(it.hasNext())
            {
                indexes.get(it.next()).commit();
            }
        }
        catch (FledTransactionException ex)
        {
            // let the programmer initiate rollbacks
            throw new DatabaseException("error during commit", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public void rollback()">
    @Override
    public void rollback()
            throws DatabaseException
    {
        try
        {
            // if any fail, we need to rollback everything
            dataTable.rollback();
            primaryKeys.rollback();
            Iterator<String> it = indexes.keySet().iterator();
            while(it.hasNext())
            {
                indexes.get(it.next()).rollback();
            }
        }
        catch (FledTransactionException ex)
        {
            throw new DatabaseException("error during rollback", ex);
        }
    }
    //</editor-fold>
    

    //<editor-fold defaultstate="collapsed" desc="public void truncate()">
    @Override
    public void truncate()
            throws DatabaseException
    {
        try
        {
            dataTable.truncate();
            primaryKeys.truncate();
            Iterator<String> it = indexes.keySet().iterator();
            while(it.hasNext())
            {
                indexes.get(it.next()).truncate();
            }
        }
        catch (FledPersistenceException ex)
        {
            throw new DatabaseException("error during truncate", ex);
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="public void close()">
    @Override
    public void close()
            throws DatabaseException
    {
        
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="public long count()">
    @Override
    public long count()
            throws DatabaseException
    {
        try
        {
            return dataTable.size();
        }
        catch (FledPersistenceException ex)
        {
            throw new DatabaseException("error while calling count()", ex);
        }
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="public DataTypeDefinition getDataTypeDefinition()">
    @Override
    public DataTypeDefinition getDataTypeDefinition()
            throws DatabaseException
    {
        return DataTypeDefinitionImpl.getInstance();
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public void drop()">
    @Override
    public void drop()
            throws DatabaseException 
    {
        try
        {
            dataTable.drop();
            primaryKeys.drop();
            
            Iterator<String> indexerNames = indexes.keySet().iterator();
            while(indexerNames.hasNext())
            {
                String index = indexerNames.next();
                IPersistence indexer = indexes.get(index);
                indexer.drop();
            }
        }
        catch(Exception ex)
        {
            throw new DatabaseException("error during drop", ex);
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="public long insert(Object[] values)">
    @Override
    public long insert(Object[] values)
            throws DatabaseException
    {
        if (values.length != columns.length)
        {
            throw new IllegalArgumentException(
                    "object count for insert does not equal amount of columns");
        }
        for(int i = 0; i < columns.length; i++)
        {
            if (values[i] == null)
            {
                throw new IllegalArgumentException(
                        columns[i].name + " cannot be null");
            }
        }
        
        long key = getCount();
        
        try
        {
            // try to insert new primary key
            if (primaryKeys.insert(key, key, false) != null)
            {
                throw new DatabaseException("duplicate key: "
                        + "key generator is out of sync");
            }
            
            // go through all of the indexes and put in
            // the values for them
            for(int i = 0; i < columns.length; i++)
            {
                if (columns[i].indexed)
                {
                    long indexKey = ((DataTypeImpl)columns[i].datatype)
                            .reduceToLong(values[i]);
                    
                    IPersistence indexer = indexes.get(columns[i].name);
                    
                    // @TODO optimize.. this implementation is slow
                    
                    // first we have to get the bucket.. which is just LongList
                    Object rawkeys = indexer.select(indexKey);
                    if (columns[i].unique)
                    {
                        if (rawkeys != null)
                        {
                            throw new DatabaseException("duplicate key: column " 
                                    + columns[i].name + " is marked unique");
                        }
                        indexer.insert(indexKey, key, true);
                    }
                    else
                    {
                        LongList keys = null;
                        if (rawkeys == null)
                        {
                            keys = new LongList();
                        }
                        else
                        {
                            keys = (LongList) rawkeys;
                        }

                        // now add the key to the arraylist
                        keys.add(key);

                        // now save it
                        indexer.insert(indexKey, keys, true);
                    }
                }
            }
            
            // insert into the main data table
            Object result = dataTable.insert(key, values, false);
            if (result != null)
            {
                throw new DatabaseException(
                        "key already exists: key generation out of sync");
            }
        }
        catch(Exception ex)
        {
            throw new DatabaseException("error during insert", ex);
        }
        
        return key;
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public void update(long id, Object[] values)">
    @Override
    public void update(long id, Object[] values)
            throws DatabaseException
    {
        if (values.length != columns.length)
        {
            throw new IllegalArgumentException(
                    "object count for insert does not equal amount of columns");
        }
        
        try
        {
            // insert into the main data table
            Object object = dataTable.select(id);
            if (object == null)
            {
                throw new DatabaseException("key does not exist");
            }
            
            Object[] result = (Object[]) object;
            
            // go through all of the indexes and put in
            // the values for them
            for(int i = 0; i < columns.length; i++)
            {
                if (values[i] != null)
                {
                    if (columns[i].indexed)
                    {
                        long newKey = ((DataTypeImpl)columns[i].datatype)
                                .reduceToLong(values[i]);
                        long oldKey = ((DataTypeImpl)columns[i].datatype)
                                .reduceToLong(result[i]);
                        if (newKey == oldKey)
                        {
                            continue;
                        }
                        
                        IPersistence indexer = indexes.get(columns[i].name);
                        
                        // @TODO optimize.. this implementation is slow
                        if (columns[i].unique)
                        {
                            indexer.delete(oldKey);
                            if (indexer.insert(newKey, id, false) != null)
                            {
                                throw new DatabaseException(
                                        "duplicate key: " + columns[i].name);
                            }
                        }
                        else
                        {
                            Object check = null;
                            
                            // first remove the old
                            check = indexer.select(oldKey);
                            if (check instanceof LongList)
                            {
                                LongList list = (LongList) check;
                                list.remove(id);
                            }
                            
                            // now insert the new
                            check = indexer.select(newKey);
                            if (check instanceof LongList)
                            {
                                LongList list = (LongList) check;
                                list.add(id);
                                indexer.update(newKey, list);
                            }
                            else
                            {
                                LongList list = new LongList();
                                list.add(id);
                                indexer.insert(newKey, list, true);
                            }
                        }
                    }
                }
            }
            
            dataTable.update(id, result);
        }
        catch(Exception ex)
        {
            throw new DatabaseException("error during insert", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public Object[] delete(long id)">
    @Override
    public Object[] delete(long id)
            throws DatabaseException
    {
        try
        {
            // get it out of the indexer and see if it exists
            if (primaryKeys.delete(id) == null)
            {
                throw new DatabaseException("key does not exist");
            }
            
            // delete the record from the dataTable. this
            // will also help with deleting from indexes
            Object[] result = (Object[]) dataTable.delete(id);
            
            if (result == null)
            {
                throw new DatabaseException("unknown error: "
                        + "record not returned after initial delete");
            }
            
            for(int i = 0; i < columns.length; i++)
            {
                if (columns[i].indexed)
                {
                    long indexKey = ((DataTypeImpl)columns[i].datatype)
                            .reduceToLong(result[i]);
                    
                    IPersistence indexer = indexes.get(columns[i].name);
                    
                    // @TODO optimize.. this implementation is slow
                    
                    // first we have to get the bucket.. which is just LongList
                    LongList rawkeys = (LongList) indexer.select(indexKey);
                    
                    // now add the key to the arraylist
                    rawkeys.remove(id);
                    
                    if (rawkeys.isEmpty())
                    {
                        indexer.delete(indexKey);
                    }
                    else
                    {
                        indexer.update(indexKey, rawkeys);
                    }
                }
            }
            
            return result;
        }
        catch(Exception ex)
        {
            throw new DatabaseException("error during delete", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public Object[] select(long id)">
    @Override
    public Object[] select(long id)
            throws DatabaseException
    {
        try
        {
            Object[] results = (Object[]) dataTable.select(id);
            Object[] result = new Object[results.length + 1];
            result[0] = id;
            System.arraycopy(results, 0, result, 1, results.length);
            return result;
        }
        catch (FledPersistenceException ex)
        {
            throw new DatabaseException("error during select", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public TableColumn[] getColumnDefinition()">
    @Override
    public TableColumn[] getColumnDefinition()
            throws DatabaseException
    {
        return columns;
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public IBrowser browseIndexer(String column, Long start)">
    @Override
    public IBrowser<KeyValuePair<Long, Object>> browseIndexer(String column, Long start)
            throws DatabaseException
    {
        IPersistence indexer = indexes.get(column);
        if (indexer == null)
        {
            throw new DatabaseException(column + " is not indexed");
        }
        try 
        {
            return indexer.browse(start);
        } 
        catch (FledPersistenceException ex) 
        {
            throw new DatabaseException("couldnt initialize browser", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="public IBrowser browseKeys(Long start)">
    @Override
    public IBrowser<KeyValuePair<Long, Object>> browseKeys(Long start)
            throws DatabaseException
    {
        try 
        {
            return primaryKeys.browse(start);
        } 
        catch (FledPersistenceException ex) 
        {
            throw new DatabaseException("couldnt initialize browser", ex);
        }
    }
    //</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="externalization">
    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException
    {
        columns = (TableColumn[]) in.readObject();
        dataTable = (IPersistence) in.readObject();
        indexes = (Map<String, IPersistence>) in.readObject();
        name = (String) in.readObject();
        maxKey = in.readLong();
    }
    
    
    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException
    {
        out.writeObject(columns);
        out.writeObject(dataTable);
        out.writeObject(indexes);
        out.writeObject(name);
        out.writeLong(maxKey);
    }
    //</editor-fold>
}