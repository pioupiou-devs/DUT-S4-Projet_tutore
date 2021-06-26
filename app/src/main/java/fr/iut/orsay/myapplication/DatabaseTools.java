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
        /**
         * connecte l'application à la base de données
         *
         * @param url l'url de connexion à la bd
         * @return l'objet Connection pour ensuite exécuter des requêtes
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ClassNotFoundException erreur retournée si le driver mariadb n'est pas trouvable
         */
        public static Connection openConnection(String url) throws SQLException, ClassNotFoundException
            {
                
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Class.forName("org.mariadb.jdbc.Driver");
                return DriverManager.getConnection(url);
            }
        
        /**
         * récupère les valeurs depuis la BD pour un capteur et un type de données donnés
         *
         * @param numSensor l'id du capteur donné
         * @param numIPSO l'id du type de données donné
         * @param preparedStatement le preparedStatement adéquat
         * @return un ArrayList<Entry> pour la classe Graph
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                //set des paramètres de la méthode dans le preparedStatement
                preparedStatement.setInt(1, numSensor);
                preparedStatement.setInt(2, numIPSO);
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau d'Entry en bouclant sur le ResultSet
                while (resultSet.next())
                    {
                        values.add(new Entry(Long.valueOf(resultSet.getTimestamp(1).getTime()).floatValue(), Float.parseFloat(resultSet.getString(2))));
                    }
                return values;
            }
        
        /**
         * surcharge avec la spécification d'une plage de dates
         *
         * @param numSensor l'id du capteur donné
         * @param numIPSO l'id du type de données donné
         * @param preparedStatement le preparedStatement adéquat
         * @param dateMin borne de date minimale du jeu de données récupéré
         * @param dateMax borne de date maximale du jeu de données récupéré
         * @return un ArrayList<Entry> pour la classe Graph
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<Entry> getValues(int numSensor, int numIPSO, PreparedStatement preparedStatement, Date dateMin, Date dateMax) throws SQLException, ExecutionException, InterruptedException
            {
                
                //set des paramètres de la méthode dans le preparedStatement
                preparedStatement.setInt(1, numSensor);
                preparedStatement.setInt(2, numIPSO);
                preparedStatement.setTimestamp(3, new Timestamp(dateMax.getTime()));
                preparedStatement.setTimestamp(4, new Timestamp(dateMin.getTime()));
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau d'Entry en bouclant sur le ResultSet
                ArrayList<Entry> values = new ArrayList<>();
                while (resultSet.next())
                    {
                        values.add(new Entry(Long.valueOf(resultSet.getTimestamp(1).getTime()).floatValue(), Float.parseFloat(resultSet.getString(2))));
                    }
                return values;
            }
        
        /**
         * méthode appelée pour récupérer la liste des types de données depuis la BD
         *
         * @param co variable Connection permettant l'exécution de requêtes à la bd
         * @param query chaine de caractère contenant la requête SQL
         * @return ArrayList<String>
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        //call this method when radio button "type" is selected, it returns the list of all types
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getTypes(Connection co, String query) throws SQLException, ExecutionException, InterruptedException
            {
                
                ArrayList<String> listTypes = new ArrayList<>();
                Statement statement = co.createStatement();
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau de types de données en bouclant sur le ResultSet
                while (resultSet.next())
                    {
                        listTypes.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                    }
                return listTypes;
            }
        
        /**
         * surcharge avec un capteur spécifié
         *
         * @param numSensor l'id du capteur donné
         * @param preparedStatement le preparedStatement adéquat
         * @return ArrayList<String>
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        //call this method to get types from a specified sensor
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getTypes(int numSensor, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                
                ArrayList<String> listTypes = new ArrayList<>();
                //set du paramètre de la méthode dans le preparedStatement
                preparedStatement.setInt(1, numSensor);
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau de types de données en bouclant sur le ResultSet
                while (resultSet.next())
                    {
                        listTypes.add(resultSet.getInt(1) + "-" + resultSet.getString(2));
                    }
                return listTypes;
            }
        
        /**
         * méthode appelée pour récupérer la liste des capteurs depuis la BD
         *
         * @param co variable Connection permettant l'exécution de requêtes à la bd
         * @param query chaine de caractère contenant la requête SQL
         * @return ArrayList<String>
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        //call this method when radio button "sensor" is selected, it returns the list of all sensors
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getSensors(Connection co, String query) throws SQLException, ExecutionException, InterruptedException
            {
                
                ArrayList<String> listSensors = new ArrayList<>();
                Statement statement = co.createStatement();
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau de capteurs en bouclant sur le ResultSet
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
        
        /**
         * surcharge avec un type de donnée spécifié
         *
         * @param numIPSO l'id du type de données donné
         * @param preparedStatement le preparedStatement adéquat
         * @return ArrayList<String>
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         * @throws InterruptedException erreur retournée en cas d'erreur lors de la récupération de la valeur retournée par le CompletableFuture
         */
        //call this method to get sensors with a specified sensor
        @RequiresApi(api = Build.VERSION_CODES.N) public static ArrayList<String> getSensors(int numIPSO, PreparedStatement preparedStatement) throws SQLException, ExecutionException, InterruptedException
            {
                
                ArrayList<String> listSensors = new ArrayList<>();
                //set du paramètre de la méthode dans le preparedStatement
                preparedStatement.setInt(1, numIPSO);
                
                //execution de la requete de manière asynchrone
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
                
                //création du tableau de capteurs en bouclant sur le ResultSet
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
    }