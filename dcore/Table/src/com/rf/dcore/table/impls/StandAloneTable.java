/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.impls;

import com.rf.dcore.table.Table;
import com.rf.dcore.table.data.TableData;
import com.rf.dcore.table.data.TableDatas;
import com.rf.dcore.table.datatype.DataTypes;
import com.rf.dcore.table.definition.TableColumn;
import com.rf.dcore.table.definition.TableColumnInit;
import com.rf.dcore.table.query.TableQuery;
import com.rf.dcore.table.exception.TableException;
import com.rf.dcore.table.indexer.TableIndexer;
import com.rf.dcore.table.indexer.TableIndexers;
import com.rf.dcore.table.query.logictree.CommandNode;
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
                String indexerType,
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
                    dataTable.getMasterFile(),
                    dataTableType.name(),
                    (String[]) indexers.keySet().toArray(),
                    indexerType.name(),
                    recordSize));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="fields">
    private String workingDir;

    private String tableName;

    private String masterFileName;

    private ArrayList<TableColumn> columns;

    private TableData dataTable;

    private TableDatas dataTableType;

    private Map<String, TableIndexer> indexers;

    private TableIndexers indexerType;

    private int recordSize;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="public StandAloneTable()">
    public StandAloneTable()
    {
        workingDir = null;
        tableName = null;
        masterFileName = null;
        columns = null;
        dataTable = null;
        indexers = null;
        dataTableType = null;
        indexerType = null;
        recordSize = -1;
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
            columns = new ArrayList<TableColumn>(definition.length);

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

            indexers = new HashMap<String, TableIndexer>();
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

            dataTable = ((TableDatas) data.get(TABLE_TYPE)).createTable(
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
            columns = new ArrayList<TableColumn>(masterData.definition.length);
            columns.addAll(Arrays.asList(masterData.definition));

            indexers = new HashMap<String, TableIndexer>(
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

            dataTable = TableDatas
                    .valueOf(masterData.dataTableType)
                    .initTable(masterData.dataMasterFile);
        }
        catch (Exception ex)
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

            dataTable.close();

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
            ByteBuffer insert = ByteBuffer.allocate(recordSize);
            for(TableColumn column : columns)
            {
                DataTypes.valueOf(column.getDatatype())
                        .getInstance()
                        .write(insert, data.get(column.getName()));
            }

            int key = dataTable.insert(insert);
            
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
            return null;
        } 
        catch (Exception ex)
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
            return 0;
        }
        catch (Exception ex)
        {
            throw new TableException(
                    "an error occurred while updating the table",
                    ex);
        }
    }
    // </editor-fold>

    
    private int[] getKeys(CommandNode[] query)
    {
        

        return null;
    }

}
