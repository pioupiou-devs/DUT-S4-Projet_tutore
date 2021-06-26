package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

public class DateValueFormatter extends ValueFormatter {
    @Override
    /**
     * cast la date de Float à Date pour la bibliothèque
     */
    public String getFormattedValue(float value) {
        String in = new Date(Float.valueOf(value).longValue()).toString();
        String[] parts = in.split(" ");

        //la date est remise dans un ordre français
        return parts[2] + " - " + parts[1] + " - " + parts[5] + " --- " + parts[3];
    }
}
