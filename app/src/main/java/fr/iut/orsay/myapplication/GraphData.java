package fr.iut.orsay.myapplication;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class GraphData {
    private ArrayList<ArrayList<String>> graphsData;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Connection connection;
    private PreparedStatement getValuesWithDate_ps;
    private PreparedStatement getValues_ps;

    public GraphData(Connection connection, Context context) throws SQLException {
        this.connection = connection;
        getValuesWithDate_ps = connection.prepareStatement(context.getString(R.string.get_values_with_specified_dates));
        getValues_ps = connection.prepareStatement(context.getString(R.string.get_values));
    }

    public Connection getConnection() {
        return connection;
    }

    public ArrayList<ArrayList<String>> getGraphsData() {
        return graphsData;
    }

    public void setGraphsData(ArrayList<ArrayList<String>> graphsData) {
        this.graphsData = graphsData;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HashMap<String, ArrayList<Entry>> getData() throws SQLException, ExecutionException, InterruptedException {
        HashMap<String, ArrayList<Entry>> data = new HashMap<>();
        if (startDate != null && endDate != null) {
            for (int i = 0; i < graphsData.size(); i++) {
                ArrayList<Entry> values = DatabaseTools.getValues(Integer.parseInt(graphsData.get(i).get(0).substring(0, 1)),
                        Integer.parseInt(graphsData.get(i).get(1).substring(0, 1)),
                        getValues_ps,
                        startDate,
                        endDate);
                data.put(graphsData.get(i).get(0).substring(4) + ":" + graphsData.get(i).get(1).substring(4), values);
            }
        }
        else{
            for (int i = 0; i < graphsData.size(); i++) {
                ArrayList<Entry> values = DatabaseTools.getValues(Integer.parseInt(graphsData.get(i).get(0).substring(0, 1)),
                        Integer.parseInt(graphsData.get(i).get(1).substring(0, 1)),
                        getValues_ps);
                data.put(graphsData.get(i).get(0).substring(4) + ":" + graphsData.get(i).get(1).substring(4), values);
            }
        }
        return data;
    }
}
