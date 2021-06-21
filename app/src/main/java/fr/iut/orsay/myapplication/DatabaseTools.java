package fr.iut.orsay.myapplication;

import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DatabaseTools
    {
        public static Connection openConnection(String url) throws SQLException, ClassNotFoundException
            {
                Connection co;
                
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                
                Class.forName("org.mariadb.jdbc.Driver");
                co = DriverManager.getConnection(url);
                return co;
            }
        
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                preparedStatement.setInt(1, numSensor);
                preparedStatement.setInt(2, numIPSO);
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = preparedStatement.executeQuery();
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                
                ResultSet resultSet = queryExecution.get();
                
                ArrayList<Entry> values = new ArrayList<>();
                while (resultSet.next())
                    {
                        //values.add(new Entry(resultSet.getTimestamp(1), Float.parseFloat(resultSet.getString(2))));
                        values.add(new Entry(Long.valueOf(resultSet.getTimestamp(1).getTime()).floatValue(), Float.parseFloat(resultSet.getString(2)))); //TODO : problème de compatibilité entre les Entry et les DateTime
                    }
                return values;
            }
        
        //surcharge pour la spécification d'une plage de dates
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement, Date dateMin, Date dateMax) throws SQLException, ExecutionException, InterruptedException
            {
                preparedStatement.setInt(1, numSensor);
                preparedStatement.setInt(2, numIPSO);
                preparedStatement.setTimestamp(3, new Timestamp(dateMax.getTime()));
                preparedStatement.setTimestamp(4, new Timestamp(dateMin.getTime()));
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = preparedStatement.executeQuery();
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                
                ResultSet resultSet = queryExecution.get();
                
                ArrayList<Entry> values = new ArrayList<>();
                while (resultSet.next())
                    {
                        //values.add(new Entry(resultSet.getTimestamp(1), Float.parseFloat(resultSet.getString(2))));
                        values.add(new Entry(Long.valueOf(resultSet.getTimestamp(1).getTime()).floatValue(), Float.parseFloat(resultSet.getString(2)))); //TODO : problème de compatibilité entre les Entry et les DateTime
                    }
                return values;
            }
        
        //call this method when radio button "type" is selected, it returns the list of all types
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getTypes(Connection co, String query) throws SQLException, ExecutionException, InterruptedException
            {
                ArrayList<String> listTypes = new ArrayList<>();
                Statement statement = co.createStatement();
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = statement.executeQuery(query);
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                
                ResultSet resultSet = queryExecution.get();
                
                while (resultSet.next())
                    {
                        listTypes.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                    }
                return listTypes;
            }
        
        //call this method to get types from a specified sensor
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getTypes(int numSensor, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                ArrayList<String> listTypes = new ArrayList<>();
                
                preparedStatement.setInt(1, numSensor);
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = preparedStatement.executeQuery();
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                ResultSet resultSet = queryExecution.get();
                
                while (resultSet.next())
                    {
                        listTypes.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                    }
                return listTypes;
            }
        
        //call this method when radio button "sensor" is selected, it returns the list of all sensors
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getSensors(Connection co, String query) throws SQLException, ExecutionException, InterruptedException
            {
                ArrayList<String> listSensors = new ArrayList<>();
                Statement statement = co.createStatement();
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = statement.executeQuery(query);
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                
                ResultSet resultSet = queryExecution.get();
                
                while (resultSet.next())
                    {
                        if (!resultSet.getString(2).equals(""))
                            {
                                listSensors.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                            }
                        else
                            {
                                listSensors.add(String.valueOf(resultSet.getInt(1)));
                            }
                    }
                return listSensors;
            }
        
        //call this method to get sensors with a specified sensor
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getSensors(int numIPSO, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                ArrayList<String> listSensors = new ArrayList<>();
                preparedStatement.setInt(1, numIPSO);
                
                CompletableFuture<ResultSet> queryExecution = CompletableFuture.supplyAsync(() ->
                {
                    ResultSet resultSet = null;
                    try
                        {
                            resultSet = preparedStatement.executeQuery();
                        }
                    catch (SQLException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return resultSet;
                });
                
                ResultSet resultSet = queryExecution.get();
                
                while (resultSet.next())
                    {
                        if (!resultSet.getString(2).equals(""))
                            {
                                listSensors.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                            }
                        else
                            {
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
