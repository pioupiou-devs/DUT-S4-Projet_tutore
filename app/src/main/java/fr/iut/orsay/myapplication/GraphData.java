package fr.iut.orsay.myapplication;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GraphData
    {
        private final Connection connection;
        private final PreparedStatement getValuesWithDate_ps;
        private final PreparedStatement getValues_ps;
        private ArrayList<ArrayList<String>> graphsData;
        private Date startDate;
        private Date endDate;
        
        /**
         * constructeur
         *
         * @param connection
         * @param context
         * @throws SQLException
         */
        public GraphData(Connection connection, Context context) throws SQLException
            {
                this.connection = connection;
                getValuesWithDate_ps = connection.prepareStatement(context.getString(R.string.get_values_with_specified_dates));
                getValues_ps = connection.prepareStatement(context.getString(R.string.get_values));
                startDate = null;
                endDate = null;
            }
        
        public Connection getConnection()
            {
                return connection;
            }
        
        public ArrayList<ArrayList<String>> getGraphsData()
            {
                return graphsData;
            }
        
        public void setGraphsData(ArrayList<ArrayList<String>> graphsData)
            {
                this.graphsData = graphsData;
            }
        
        public void setStartDate(Date startDate)
            {
                this.startDate = startDate;
            }
        
        public void setEndDate(Date endDate)
            {
                this.endDate = endDate;
            }
        
        /**
         * execute les requêtes pour chaque données sélectionnées
         *
         * @return HashMap<String, ArrayList < Entry>> : tableau clé : valeur récupéré donné à la page ChartActivity
         * @throws SQLException
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @RequiresApi(api = Build.VERSION_CODES.N) public HashMap<String, ArrayList<Entry>> getData() throws SQLException, ExecutionException, InterruptedException
            {
                HashMap<String, ArrayList<Entry>> data = new HashMap<>();
                if (startDate != null && endDate != null)
                    {
                        for (int i = 0; i < graphsData.size(); i++)
                            {
                                ArrayList<Entry> values = DatabaseTools.getValues(Integer.parseInt(graphsData.get(i).get(0).substring(0, 1)), Integer.parseInt(graphsData.get(i).get(1).substring(0, 1)), getValuesWithDate_ps, startDate, endDate);
                                data.put(graphsData.get(i).get(0) + ":" + graphsData.get(i).get(1), values);
                            }
                    }
                else
                    {
                        for (int i = 0; i < graphsData.size(); i++)
                            {
                                ArrayList<Entry> values = DatabaseTools.getValues(Integer.parseInt(graphsData.get(i).get(0).substring(0, 1)), Integer.parseInt(graphsData.get(i).get(1).substring(0, 1)), getValues_ps);
                                data.put(graphsData.get(i).get(0) + ":" + graphsData.get(i).get(1), values);
                            }
                    }
                return data;
            }
        
        /**
         * concatène le numSensor et le numIPSO en une string pour l'afficher dans la list view
         *
         * @return ArrayList<String>
         */
        public ArrayList<String> getConcatenatedCurrentData()
            {
                ArrayList<String> concatenatedCurrentData = new ArrayList<>();
                for (int i = 0; i < graphsData.size(); i++)
                    {
                        concatenatedCurrentData.add(graphsData.get(i).get(0) + ":" + graphsData.get(i).get(1));
                    }
                return concatenatedCurrentData;
            }
    }
