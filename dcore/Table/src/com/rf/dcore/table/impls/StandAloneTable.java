
package com.rf.dcore.table.impls;

import com.rf.dcore.table.Table;
import com.rf.dcore.table.data.TableData;
import com.rf.dcore.table.data.TableDatas;
import com.rf.dcore.table.datatype.DataType;
import com.rf.dcore.table.datatype.DataTypes;
import com.rf.dcore.table.definition.TableColumn;
import com.rf.dcore.table.definition.TableColumnInit;
import com.rf.dcore.table.exception.TableDataAndIndexerMismatch;
import com.rf.dcore.table.keyquery.exceptions.IllegalOperationException;
import com.rf.dcore.table.keyquery.exceptions.IndexerNotFoundException;
import com.rf.dcore.table.query.TableQuery;
import com.rf.dcore.table.exception.TableException;
import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.table.indexer.TableIndexers;
import com.rf.dcore.table.keyquery.impls.NotOptimizedPrimaryKeysOnly;
import com.rf.dcore.util.FileSerializer;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author REx
 */
public class StandAloneTable implements Table
{
    
    //<editor-fold defaultstate="collapsed" desc="public class KeyDataPair">
    public class KeyDataPair
    {
        public int key;
        public Map<String, Object> data;
        
        public KeyDataPair(int key, Map<String, Object> data)
        {
            this.key    = key;
            this.data   = data;
        }
    }
    //</editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="public class TableMasterData">
    public class TableMasterData implements Serializable
    {
        public String workingDir;
        public String tableName;
        public TableColumn[] definition;
        public String dataMasterFile;
        public String dataTableType;
        public String[] indexMasterFiles;
        public int recordSize;

