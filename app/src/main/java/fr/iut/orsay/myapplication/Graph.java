package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class Graph implements GraphInterface
    {
        
        int height = 0, width = 0, initHeight = 0, initWidth = 0, compoWidth = 0, compoHeight = 0;
        String graphName;
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<ArrayList<Entry>> dataSets = new ArrayList<>();
        LineChart chart = null;
        
        public Graph(String graphName, int compoWidth, int compoHeight, int width, int height)
            {
                this.graphName = graphName;
                this.compoWidth = compoWidth;
                this.compoHeight = compoHeight;
                this.width = width;
                this.height = height;
                this.initWidth = width;
                this.initHeight = height;
                
                //TODO: create the graph => chart
                chart.setPinchZoom(true);
            }
        
        public Graph(String graphName, int compoWidth, int compoHeight, int width, int height, ArrayList<String> labels, ArrayList<ArrayList<Entry>> dataSets)
            {
                this.graphName = graphName;
                this.compoWidth = compoWidth;
                this.compoHeight = compoHeight;
                this.width = width;
                this.height = height;
                this.initWidth = width;
                this.initHeight = height;
                this.labels = labels;
                this.dataSets = dataSets;
                
                //TODO: create the graph => chart
                chart.setPinchZoom(true);
            }
        
        @Override public void addDataSet(String label, ArrayList<Entry> dataSet)
            {
            
            }
        
        @Override public void removeDataSet(String label)
            {
            
            }
        
        @Override public void show()
            {
            
            }
        
        @Override public void showDataSet(String label)
            {
            
            }
        
        @Override public void zoomIn()
            {
                width -= (initWidth * 10) / 100;
                height -= (initHeight * 10) / 100;
                update();
            }
        
        @Override public void zoomIn(int scale)
            {
                width -= (initWidth * scale) / 100;
                height -= (initHeight * scale) / 100;
                update();
            }
        
        @Override public void zoomIn(int scaleW, int scaleH)
            {
                width -= (initWidth * scaleW) / 100;
                height -= (initHeight * scaleH) / 100;
                update();
            }
        
        @Override public void zoomOut()
            {
                width += (initWidth * 10) / 100;
                height += (initHeight * 10) / 100;
                update();
            }
        
        @Override public void zoomOut(int scale)
            {
                width += (initWidth * scale) / 100;
                height += (initHeight * scale) / 100;
                update();
            }
        
        @Override public void zoomOut(int scaleW, int scaleH)
            {
                width += (initWidth * scaleW) / 100;
                height += (initHeight * scaleH) / 100;
                update();
            }
        
        @Override public void rename(String newLabel)
            {
            
            }
        
        @Override public void update()
            {
                chart.zoomToCenter(width, height);
            }
        
        @Override public String print()
            {
                return null;
            }
        
        @Override public String printDataSet(String label)
            {
                return null;
            }
    }
