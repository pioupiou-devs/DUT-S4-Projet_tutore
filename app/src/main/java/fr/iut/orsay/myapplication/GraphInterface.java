package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface GraphInterface {
    int height = 0, width = 0;
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<ArrayList<Entry>> dataSets = new ArrayList<>();
    LineChart chart = null;
    
    
    void addDataSet(String label, ArrayList<Entry> dataSet);
    
    void removeDataSet(String label);
    
    void show();
    
    void showDataSet(String label);
    
    void zoomIn();
    
    void zoomOut();
    
    void rename(String newLabel);
    
    String print();
    
    String printDataSet(String label);
}
