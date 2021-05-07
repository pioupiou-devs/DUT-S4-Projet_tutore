package fr.iut.orsay.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;

import com.github.mikephil.charting.data.Entry;

public class DatabaseTools {
    public static Connection openConnection(String url) throws SQLException, ClassNotFoundException {
        Connection co = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Class.forName("org.mariadb.jdbc.Driver");
        co = DriverManager.getConnection(url);
        System.out.println("coucou ! " + co.getMetaData());
        return co;
    }

    public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, numSensor);
        preparedStatement.setInt(2, numIPSO);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Entry> values = new ArrayList<>();
        int i = 1;
        while(resultSet.next()) {
            //values.add(new Entry(resultSet.getTimestamp(1), Float.parseFloat(resultSet.getString(2)))); //TODO : problème de compatibilité entre les Entry et les DateTime
            values.add(new Entry(new Long(resultSet.getTimestamp(1).getTime()).floatValue(), Float.parseFloat(resultSet.getString(2)))); //TODO : problème de compatibilité entre les Entry et les DateTime
            i++;
        }
        return values;
    }

    /*public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException { //surcharge pour la spécification d'une plage de dates
        preparedStatement.setInt(1, numSensor);
        preparedStatement.setInt(2, numIPSO);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(String.valueOf(dateMax)));
        preparedStatement.setTimestamp(3, Timestamp.valueOf(String.valueOf(dateMin)));

        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Entry> values = new ArrayList<>();
        while(resultSet.next()) {
            values.add(new Entry(resultSet.getTimestamp(1), Float.parseFloat(resultSet.getString(2)))); //TODO : problème de compatibilité entre les Entry et les DateTime
        }
        return values;
    }*/

    //call this method when radio button "type" is selected, it returns the list of all types
    //https://www.tutorialspoint.com/how-can-i-add-items-to-a-spinner-in-android
    public static ArrayList<String> getTypes(Connection co, Context context) throws SQLException {
        ArrayList<String> listTypes = new ArrayList<>();

        Statement statement = co.createStatement();
        ResultSet resultSet = statement.executeQuery(context.getResources().getString(R.string.get_types));

        while (resultSet.next()){
            listTypes.add(resultSet.getInt(1) + " - " + resultSet.getString(2) + " (" + resultSet.getString(3) + ")");
        }
        return listTypes;
    }

    //call this method to get types from a specified sensor
    public static ArrayList<String> getTypes(int numSensor, PreparedStatement preparedStatement) throws SQLException {
        ArrayList<String> listTypes = new ArrayList<>();

        preparedStatement.setInt(1, numSensor);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            listTypes.add(resultSet.getInt(1) + " - " + resultSet.getString(2) + " (" + resultSet.getString(3) + ")");
        }
        return listTypes;
    }

    //call this method when radio button "sensor" is selected, it returns the list of all sensors
    public static ArrayList<String> getSensors(Connection co, Context context) throws SQLException{
        ArrayList<String> listSensors = new ArrayList<>();

        Statement statement = co.createStatement();
        ResultSet resultSet = statement.executeQuery(context.getResources().getString(R.string.get_sensors));

        while (resultSet.next()){
            if(resultSet.getString(2) != "") {
                listSensors.add(resultSet.getInt(1) + " - " + resultSet.getString(2));
            }
            else {
                listSensors.add(String.valueOf(resultSet.getInt(1)));
            }
        }
        return listSensors;
    }

    //call this method to get sensors with a specified sensor
    public static ArrayList<String> getSensors(int numIPSO, PreparedStatement preparedStatement) throws SQLException{
        ArrayList<String> listSensors = new ArrayList<>();

        preparedStatement.setInt(1, numIPSO);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            if(resultSet.getString(2) != "") {
                listSensors.add(resultSet.getInt(1) + " - " + resultSet.getString(2));
            }
            else {
                listSensors.add(String.valueOf(resultSet.getInt(1)));
            }
        }
        return listSensors;
    }

    //radio button "sensor" donne la liste de tous les capteurs et radion button "type" donne tous les différents types présents sur le google doc "types données"
    // --> afficher que si y a des données pour ce capteur/ type
    //si "type" est sélectionné, le champ data list affiche tous les capteurs qui délivrent ce type et qui ont des données
    //si "sensor" est sélectionné, le champ data list affiche tous les types délivrés par ce capteur et qui ont des données

}
