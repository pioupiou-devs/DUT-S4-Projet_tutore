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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//OnChartGestureListener
public class Graph implements GraphInterface, OnChartValueSelectedListener, Serializable
    {
        
        private static int NUM_ID = 0;
        private final int max_curve = 5;
        int height = 0, width = 0, initHeight = 0, initWidth = 0;
        String graphName;
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        LineChart chart = null;
        private int id;
        
        private final String popval = "Point";
        
        
        public Graph(String graphName)
            {
                this.graphName = graphName;
                this.initWidth = width;
                this.initHeight = height;
                id = NUM_ID;
                NUM_ID++;
            }
        
        
        public Graph(String graphName, ArrayList<LineDataSet> dataSets)
            {
                this.graphName = graphName;
                this.initWidth = width;
                this.initHeight = height;
                id = NUM_ID;
                NUM_ID++;
                
                
                this.dataSets = dataSets;
                
                this.chart = null;
                
            }
        
        public void create_chart(View view)
            {//creer le cadre et initialiser les valeur du cadre
                LineChart new_chart = (LineChart) view;
                
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

        //TODO orienter les legendes
        //new_chart.getLegend().setOrientation(45);
    
        this.chart = new_chart;
        return;
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
     * methode pour la representation graphique de la courbe
     *
     * @param set la courbe a formatter
     * @return la courbe correctement formatter
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
     * utiliser pour ajouter la courbe au graph
     */
    public void addDataSet(String label, ArrayList<Entry> dataSet) {
        if (dataSets.size() >= max_curve) { //on a limiter la courbe
            System.out.println("max number of data set for " + graphName + " (max is 4)");
            return;
        }
        
        
        dataSets.add(formatting_dataset(new LineDataSet(dataSet, label)));
        
        return;
    }
    
    @Override
    /**
     * utiliser pour retirer la courbe du graph
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
     * pour recuperer la courbe a partir du nom
     *
     * @param label le nom de la courbe
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
     * affiche la courbe avec chaque courbe
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
     * affiche une unique courbe
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
     * zoomer dans le graph
     */
    public void zoomIn() {
        width -= (initWidth * 10) / 100;
        height -= (initHeight * 10) / 100;
    
        chart.getViewPortHandler().setZoom( (float).5, (float).5,4,40 );
    
        show();
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void zoomIn(int scale) {
        width -= (initWidth * scale) / 100;
        height -= (initHeight * scale) / 100;
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void zoomIn(int scaleW, int scaleH) {
        width -= (initWidth * scaleW) / 100;
        height -= (initHeight * scaleH) / 100;
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void zoomOut() {
        width += (initWidth * 10) / 100;
        height += (initHeight * 10) / 100;
        
        show();
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void zoomOut(int scale) {
        width += (initWidth * scale) / 100;
        height += (initHeight * scale) / 100;
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void zoomOut(int scaleW, int scaleH) {
        width += (initWidth * scaleW) / 100;
        height += (initHeight * scaleH) / 100;
        
    }
    
    @Override
    /**
     *  zoomer dans le graph
     */
    public void rename(String newLabel) {
        this.graphName = newLabel;
        return;
    }
    
    
    @Override
    /**
     * affiche les donner du graph dans la console (debug)
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
     * affiche les valeure d'une courbe, en fonction du nom, dans la console  (debug)
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
     * transform les donnees en string
     *
     * @param data les donnée a transformer
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
     * ecouter pour la popup
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
     * ecouteur, en cas de reclick sur le meme point
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
    
    public LineChart getChart()
    {
        return chart;
    }
    
    public float get_startdate(){
            return (float)0.0;
    }
    
    public float get_enddate(){
        return (float)0.0;
    }
    
    public ArrayList<String> get_curvelbl(){
        ArrayList<String> labels = new ArrayList<>();
        
        for (int i =0; i< dataSets.size();i++){
            labels.add(dataSets.get(i).getLabel());
        }
        return labels;
    }
    
    
    }
    
    
   
    
