/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author REx
 */
public class Metric implements Serializable
{
    public String   IP;
    public String   request;
    public Date     date;
    public Integer  time;
    public String   error;

    public Metric()
    {
        IP      = null;
        request = null;
        date    = null;
        time    = 0;
        error   = null;
    }

    public Metric(String IP, String request, String date, String time)
    {
        this.IP      = IP;
        this.request = request;
        parseDate(date);
        parseTime(time);
    }

    public final void parseDate(String date)
    {
        try
        {
            this.date = (new SimpleDateFormat("dd/MMM/yyyy")).parse(date);
        }
        catch(ParseException ex)
        {
            this.error = ex.getMessage();
        }
    }

    public final void parseTime(String time)
    {
        String[] parts = time.split("\\:");
        if (parts.length >= 1)
        {
            this.time = Integer.parseInt(parts[0]) * 60 * 60;
        }
        if (parts.length >= 2)
        {
            this.time += Integer.parseInt(parts[1]) * 60;
        }
        if (parts.length >= 3)
        {
            this.time += Integer.parseInt(parts[2]);
        }
    }

    @Override
    public String toString()
    {
        return "[IP:" + IP +
                ",request:" + request +
                ",date:" + date +
                ",time:" + time +
                ",error:" + error;
    }
}
