package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


//OnChartGestureListener
public class Graph implements GraphInterface, OnChartValueSelectedListener {
    
    private static int NUM_ID = 0;
    private final int max_curve = 5;
    int height = 0, width = 0, initHeight = 0, initWidth = 0, compoWidth = 0, compoHeight = 0;
    String graphName;
    ArrayList<LineDataSet> dataSets = new ArrayList<>();
    LineChart chart = null;
    private int id;
    
    private final String popval = "Point";
    
    
    public Graph(String graphName, int compoWidth, int compoHeight, int width, int height, View context) {
        this.graphName = graphName;
        this.compoWidth = compoWidth;
        this.compoHeight = compoHeight;
        this.width = width;
        this.height = height;
        this.initWidth = width;
        this.initHeight = height;
        id = NUM_ID;
        NUM_ID++;
        
        this.chart = create_chart(context);
    }
    
    
    public Graph(String graphName, int compoWidth, int compoHeight, int width, int height, ArrayList<LineDataSet> dataSets, View context) {
        this.graphName = graphName;
        this.compoWidth = compoWidth;
        this.compoHeight = compoHeight;
        this.width = width;
        this.height = height;
        this.initWidth = width;
        this.initHeight = height;
        id = NUM_ID;
        NUM_ID++;
        
        
        this.dataSets = dataSets;
        
        this.chart = create_chart(context);
        
    }
    
    public LineChart create_chart(View context) {//creer le cadre et initialiser les valeur du cadre
        LineChart new_chart = (LineChart) context;
        
        new_chart.setDrawBorders(true); //encadre la graph
        new_chart.setPinchZoom(true);
        new_chart.setTouchEnabled(true);
        
        new_chart.setNoDataText("Chart loading, please wait");
        //new_chart.getDescription().setEnabled(false);
        
        new_chart.getXAxis().setDrawLabels(false);
    
        new_chart.getDescription().setText("");
        new_chart.getDescription().setTextSize(15f);
        
        // Formatter to adjust epoch time to readable date
        //new_chart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
    
        /*XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateValueFormatter());*/
    
        //TODO orienter les legendes
        //new_chart.getLegend().setOrientation(45);
        
        
        return new_chart;
    }
    
    /**
     * is use to set a color for each curve
     *
     * @param set Linedatadet to be modified
     * @param i   his index in the chart
     * @return the Linedataset with the correct color
     */
    private LineDataSet get_color(LineDataSet set, int i) {
        switch (i) {
            case 0:
                set.setCircleColor(Color.BLUE);
                set.setColor(Color.BLUE);
                return set;
            case 1:
                set.setCircleColor(Color.RED);
                set.setColor(Color.RED);
                return set;
            case 2:
                set.setCircleColor(Color.MAGENTA);
                set.setColor(Color.MAGENTA);
                return set;
            case 3:
                set.setCircleColor(Color.GREEN);
                set.setColor(Color.GREEN);
                return set;
            default:
                System.out.println("test couleur " + i);
                set.setCircleColor(Color.BLACK);
                set.setColor(Color.BLACK);
                return set;
            
        }
    }
    
    
    /**
     * function used to set the graphical representation of the curve
     *
     * @param set the curve to be formated
     * @return the curve, correctly formated
     */
    private LineDataSet formatting_dataset(LineDataSet set) {
        
        set.setDrawIcons(false);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setDrawFilled(false);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        set.setDrawValues(false);
        
        return set;
    }
    
    
    @Override
    /**
     * is used to add dataset (curve) to chart
     */
    public void addDataSet(String label, ArrayList<Entry> dataSet) {
        if (dataSets.size() >= max_curve) { //we put a limit to the curve
            System.out.println("max number of data set for " + graphName + " (max is 4)");
            return;
        }
        
        
        dataSets.add(formatting_dataset(new LineDataSet(dataSet, label)));
        
        return;
    }
    
    @Override
    /**
     * is used to remove dataset (curve) from the chart
     */
    public void removeDataSet(String label) {
        
        for (int i = 0; i < dataSets.size(); i++) {
            if (dataSets.get(i).getLabel() == label) { //on cherche par rapport au label
                dataSets.remove(i);
                return;
            }
        }
        
        return;
    }
    
    
    /**
     * is used to get a dataset from the curve name
     *
     * @param label the name of the curve you want
     * @return
     */
    public LineDataSet getDataSet(String label){
        for (int i = 0; i < dataSets.size(); i++) { //pas de recherche avencer car on aurais au max 4 courbes
            if (dataSets.get(i).getLabel() == label) { //on cherche par rapport au label
                return dataSets.get(i);
            }
        }
    
        return null;
        
    }
    
    
    @Override
    /**
     * display the cahet with every curve in it
     */
    public void show() {
        
        if (dataSets.size() == 0) {
            return;
        }
        
        ArrayList<ILineDataSet> dataSe = new ArrayList<>();
        for (int i = 0; i < dataSets.size(); i++) {
            if (dataSets.get(i).getLabel() != "Point") {
                dataSets.set(i, get_color(dataSets.get(i), i));
            }
            
            dataSe.add(dataSets.get(i));
        }
        
        LineData data = new LineData(dataSe);
        chart.setOnChartValueSelectedListener(this);
        
        chart.setData(data);
        generate_legend();
        
        return;
        
    }
    
