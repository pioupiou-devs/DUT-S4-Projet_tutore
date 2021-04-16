package fr.iut.orsay.myapplication;

import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;

import com.github.mikephil.charting.data.Entry;

public class DatabaseTools {
    final static String LOGIN = "root", PASSWORD = null;

    public static Connection openConnection(String url) throws SQLException {
        Connection co = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        co = DriverManager.getConnection(url);
        System.out.println("coucou ! " + co.getMetaData());
        return co;
    }

    public static ArrayList<Entry> getValues(int numSensor, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, numSensor);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Entry> values = new ArrayList<>();
        while(resultSet.next()) {
            values.add(new Entry(Float.parseFloat(resultSet.getString(1)), resultSet.getTimestamp(2))); //TODO : problème de compatibilité entre les Entry et les DateTime
        }
        return values;
    }

    public static ArrayList<Entry> getValues(int numSensor, PreparedStatement preparedStatement, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException { //surcharge pour la spécification d'une plage de dates
        preparedStatement.setInt(1, numSensor);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(String.valueOf(dateMax)));
        preparedStatement.setTimestamp(3, Timestamp.valueOf(String.valueOf(dateMin)));

        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Entry> values = new ArrayList<>();
        while(resultSet.next()) {
            values.add(new Entry(Float.parseFloat(resultSet.getString(1)), resultSet.getTimestamp(2))); //TODO : problème de compatibilité entre les Entry et les DateTime
        }
        return values;
    }
}
