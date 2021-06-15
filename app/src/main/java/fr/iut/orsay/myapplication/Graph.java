package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.service.autofill.Dataset;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.util.ArrayList;


//OnChartGestureListener
public class Graph implements GraphInterface, OnChartValueSelectedListener {
    
    int height = 0, width = 0, initHeight = 0, initWidth = 0, compoWidth = 0, compoHeight = 0;
    String graphName;
    
    private final int max_curve=4;
    private static int NUM_ID = 0;
    private int id;
    ArrayList<LineDataSet> dataSets = new ArrayList<>();
    
    LineChart chart = null;
    
    
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
        
        this.chart= create_chart(context);
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
        
        this.chart=create_chart(context);

    }
    
    public LineChart create_chart(View context) {//creer le cadre et initialiser les valeur du cadre
        LineChart new_chart = (LineChart) context;
        
        new_chart.setDrawBorders(true); //encadre la graph
        new_chart.setPinchZoom(true);
        new_chart.setTouchEnabled(true);
        
        new_chart.setNoDataText("Chart loading, please wait");
        new_chart.getDescription().setEnabled(false);
    
        // Formatter to adjust epoch time to readable date
        //new_chart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
    
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateValueFormatter());
        
        
        return new_chart;
    }
    
    /**
     * choos
     *
     * @param set Linedatadet to be modified
     * @param i his index in the chart
     *
     * @return the Linedataset
     */
    private LineDataSet get_color(LineDataSet set,int i){
        switch(i) {
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
    
    
    private LineDataSet formatting_dataset(LineDataSet set){
        
        set.setDrawIcons(false);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(false);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        set.setDrawValues(false);
        
        return set;
    }
    
    
    
    @Override
    public void addDataSet(String label, ArrayList<Entry> dataSet) {//ajouter les entry dans le tableau
        //TODO : ajouter les anticon --> verif sur le label/ entry vide
        if (dataSets.size() == max_curve) { //on ne veux pas plus de 4 courbes dans notre appli
            System.out.println("max number of data set for " + graphName + " (max is 4)");
            return;
        }
        
        
        dataSets.add(formatting_dataset(new LineDataSet(dataSet, label)));
        
        return;
    }
    
    @Override
    public void removeDataSet(String label) { //clear le tablea I gess, de une seule courbe
        
        for (int i = 0; i < dataSets.size(); i++) { //pas de recherche avencer car on aurais au max 4 courbes
            if (dataSets.get(i).getLabel() == label) { //on cherche par rapport au label
                dataSets.remove(i);
                return;
            }
        }
        
        return;
    }
    

    @Override
    public void show() { // affiche toute les courbes et le graph
        
        if (dataSets.size() ==0 ){
            return;
        }
        
        ArrayList<ILineDataSet> dataSe = new ArrayList<>();
        for (int i=0; i<dataSets.size() ;i++){
            if (dataSets.get(i).getLabel()!="Point"){
                dataSets.set(i, get_color(dataSets.get(i),i));
            }
            
            dataSe.add(dataSets.get(i));
        }
        
        LineData data = new LineData(dataSe);
        chart.setOnChartValueSelectedListener(this);
    
        chart.setData(data);
        
        return;
    
    }
    
    @Override
    public void showDataSet(String label) {//affiche une des courbes graphique et desaffiche les autres
        ArrayList<ILineDataSet> dataSe = new ArrayList<>();
        for (int i=0; i<dataSets.size() ;i++){
            if(dataSets.get(i).getLabel()==label){
                dataSe.add(dataSets.get(i));
            }

        }
    
        LineData data = new LineData(dataSe);
    
        chart.setData(data);
        

        return;
    }
    
    @Override
    public void zoomIn() {
        width -= (initWidth * 10) / 100;
        height -= (initHeight * 10) / 100;

    }
    
    @Override
    public void zoomIn(int scale) {
        width -= (initWidth * scale) / 100;
        height -= (initHeight * scale) / 100;

    }
    
    @Override
    public void zoomIn(int scaleW, int scaleH) {
        width -= (initWidth * scaleW) / 100;
        height -= (initHeight * scaleH) / 100;

    }
    
    @Override
    public void zoomOut() {
        width += (initWidth * 10) / 100;
        height += (initHeight * 10) / 100;

    }
    
    @Override
    public void zoomOut(int scale) {
        width += (initWidth * scale) / 100;
        height += (initHeight * scale) / 100;

    }
    
    @Override
    public void zoomOut(int scaleW, int scaleH) {
        width += (initWidth * scaleW) / 100;
        height += (initHeight * scaleH) / 100;

    }
    
    @Override
    public void rename(String newLabel) { //renomer dans la listes des grphique??
        this.graphName = newLabel;
        return;
    }
    
    @Override
    public void update() { //Deprecated
        
        //TODO : gerer les truc pour update les courbes
    
        System.out.println(chart.getWidth());
        System.out.println(chart.getHeight());
        
        
        //chart.zoomToCenter(width, height);
        //chart.zoomToCenter((float) 1.5 , (float) 1.5);
    
    
        return;
    }
    
    @Override
    public String print() { // affiche dans la console, toute les courbes
        String out ="";
    
        out+=("Data form graph "+graphName+"\n");
        
        for (int i = 0; i < dataSets.size(); i++) {
            out+=printer(dataSets.get(i));
            out+="...\n";
        }
        
        out+=("--------------------------------");
        
        return out;
    }
    
    
    @Override
    public String printDataSet(String label) { //affiche une seule courbe
        String out ="";
        boolean found=false;
        
        for (int i = 0; i < dataSets.size(); i++) { //pas de recherche avencer car on aurais au max 4 courbes
            if (dataSets.get(i).getLabel() == label) { //on cherche par rapport au label
                out+=printer(dataSets.get(i));
                found = true;
            }
        }
        
        if (!found){
            out+=("No data set found for the search :"+label+"\n");
        }
    
        out+=("--------------------------------");
        return out;
    }
    
    //sert a mettre en forme les coubres
    private String printer(ILineDataSet data){
        String out ="";
        
        out+=("Data set : "+data.getLabel()+"\n");
        
        for (int i =0; i<data.getEntryCount();i++) {
            Entry temp = data.getEntryForIndex(i);
            
            out+=("\t - x :"+temp.getX()+"\t y :"+temp.getY()+"\n");
        }
        
        return out;
    }
    
    
    //OnChartValueSelectedListener
    
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        float f = e.getY();
        System.out.println(f);
        
        for (int i = 0; i < dataSets.size(); i++) {
            if (i==h.getDataSetIndex()) {
                LineDataSet sel = dataSets.get(i);
                popup(e,sel);
                //stat(sel);
            }
        }
        
        return;
    }
    
    @Override
    public void onNothingSelected() {
    
    }
    
    
    private void popup(Entry e,LineDataSet h){
    
        String popval = "Point";
        removeDataSet(popval);
    
        ArrayList<Entry> val = new ArrayList<>();
        val.add(e.copy());
    
        LineDataSet point = new LineDataSet(val, popval);
        point.setDrawIcons(false);
        point.setColor(h.getColor());
        point.setCircleColor(h.getColor());
        point.setLineWidth(1f);
        point.setCircleRadius(5f);
        point.setDrawCircleHole(false);
        point.setValueTextSize(9f);
        point.setDrawFilled(false);
        point.setFormLineWidth(1f);
        point.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        point.setFormSize(15.f);
        point.setDrawValues(true);
        
        dataSets.add(point);
        
        show();
        
        return;
    
    }
    
    
    
    
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return graphName;
    }
    
    public void setName(String graphName)
    {
        this.graphName = graphName;
    }
    
}
