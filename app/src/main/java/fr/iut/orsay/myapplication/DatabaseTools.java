package fr.iut.orsay.myapplication;

import android.content.Context;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTools {
    final static String LOGIN = "root", PASSWORD = null;

    public static Connection openConnection(String url) throws SQLException {
        Connection co = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        co = DriverManager.getConnection(url);
        //System.out.println("coucou ! " + co.getMetaData());
        return co;
    }

    public static void getValues(Context context, int numSensor){
        //executeQuery(context.getResources().getString(R.string.get_values) + numSensor);

        //graph -> arraylist<Entry> avec temps/date et valeur
    }
}