    @Override
    /**
     * diplay only one chart
     */
    public void showDataSet(String label) {
        ArrayList<ILineDataSet> dataSe = new ArrayList<>();
        for (int i = 0; i < dataSets.size(); i++) {
            if (dataSets.get(i).getLabel() == label) {
                dataSe.add(dataSets.get(i));
            }
            
        }
        
        LineData data = new LineData(dataSe);
        
        chart.setData(data);
        
        
        return;
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomIn() {
        width -= (initWidth * 10) / 100;
        height -= (initHeight * 10) / 100;
        
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomIn(int scale) {
        width -= (initWidth * scale) / 100;
        height -= (initHeight * scale) / 100;
        
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomIn(int scaleW, int scaleH) {
        width -= (initWidth * scaleW) / 100;
        height -= (initHeight * scaleH) / 100;
        
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomOut() {
        width += (initWidth * 10) / 100;
        height += (initHeight * 10) / 100;
        
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomOut(int scale) {
        width += (initWidth * scale) / 100;
        height += (initHeight * scale) / 100;
        
    }
    
    @Override
    /**
     * used to zoom in the chart
     */
    public void zoomOut(int scaleW, int scaleH) {
        width += (initWidth * scaleW) / 100;
        height += (initHeight * scaleH) / 100;
        
    }
    
    @Override
    /**
     * used to rename the chart
     */
    public void rename(String newLabel) {
        this.graphName = newLabel;
        return;
    }
    
    
    @Override
    /**
     * used to print the value of the chart in the consol (debug)
     */
    public String print() {
        String out = "";
        
        out += ("Data form graph " + graphName + "\n");
        
        for (int i = 0; i < dataSets.size(); i++) {
            out += printer(dataSets.get(i));
            out += "...\n";
        }
        
        out += ("--------------------------------");
        
        return out;
    }
    
    
    @Override
    /**
     * used to print the value of one curve in the consol from it's name (debug)
     */
    public String printDataSet(String label) {
        String out = "";
        boolean found = false;
        
        for (int i = 0; i < dataSets.size(); i++) { //pas de recherche avencer car on aurais au max 4 courbes
            if (dataSets.get(i).getLabel() == label) { //on cherche par rapport au label
                out += printer(dataSets.get(i));
                found = true;
            }
        }
        
        if (!found) {
            out += ("No data set found for the search :" + label + "\n");
        }
        
        out += ("--------------------------------");
        return out;
    }
    
    /**
     * format the data to a string
     *
     * @param data
     * @return
     */
    private String printer(ILineDataSet data) {
        String out = "";
        
        out += ("Data set : " + data.getLabel() + "\n");
        
        for (int i = 0; i < data.getEntryCount(); i++) {
            Entry temp = data.getEntryForIndex(i);
            
            out += ("\t - x :" + temp.getX() + "\t y :" + temp.getY() + "\n");
        }
        
        return out;
    }
    
    
    //OnChartValueSelectedListener
    
    @Override
    /**
     * listener for the popop
     */
    public void onValueSelected(Entry e, Highlight h) {
        float f = e.getY();
        System.out.println(f);
        
        for (int i = 0; i < dataSets.size(); i++) {
            //on recupere la courbe selectionner
            if (i == h.getDataSetIndex()) {
                LineDataSet sel = dataSets.get(i);
                popup(e, sel);
                
            }
        }
        
        return;
    }
    
    @Override
    /**
     * used to clear the popup
     */
    public void onNothingSelected() {
        removepopval();
        
        //TODO mettre le truc de clear les states
    }
    
    
    private void removepopval() {
        removeDataSet(popval);
        chart.getDescription().setText("");
        
        show();
        
        return;
        
    }
    
    
    private void popup(Entry e, LineDataSet h) {
        
 
        removeDataSet(popval);
        
        ArrayList<Entry> val = new ArrayList<>();
        val.add(e.copy());
        
        LineDataSet point = new LineDataSet(val, popval);
        point.setDrawIcons(false);
        point.setColor(h.getColor());
        point.setCircleColor(h.getColor());
        point.setLineWidth(1f);
        point.setCircleRadius(6f);
        point.setDrawCircleHole(false);
        point.setValueTextSize(15f);
        point.setDrawFilled(false);
        point.setFormLineWidth(1f);
        point.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        point.setFormSize(15.f);
        point.setDrawValues(true);
        
        
        dataSets.add(point);
        
        chart.getDescription().setText(String.valueOf(e.getX()));
        
        show();
        
        return;
        
    }
    
    
    private void generate_legend(){
        ArrayList<LegendEntry> legend = new ArrayList<>();
        
        for (int i = 0; i < dataSets.size(); i++) {
            //on recupere la courbe selectionner
            if (dataSets.get(i).getLabel() != popval) {
                legend.add(new LegendEntry(dataSets.get(i).getLabel(), Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, dataSets.get(i).getColor()));
            }
        }
    
        chart.getLegend().setCustom(legend);
        
    }
    
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return graphName;
    }
    
    public void setName(String graphName) {
        this.graphName = graphName;
    }
    
}