        public TableMasterData(
                String workingDir,
                String tableName,
                TableColumn[] definition,
                String dataMasterFile,
                String dataTableType,
                String[] indexMasterFiles,
                int recordSize)
        {
            this.workingDir = workingDir;
            this.tableName = tableName;
            this.definition = definition;
            this.dataMasterFile = dataMasterFile;
            this.dataTableType = dataTableType;
            this.indexMasterFiles = indexMasterFiles;
            this.recordSize = recordSize;
        }
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="private void saveMasterData()">
    private void saveMasterData() throws IOException
    {
        (new FileSerializer()).serialize(
                masterFileName,
                new TableMasterData(
                    workingDir,
                    tableName,
                    (TableColumn[]) columns.toArray(),
                    tableData.getMasterFile(),
                    dataTableType.name(),
                    (String[]) indexers.keySet().toArray(),
                    recordSize));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="fields">
    private String workingDir;

    private String tableName;

    private String masterFileName;

    private ArrayList<TableColumn> columns;

    private TableData tableData;

    private TableDatas dataTableType;

    private Map<String, TableIndexer> indexers;

    private int recordSize;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public StandAloneTable()">
    public StandAloneTable()
    {
        workingDir      = null;
        tableName       = null;
        masterFileName  = null;
        columns         = null;
        tableData       = null;
        indexers        = null;
        dataTableType   = null;
        recordSize      = -1;
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void create(TableColumnInit[] definition, Map<String, Object> data)">
    public void create(
            TableColumnInit[] definition,
            Map<String, Object> data)
            throws TableException
    {
        if (masterFileName != null)
        {
            throw new TableException("table is already initalize");
        }
        if (definition == null)
        {
            throw new NullPointerException("definition");
        }
        if (data == null)
        {
            throw new NullPointerException("data");
        }
        if (definition.length < 1)
        {
            throw new IllegalArgumentException(
                    "there must be at least one column");
        }

        if (data.containsKey(WORKING_DIRECTORY))
        {
            throw new IllegalArgumentException(
                    "a working directory must be specified");
        }
        if (data.containsKey(TABLE_NAME))
        {
            throw new IllegalArgumentException(
                    "a table name must be specified");
        }
        if (data.containsKey(TABLE_TYPE))
        {
            throw new IllegalArgumentException(
                    "a pagination type for data must be specified");
        }

        // check to make sure the working directory is actually
        // a directory
        if (!(new File((String) data.get(WORKING_DIRECTORY))).isDirectory())
        {
            throw new TableException(
                    workingDir + " is not a directory");
        }
        this.workingDir = (String) data.get(WORKING_DIRECTORY);

        masterFileName = workingDir + "/" + (String) data.get(TABLE_NAME) + "." + EXTENSION;
        // check to make sure a table with this name does not already
        // exist
        if ((new File(masterFileName)).isFile())
        {
            throw new TableException(
                    "table with name " + tableName + " already exists");
        }
        this.tableName = (String) data.get(TABLE_NAME);

        try
        {
            recordSize = 0;
            // plus one because the system always automatically add
            // a primary key column at the beginning
            columns = new ArrayList<>(definition.length + 1);
            columns.add(TableColumn.primaryKeyColumn);

            for (int i = 0; i < definition.length; i++)
            {
                if ( definition[i].isIndexed() &&
                    !definition[i].getDatatype().getInstance().indexable())
                {
                    throw new Exception("column " + definition[i].getName()
                            + " is not an indexable type, but is set to be indexed");
                }
                recordSize += definition[i].getDatatype().getInstance().numOfBytes();
                columns.add(new TableColumn(
                        definition[i].getName(),
                        definition[i].getDatatype().name(),
                        definition[i].isIndexed(),
                        definition[i].isRequired(),
                        definition[i].isUnique(),
                        definition[i].getDefaultValue(),
                        definition[i].getIndexerType().name()));
            }

            indexers = new HashMap<>();
            for (int i = 0; i < definition.length; i++)
            {
                if (definition[i].isIndexed())
                {
                    TableIndexer indexer = definition[i]
                            .getIndexerType()
                            .createIndexer(
                                tableName + definition[i].getName(),
                                workingDir,
                                2000);
                    indexers.put(definition[i].getName(), indexer);
                }
            }

            tableData = ((TableDatas) data.get(TABLE_TYPE)).createTable(
                    tableName,
                    workingDir,
                    2000,
                    recordSize);

            saveMasterData();
        } 
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while creating the table",
                    ex);
        }
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="public void init(String master)">
    public void init(String master)
            throws TableException
    {
        if (masterFileName != null)
        {
            throw new TableException("table is already initalize");
        }
        try
        {
            masterFileName = master;

            TableMasterData masterData = (TableMasterData)
                    (new FileSerializer()).unserialize(master);

            recordSize = masterData.recordSize;
            workingDir = masterData.workingDir;
            tableName = masterData.tableName;
            columns = new ArrayList<>(masterData.definition.length);
            columns.addAll(Arrays.asList(masterData.definition));

            indexers = new HashMap<>(
                    masterData.indexMasterFiles.length);
            for (String name : masterData.indexMasterFiles)
            {
                String indexerMaster =
                        workingDir + "/"
                        + tableName + name
                        + "." + TableIndexer.EXTENSION;
                indexers.put(name,
                        TableIndexers.valueOf(columns.get(columns.indexOf(name))
                            .getIndexType())
                            .initIndexer(indexerMaster));
            }

            tableData = TableDatas
                    .valueOf(masterData.dataTableType)
                    .initTable(masterData.dataMasterFile);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            throw new TableException(
                    "an error occurred while initiating the table",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public void close()">
    public void close()
            throws TableException
    {
        try
        {
            Iterator<String> it = indexers.keySet().iterator();
            while (it.hasNext()) {
                TableIndexer indexer = indexers.get(it.next());
                indexer.close();
            }

            tableData.close();

            saveMasterData();
        } 
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while closing the table",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public int delete(TableQuery query)">
    public int delete(TableQuery query)
            throws TableException
    {
        try
        {
            int[] keys = (new NotOptimizedPrimaryKeysOnly())
                    .queryForKeys(query.commandsArray(), indexers); 
            
            int counter = 0;
            
            for(int key : keys)
            {
                ByteBuffer rawData = tableData.delete(key);
                
                if (rawData == null)
                {
                    continue;
                }
                
                Map<String, Object> data = readyMapFromBuffer(rawData);
                counter++;
                
                Iterator<String> it = indexers.keySet().iterator();
                while(it.hasNext())
                {
                    String indexerName = it.next();
                    indexers.get(indexerName).delete(
                                    DataTypes
                                        .valueOf(indexerName)
                                        .getInstance()
                                        .convert(data.get(indexerName)), 
                                    key);
                }
            }
            
            return 0;
        } 
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while deleting data from the table",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public int insert(Map<String, Object> data)">
    public int insert(Map<String, Object> data)
            throws TableException
    {
        try
        {
            ByteBuffer insert = readyBufferFromMap(data);
            int key = tableData.insert(insert);
            
            Iterator<String> it = indexers.keySet().iterator();
            while(it.hasNext())
            {
                String indexerName = it.next();
                indexers.get(indexerName)
                        .insert(
                            DataTypes
                                .valueOf(indexerName)
                                .getInstance()
                                .convert(data.get(indexerName)),
                            key);
            }

            return key;
        } 
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while inserting data into the table",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public ArrayList<Map<String, Object>> select(TableQuery query)">
    public ArrayList<Map<String, Object>> select(TableQuery query)
            throws TableException
    {
        try
        {
            int[] keys = (new NotOptimizedPrimaryKeysOnly()).queryForKeys(
                    query.commandsArray(), indexers); 
            
            if (keys.length == 0)
            {
                return new ArrayList<>();
            }
            ArrayList<Map<String, Object>> result = new ArrayList<>(keys.length);
            
            for(int key : keys)
            {
                ByteBuffer rawData = tableData.select(key);
                if (rawData == null)
                {
                    throw new TableDataAndIndexerMismatch(
                            "key " + key + " returned no data from tableData while indexed");
                }
                result.add(readyMapFromBuffer(rawData));
            }
            
            return result;
        } 
        catch ( IllegalOperationException | 
                IndexerNotFoundException | 
                TableDataAndIndexerMismatch ex)
        {
            throw new TableException(
                    "an error occurred while selecting from the table",
                    ex);
        }
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public int update(TableQuery query, Map<String, Object> data)">
    public int update(TableQuery query, Map<String, Object> data)
            throws TableException
    {
        try
        {
            int[] keys = (new NotOptimizedPrimaryKeysOnly()).queryForKeys(
                    query.commandsArray(), indexers);
            
            String[] recordUpdates = (String[])data.keySet().toArray();
            
            for(int key : keys)
            {
                ByteBuffer rawData = tableData.select(key);
                if (rawData == null)
                {
                    throw new TableDataAndIndexerMismatch(
                            "key " + key + " returned no data from tableData while indexed");
                }
                Map<String, Object> currentData = readyMapFromBuffer(rawData);
                
                for(String update : recordUpdates)
                {
                    Object updatedColumnRecord = data.get(update);
                    if (indexers.containsKey(update))
                    {
                        TableIndexer indexer = indexers.get(update);
                        DataType datatype = DataTypes.valueOf(update).getInstance();
                        indexer.delete(
                                    datatype.convert(currentData.get(update)), 
                                    key);
                        indexer.insert(
                                    datatype.convert(updatedColumnRecord), 
                                    key);
                    }
                    currentData.put(update, updatedColumnRecord);
                }
                
                rawData = readyBufferFromMap(currentData);
                tableData.update(key, rawData);
            }
            
            return keys.length;
        }
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while updating the table",
                    ex);
        }
    }
    // </editor-fold>
    

    //<editor-fold defaultstate="collapsed" desc="buffer conversions">
    private Map<String, Object> readyMapFromBuffer(ByteBuffer data)
    {
        Map<String, Object> result = new HashMap<>(columns.size());
        
        for(TableColumn column : columns)
        {
            result.put(
                    column.getName(),
                    DataTypes
                        .valueOf(column.getDatatype())
                        .getInstance()
                        .read(data));
        }
        
        return result;
    }
    
    private ByteBuffer readyBufferFromMap(Map<String, Object> data)
    {
        ByteBuffer result = ByteBuffer.allocate(recordSize);
        for(TableColumn column : columns)
        {
            DataTypes.valueOf(column.getDatatype())
                    .getInstance()
                    .write(result, data.get(column.getName()));
        }
        return result;
    }
    //</editor-fold>
    
}
