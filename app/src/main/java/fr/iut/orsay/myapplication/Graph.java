package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Graph implements GraphInterface {
    
    int height = 0, width = 0, initHeight = 0, initWidth = 0, compoWidth = 0, compoHeight = 0;
    String graphName;
    //ArrayList<String> labels = new ArrayList<>(); ///on stock toutes les noms des courbes
    //ArrayList<ArrayList<Entry>> dataSets = new ArrayList<>(); //on stickes les valeurs?
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    
    LineChart chart = null;
    
    public Graph(String graphName, int compoWidth, int compoHeight, int width, int height) {
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
    
    //public Graph(String graphName, int compoWidth, int compoHeight, int width, int height, ArrayList<String> labels, ArrayList<ArrayList<Entry>> dataSets) {
    public Graph(String graphName, int compoWidth, int compoHeight, int width, int height, ArrayList<ILineDataSet> dataSets) {
        this.graphName = graphName;
        this.compoWidth = compoWidth;
        this.compoHeight = compoHeight;
        this.width = width;
        this.height = height;
        this.initWidth = width;
        this.initHeight = height;
        //this.labels = labels;
        this.dataSets = dataSets;
        
        //TODO: create the graph => chart
        this.chart=create_chart();
        chart.setPinchZoom(true);
    }
    
    
    public LineChart create_chart() {//creer le cadre et initialiser les valeur du cadre
        //TODO : finir la fonction et regarder comment creer un linechart propre
        LineChart new_chart = null;
        
        return new_chart;
    }
    
    @Override
    public void addDataSet(String label, ArrayList<Entry> dataSet) {//ajouter les entry dans le tableau
        //LineDataSet temp = new LineDataSet(dataSet,label);
        //TODO : ajouter les anticon --> verif sur le label/ entry vide
        //4 courbes max
        if (dataSets.size() == 4) { //on ne veux pas plus de 4 courbes dans notre appli
            System.out.println("max number of data set for " + graphName + " (max is 4)");
            return;
        }
        
        dataSets.add(new LineDataSet(dataSet, label));
        
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
        
        
        update();
        return;
    
    }
    
    @Override
    public void showDataSet(String label) {//affiche une des courbes graphique et desaffiche les autres
        
        
        
        update();
        return;
    }
    
    @Override
    public void zoomIn() {
        width -= (initWidth * 10) / 100;
        height -= (initHeight * 10) / 100;
        update();
    }
    
    @Override
    public void zoomIn(int scale) {
        width -= (initWidth * scale) / 100;
        height -= (initHeight * scale) / 100;
        update();
    }
    
    @Override
    public void zoomIn(int scaleW, int scaleH) {
        width -= (initWidth * scaleW) / 100;
        height -= (initHeight * scaleH) / 100;
        update();
    }
    
    @Override
    public void zoomOut() {
        width += (initWidth * 10) / 100;
        height += (initHeight * 10) / 100;
        update();
    }
    
    @Override
    public void zoomOut(int scale) {
        width += (initWidth * scale) / 100;
        height += (initHeight * scale) / 100;
        update();
    }
    
    @Override
    public void zoomOut(int scaleW, int scaleH) {
        width += (initWidth * scaleW) / 100;
        height += (initHeight * scaleH) / 100;
        update();
    }
    
    @Override
    public void rename(String newLabel) { //renomer dans la listes des grphique??
        this.graphName = newLabel;
        return;
    }
    
    @Override
    public void update() { //met a jour le graph zoom ou des courbes en plus etc
        
        //TODO : gerer les truc pour update les courbes
        
        
        chart.zoomToCenter(width, height);
        
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
    
        //out+=("Data set form dataset "+label+"\n");
        
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
}
