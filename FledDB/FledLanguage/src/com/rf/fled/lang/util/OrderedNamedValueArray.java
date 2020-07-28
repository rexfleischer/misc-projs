/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.util;

/**
 *
 * @author REx
 */
public class OrderedNamedValueArray<_Ty>
{
    private class VariablePair<_Ty>
    {
        public String name;
        public _Ty value;
    }
    
    private VariablePair<_Ty>[] variables;
    
    private int compacityUsed;
    
    public OrderedNamedValueArray(int size) 
    {
        if (size < 5)
        {
            throw new IllegalArgumentException("size cannot be less than 5");
        }
        variables = new VariablePair[size];
        compacityUsed = 0;
    }
    
    public int getCompacityUsed()
    {
        return compacityUsed;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return true if it is a new variable. false if overrides another variable.
     * @throws FledExecutionException 
     */
    public boolean setVariable(String name, _Ty value) 
    {
        if (compacityUsed == 0)
        {
            VariablePair pair = new VariablePair();
            pair.name = name;
            pair.value = value;
            variables[compacityUsed++] = pair;
            return true;
        }
        else
        {
            if (compacityUsed == variables.length)
            {
                VariablePair[] newPairs = new VariablePair[compacityUsed * 2];
                System.arraycopy(variables, 0, newPairs, 0, compacityUsed);
                variables = newPairs;
            }
            int index = binarySearchFromName(name);
            if (index < 0)
            {
                index = index * -1 - 1;
                System.arraycopy(variables, 
                                 index, 
                                 variables, 
                                 index + 1, 
                                 compacityUsed - index);
                compacityUsed++;
                VariablePair pair = new VariablePair();
                pair.name = name;
                pair.value = value;
                variables[index] = pair;
                return true;
            }
            else
            {
                variables[index].value = value;
                return false;
            }
        }
    }
    
    public void removeVariable(String name) throws UndefinedVariableException
    {
        int index = binarySearchFromName(name);
        if (index < 0)
        {
            throw new UndefinedVariableException(name + " is not defined");
        }
        else
        {
            if (index != compacityUsed - 1)
            {
                System.arraycopy(variables, 
                                 index + 1, 
                                 variables, 
                                 index, 
                                 compacityUsed - index);
            }
            variables[compacityUsed--] = null;
        }
    }
    
    public boolean hasVariable(String name)
    {
        int index = binarySearchFromName(name);
        if (index < 0)
        {
            return false;
        }
        return true;
    }
    
    public _Ty getVariable(String name) throws UndefinedVariableException
    {
        int index = binarySearchFromName(name);
        if (index < 0)
        {
            throw new UndefinedVariableException(name + " is not defined");
        }
        return variables[index].value;
    }
    
    public _Ty getVariableSafe(String name)
    {
        int index = binarySearchFromName(name);
        if (index < 0)
        {
            return null;
        }
        return variables[index].value;
    }
    
    public void addNamedValueSet(OrderedNamedValueArray<_Ty> adding)
    {
        for(VariablePair<_Ty> pair : adding.variables)
        {
            this.setVariable(pair.name, pair.value);
        }
    }
    
    private int binarySearchFromName(String key) 
    {
        int low = 0;
        int high = compacityUsed - 1;

        while (low <= high) 
        {
            int mid = (low + high) >>> 1;
            VariablePair midVal = variables[mid];
            int cmp = midVal.name.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }
}
