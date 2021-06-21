package fr.iut.orsay.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//OnChartGestureListener
public class Graph implements GraphInterface, OnChartValueSelectedListener, Serializable
    {
        
        private static int NUM_ID = 0;
        private final int max_curve = 5;
        private final String popval = "Point";
        int height = 0, width = 0, initHeight = 0, initWidth = 0;
        String graphName;
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        LineChart chart = null;
        private int id;
        private Context context;
        private View view;
        
        
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
        
        public void create_chart(View view, Context context)
            {//creer le cadre et initialiser les valeur du cadre
                
                this.context = context;
                this.view = view;
                
                LineChart new_chart = (LineChart) view;
                
                new_chart.setDrawBorders(true); //encadre la graph
                new_chart.setPinchZoom(true);
                new_chart.setTouchEnabled(true);
                
                new_chart.setNoDataText("Chart loading, please wait");
                //new_chart.getDescription().setEnabled(false);
                
                new_chart.getXAxis().setDrawLabels(false);
                
                new_chart.getDescription().setText("");
                new_chart.getDescription().setTextSize(15f);
                
                
                new_chart.setDragDecelerationEnabled(false);
                
                // Formatter to adjust epoch time to readable date
                //new_chart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                
                
                XAxis xAxis = new_chart.getXAxis();
                xAxis.setValueFormatter(new DateValueFormatter());
                
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
        private LineDataSet set_curve_color(LineDataSet set, int i)
            {
                switch (i)
                    {
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
        private LineDataSet formatting_dataset(LineDataSet set)
            {
                
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
         */ public void addDataSet(String label, ArrayList<Entry> dataSet)
            {
                if (dataSets.size() >= max_curve)
                    { //on a limiter la courbe
                        System.out.println("max number of data set for " + graphName + " (max is 4)");
                        return;
                    }
                
                
                dataSets.add(formatting_dataset(new LineDataSet(dataSet, label)));
                
                return;
            }
        
        @Override
        /**
         * utiliser pour retirer la courbe du graph
         */ public void removeDataSet(String label)
            {
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (dataSets.get(i).getLabel() == label)
                            { //on cherche par rapport au label
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
        public LineDataSet getDataSet(String label)
            {
                for (int i = 0; i < dataSets.size(); i++)
                    { //pas de recherche avencer car on aurais au max 4 courbes
                        if (dataSets.get(i).getLabel() == label)
                            { //on cherche par rapport au label
                                return dataSets.get(i);
                            }
                    }
                
                return null;
                
            }
        
        
        @Override
        /**
         * affiche la courbe avec chaque courbe
         */ public void show()
            {
                
                if (dataSets.size() == 0)
                    {
                        return;
                    }
                
                ArrayList<ILineDataSet> dataSe = new ArrayList<>();
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (dataSets.get(i).getLabel() != "Point")
                            {
                                dataSets.set(i, set_curve_color(dataSets.get(i), i));
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
         */ public void showDataSet(String label)
            {
                ArrayList<ILineDataSet> dataSe = new ArrayList<>();
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (dataSets.get(i).getLabel() == label)
                            {
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
         */ public void zoomIn()
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                chart.zoom((float) 1.2, (float) 1.2, xValue, yValue);
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void zoomIn(int scale)
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                chart.zoom((float) 1 + Math.abs(scale), (float) 1 + Math.abs(scale), xValue, yValue);
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void zoomIn(int scaleW, int scaleH)
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                chart.zoom((float) 1 + Math.abs(scaleW), (float) 1 + Math.abs(scaleH), xValue, yValue);
                
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void zoomOut()
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                chart.zoom((float) .8, (float) .8, xValue, yValue);
                
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void zoomOut(int scale)
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                chart.zoom((float) 1 - Math.abs(scale), (float) 1 - Math.abs(scale), xValue, yValue);
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void zoomOut(int scaleW, int scaleH)
            {
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                chart.zoom((float) 1 - Math.abs(scaleW), (float) 1 - Math.abs(scaleH), xValue, yValue);
            }
        
        @Override
        /**
         *  zoomer dans le graph
         */ public void rename(String newLabel)
            {
                this.graphName = newLabel;
                return;
            }
        
        
        @Override
        /**
         * affiche les donner du graph dans la console (debug)
         */ public String print()
            {
                StringBuilder out = new StringBuilder();
                
                out.append("Data form graph ").append(graphName).append("\n");
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        out.append(printer(dataSets.get(i)));
                        out.append("...\n");
                    }
                
                out.append("--------------------------------");
                
                return out.toString();
            }
        
        
        @Override
        /**
         * affiche les valeure d'une courbe, en fonction du nom, dans la console  (debug)
         */ public String printDataSet(String label)
            {
                StringBuilder out = new StringBuilder();
                boolean found = false;
                
                for (int i = 0; i < dataSets.size(); i++)
                    { //pas de recherche avencer car on aurais au max 4 courbes
                        if (dataSets.get(i).getLabel().equalsIgnoreCase(label))
                            { //on cherche par rapport au label
                                out.append(printer(dataSets.get(i)));
                                found = true;
                            }
                    }
                
                if (!found)
                    {
                        out.append("No data set found for the search :").append(label).append("\n");
                    }
                
                out.append("--------------------------------");
                return out.toString();
            }
        
        /**
         * transform les donnees en string
         *
         * @param data les donnée a transformer
         * @return
         */
        private String printer(ILineDataSet data)
            {
                StringBuilder out = new StringBuilder();
                
                out.append("Data set : ").append(data.getLabel()).append("\n");
                
                for (int i = 0; i < data.getEntryCount(); i++)
                    {
                        Entry temp = data.getEntryForIndex(i);
                        
                        out.append("\t - x :").append(temp.getX()).append("\t y :").append(temp.getY()).append("\n");
                    }
                
                return out.toString();
            }
        
        
        //OnChartValueSelectedListener
        
        @Override
        /**
         * ecouter pour la popup
         */ public void onValueSelected(Entry e, Highlight h)
            {
                float f = e.getY();
                System.out.println(f);
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //on recupere la courbe selectionner
                        if (i == h.getDataSetIndex())
                            {
                                LineDataSet sel = dataSets.get(i);
                                popup(e, sel);
                                setStats(sel);
                                
                                //call stat(sel)
                                
                                return;
                            }
                    }
                
                return;
            }
        
        @Override
        /**
         * ecouteur, en cas de reclick sur le meme point
         */ public void onNothingSelected()
            {
                removepopval();
                clearStats();
                
                //TODO mettre le truc de clear les states
            }
        
        
        private void removepopval()
            {
                removeDataSet(popval);
                chart.getDescription().setText("");
                
                show();
                
                return;
                
            }
        
        
        private void popup(Entry e, LineDataSet h)
            {
                
                
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
                
                DateValueFormatter format = new DateValueFormatter();
                chart.getDescription().setText(format.getFormattedValue(e.getX()));
                show();
                
                return;
                
            }
        
        
        private void generate_legend()
            {
                ArrayList<LegendEntry> legend = new ArrayList<>();
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //on recupere la courbe selectionner
                        if (!dataSets.get(i).getLabel().equalsIgnoreCase(popval))
                            {
                                legend.add(new LegendEntry(dataSets.get(i).getLabel(), Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, dataSets.get(i).getColor()));
                            }
                    }
                
                chart.getLegend().setCustom(legend);
                
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
        
        public LineChart getChart()
            {
                return chart;
            }
        
        
        public float get_startdate()
            {
                return (float) chart.getXChartMin();
            }
        
        public float get_enddate()
            {
                return (float) chart.getXChartMax();
            }
        
        
        public ArrayList<String> get_curvelbl()
            {
                ArrayList<String> labels = new ArrayList<>();
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        labels.add(dataSets.get(i).getLabel());
                    }
                return labels;
            }
        
        public void setStats(LineDataSet data)
            {
                View t = (View) view.getParent();
                TextView txtMin = (TextView) t.findViewById(R.id.txtMinValue);
                TextView txtMax = (TextView) t.findViewById(R.id.txtMaxValue);
                TextView txtMoy = (TextView) t.findViewById(R.id.txtMean);
                TextView txtEcartType = (TextView) t.findViewById(R.id.txtDeviation);
                
                txtMin.setText(String.format("%.2f", data.getYMin()));
                txtMax.setText(String.format("%.2f", data.getYMax()));
                
                List<Entry> values = data.getValues();
                System.out.println("----------------");
                for (Entry i : values)
                    {
                        System.out.print(i.getY() + " - ");
                    }
                System.out.println("----------------");
                
                float moy = 0;
                for (Entry d : values)
                    {
                        moy += d.getY();
                    }
                moy = moy / data.getEntryCount();
                txtMoy.setText(String.format("%.2f", moy));
                
                float ecartType = 0;
                for (Entry d : values)
                    {
                        ecartType += (d.getY() - moy) * (d.getY() - moy);
                    }
                ecartType = ecartType / (data.getEntryCount() - 1);
                txtEcartType.setText(String.format("%.2f", Math.sqrt(ecartType)));
            }
        
        public void clearStats()
            {
                View t = (View) view.getParent();
                TextView txtMin = t.findViewById(R.id.txtMinValue);
                TextView txtMax = t.findViewById(R.id.txtMaxValue);
                TextView txtMoy = t.findViewById(R.id.txtMean);
                TextView txtEcartType = t.findViewById(R.id.txtDeviation);
                
                txtMax.setText("");
                txtMin.setText("");
                txtMoy.setText("");
                txtEcartType.setText("");
            }
    }
    
    
   
    
