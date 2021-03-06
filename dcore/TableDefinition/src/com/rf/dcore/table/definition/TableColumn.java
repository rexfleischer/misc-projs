/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.definition;

import com.rf.dcore.table.datatype.DataTypes;
import java.io.Serializable;

/**
 *
 * @author REx
 */
public class TableColumn implements Serializable
{
    /**
     * because primary keys are required and automatic, then it is added
     * automatically. the reason this is automatic and forced is for 
     * yet more optimizations. also, the primary keys are treated differently
     * than how indexed keys are. 
     */
    public static final TableColumn primaryKeyColumn = new TableColumn(
            "primaryKey", 
            DataTypes.INTEGER.name(), 
            true, true, true, 
            null, null);
    
    private final String name;

    private final String datatype;

    private final boolean indexed;

    private final boolean required;

    private final boolean isUnique;

    private final Object defaultValue;

    private final String indexType;
    
    public TableColumn(
            String name,
            String datatype,
            boolean indexed,
            boolean required,
            boolean isUnique,
            Object defaultValue,
            String indexType)
    {
        this.name           = name;
        this.datatype       = datatype;
        this.indexed        = indexed;
        this.required       = required;
        this.isUnique       = isUnique;
        this.defaultValue   = defaultValue;
        this.indexType      = indexType;
    }

    public final String getIndexType()
    {
        return indexType;
    }

    public final String getName()
    {
        return name;
    }

    public final String getDatatype()
    {
        return datatype;
    }

    public final boolean isIndexed()
    {
        return indexed;
    }

    public final boolean isRequired()
    {
        return required;
    }

    public final boolean isUnique()
    {
        return isUnique;
    }

    public final Object getDefaultValue()
    {
        return defaultValue;
    }
}
