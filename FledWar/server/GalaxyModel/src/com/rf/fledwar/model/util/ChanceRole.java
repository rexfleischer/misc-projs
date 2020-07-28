/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.util;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author REx
 */
public class ChanceRole
{
    private Random random;
    
    private ArrayList<Integer> marks;
    
    private ArrayList<Object> events;
    
    public ChanceRole()
    {
        this(new Random());
    }
    
    public ChanceRole(Random random)
    {
        Objects.requireNonNull(random, "random");
        this.random = random;
        this.marks = new ArrayList<>();
        this.events = new ArrayList<>();
    }
    
    public void set(int chance, Object event)
    {
        Objects.requireNonNull(event, "event");
        if (chance < 1)
        {
            throw new IllegalArgumentException("chance < 1");
        }
        marks.add(getMax() + chance);
        events.add(event);
    }
    
    public Object role()
    {
        int role = random.nextInt(getMax());
        for(int i = 0; i < marks.size(); i++)
        {
            if (role <= marks.get(i))
            {
                return events.get(i);
            }
        }
        return events.get(events.size() - 1);
    }
    
    private int getMax()
    {
        if (marks.isEmpty())
        {
            return 0;
        }
        return marks.get(marks.size() - 1);
    }
    
}
