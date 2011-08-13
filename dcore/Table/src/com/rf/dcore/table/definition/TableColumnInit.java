/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.definition;

import com.rf.dcore.table.datatype.DataTypes;
import com.rf.dcore.table.indexer.TableIndexers;

/**
 *
 * @author REx
 */
public class TableColumnInit
{
    private final String name;

    private final DataTypes datatype;

    private final boolean indexed;

    private final boolean required;

    private final boolean isUnique;

    private final Object defaultValue;

    private final TableIndexers indexerType;

    public TableColumnInit(
            String name,
            DataTypes datatype,
            boolean indexed,
            boolean required,
            boolean isUnique,
            Object defaultValue,
            TableIndexers indexerType)
    {
        this.name           = name;
        this.datatype       = datatype;
        this.indexed        = indexed;
        this.required       = required;
        this.isUnique       = isUnique;
        this.defaultValue   = defaultValue;
        this.indexerType    = indexerType;
    }

    public final TableIndexers getIndexerType()
    {
        return indexerType;
    }

    public final String getName()
    {
        return name;
    }

    public final DataTypes getDatatype()
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
