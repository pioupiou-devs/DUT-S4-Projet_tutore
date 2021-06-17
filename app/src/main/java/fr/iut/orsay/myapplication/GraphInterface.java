package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface GraphInterface
    {
        void addDataSet(String label, ArrayList<Entry> dataSet);
        
        void removeDataSet(String label);
        
        void show();
        
        void showDataSet(String label);
        
        void zoomIn();
        
        void zoomIn(int scale);
        
        void zoomIn(int scaleW, int scaleH);
        
        void zoomOut();
        
        void zoomOut(int scale);
        
        void zoomOut(int scaleW, int scaleH);
        
        void rename(String newLabel);
        
        String print();
        
        String printDataSet(String label);
    }
