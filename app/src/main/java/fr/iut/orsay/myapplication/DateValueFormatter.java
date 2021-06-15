package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

public class DateValueFormatter extends ValueFormatter
    {
        @Override public String getFormattedValue(float value)
            {
                return new Date(new Float(value).longValue()).toString();
            }
    }