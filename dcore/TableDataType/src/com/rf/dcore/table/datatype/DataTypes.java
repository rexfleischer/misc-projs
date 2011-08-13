/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.datatype;

import com.rf.dcore.table.datatype.impls.DataTypeBoolean;
import com.rf.dcore.table.datatype.impls.DataTypeDate;
import com.rf.dcore.table.datatype.impls.DataTypeInteger;
import com.rf.dcore.table.datatype.impls.DataTypeLong;
import com.rf.dcore.table.datatype.impls.DataTypeString;
import com.rf.dcore.table.datatype.impls.DataTypeText;

/**
 *
 * @author REx
 */
public enum DataTypes
{
    BOOLEAN()
    {
        protected DataType getDataType()
        {
            return new DataTypeBoolean();
        }
    },
    INTEGER()
    {
        protected DataType getDataType()
        {
            return new DataTypeInteger();
        }
    },
    LONG()
    {
        protected DataType getDataType()
        {
            return new DataTypeLong();
        }
    },
    DATETIME()
    {
        protected DataType getDataType()
        {
            return new DataTypeDate();
        }
    },
    STRING()
    {
        protected DataType getDataType()
        {
            return new DataTypeString();
        }
    },
    TEXT()
    {
        protected DataType getDataType()
        {
            return new DataTypeText();
        }
    };

    protected abstract DataType getDataType();

    private DataType instance = null;

    public DataType getInstance()
    {
        if (instance == null)
        {
            instance = getDataType();
        }
        return instance;
    }
}
