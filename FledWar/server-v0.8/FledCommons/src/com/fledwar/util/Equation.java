/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.util;

/**
 *
 * @author REx
 */
public abstract class Equation
{
    public static Equation linear(double min, double max)
    {
        return new Equation(min, max)
        {
            @Override
            protected double equate(double percent)
            {
                return (range * percent) + start;
            }
        };
    }
    
    public static Equation square(double min, double max)
    {
        return new Equation(min, max) 
        {
            @Override
            protected double equate(double percent)
            {
                return (range * percent * percent) + start;
            }
        };
    }
    
    public static Equation root(double min, double max)
    {
        return new Equation(min, max)
        {
            @Override
            protected double equate(double percent)
            {
                return (range * Math.sqrt(percent)) + start;
            }
        };
    }
    
    public static Equation power(double min, double max, double power)
    {
        return new Equation(min, max) 
        {
            private double power;
            
            protected Equation placePower(double power)
            {
                this.power = power;
                return this;
            }
            
            @Override
            protected double equate(double percent)
            {
                return (range * Math.pow(percent, power)) + start;
            }
            
        }.placePower(power);
    }
    
    protected abstract double equate(double percent);
    
    protected final double start;
    
    protected final double end;
    
    protected final double range;

    public Equation(double start, double end)
    {
        this.start = start;
        this.end = end;
        this.range = (end - start);
    }

    public double getStart()
    {
        return start;
    }

    public double getEnd()
    {
        return end;
    }

    public double getRange()
    {
        return range;
    }
    
    public double find(double percent)
    {
        if (percent < 0.0 || 1.0 < percent)
        {
            throw new IllegalArgumentException(String.format(
                    "percent must be within [0.0, 1.0]: %s",
                    percent));
        }
        return equate(percent);
    }
}
