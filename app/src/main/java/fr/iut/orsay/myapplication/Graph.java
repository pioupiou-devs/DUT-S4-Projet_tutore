package fr.iut.orsay.myapplication;

import android.content.Context;
import android.graphics.Color;
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
        // l'ID du graphique
        private static int NUM_ID = 0;
        //une limite de 5 courbes est en place, pour la gestion des couleurs
        private final int max_curve = 5;
        //le nom de la courbes qui sert a afficher le point selectionner
        private final String popval = "Point";
        //le nom du graphique
        String graphName;
        //le tableau des courbes
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        //le cadre graphique,mis a null par defaut tant qu'on a pas charger la page
        LineChart chart = null;
        private int id;
        //le contexte et la vu pour l'affichage
        private Context context;
        private View view;
        
        /**
         * Constructeur sans donee remplie
         *
         * @param graphName le nom du graphique
         */
        public Graph(String graphName)
            {
                this.graphName = graphName;
                
                //chaque graph a un id unnique pour eviter les probleme
                id = NUM_ID;
                NUM_ID++;
            }
        
        /**
         * Constructeur avec un tableau de courbe donne lors de la création
         *
         * @param graphName
         * @param dataSets
         */
        public Graph(String graphName, ArrayList<LineDataSet> dataSets)
            {
                this.graphName = graphName;
                
                //chaque graph a un id unnique pour eviter les probleme
                id = NUM_ID;
                NUM_ID++;
                
                this.dataSets = dataSets;
                
            }
        
        
        /**
         * création du cadre graphique pour afficher les donnees
         *
         * @param view    la vue dans laquel le grph doit etre affiché
         * @param context le contexte du cadre graphique
         */
        public void create_chart(View view, Context context)
            {
                //on attribut les context et vue
                this.context = context;
                this.view = view;
                
                //on cree un grapqhique temporaire pour construire le graphe
                LineChart new_chart = (LineChart) view;
                
                //encadre la graph
                new_chart.setDrawBorders(true);
                
                //defnint des comportements
                new_chart.setPinchZoom(true);
                new_chart.setTouchEnabled(true);
                new_chart.setDragDecelerationEnabled(false);
                
                //si on a pas recu de donnees on affiche un message
                new_chart.setNoDataText("Chart loading, please wait");
                
                //on affiche pas les labels des dates
                new_chart.getXAxis().setDrawLabels(false);
                
                //pour rendre les info des dates plus lisibles, elles sont vide de base
                new_chart.getDescription().setText("");
                new_chart.getDescription().setTextSize(15f);
                
                //Un formatter pour l'axe des dates pour pouvoir bien les afficher
                XAxis xAxis = new_chart.getXAxis();
                xAxis.setValueFormatter(new DateValueFormatter());
                
                //une fois tout bien calibre, on attribut le grpah
                this.chart = new_chart;
            }
        
        /**
         * sert a attibuer une couleur a chaque courbe
         *
         * @param set la courbe a recevoir une couleur
         * @param i   son index dans le graphique
         * @return la courbe avec la coueleur correcte
         */
        private LineDataSet set_curve_color(LineDataSet set, int i)
            {
                //un switch sur l'index de la courbe
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
                //on choisit la taille de la courbe et de ses points
                set.setLineWidth(1f);
                set.setCircleRadius(3f);
                set.setDrawCircleHole(false);
                set.setFormLineWidth(1f);
                set.setDrawValues(false);
                
                //une fois tout formatter, on renvoie
                return set;
            }
        
        /**
         * sert a ajouter une courbe dans le grpah
         *
         * @param label   le nom de la courbe
         * @param dataSet le tableau de valeur
         */
        @Override public void addDataSet(String label, ArrayList<Entry> dataSet)
            {
                if (dataSets.size() >= max_curve)
                    { //on a limiter le nombre de courbe
                        System.out.println("max number of data set for " + graphName + " (max is 4)");
                        return;
                    }
                
                //si on a pas atteint le nombre de courbe on peux ajouter la courbe au tableau de courbes
                dataSets.add(formatting_dataset(new LineDataSet(dataSet, label)));
    
            }
        
        /**
         * utiliser pour retirer la courbe du graph
         *
         * @param label le nom de la courbe a retirer
         */
        @Override public void removeDataSet(String label)
            {
                //on parcour le tableau de courbe
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //si la courbe est trouver, on la supprime
                        if (dataSets.get(i).getLabel().equals(label))
                            {
                                dataSets.remove(i);
                                return;
                            }
                    }
    
            }
        
        
        /**
         * pour recuperer la courbe a partir du nom
         *
         * @param label le nom de la courbe
         * @return
         */
        public LineDataSet getDataSet(String label)
            {
                //on parcour le tableau de courbe
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //on cherche par rapport au label et la renvoie
                        if (dataSets.get(i).getLabel().equals(label))
                            {
                                return dataSets.get(i);
                            }
                    }
                
                return null;
                
            }
        
        /**
         * affiche la courbe avec chaque courbe
         */
        @Override public void show()
            {
                //si il n'y a pas de courbe, on sort
                if (dataSets.size() == 0)
                    {
                        return;
                    }
                
                //on creer un objet temporaire pour l'affichage
                ArrayList<ILineDataSet> dataSe = new ArrayList<>();
                //on parcour le tableau de courbe
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //tant que ce n'est pas une courbe pour le point afficher, on choisit une couleur
                        if (!dataSets.get(i).getLabel().equals(popval))
                            {
                                dataSets.set(i, set_curve_color(dataSets.get(i), i));
                            }
                        
                        //on ajoute la courbe a l'objet graphique
                        dataSe.add(dataSets.get(i));
                    }
                
                //on link le listener
                LineData data = new LineData(dataSe);
                chart.setOnChartValueSelectedListener(this);
                
                //on affiche le graphique
                chart.setData(data);
                generate_legend();
    
            }
        
        
        /**
         * affiche une unique courbe dans le graqhique
         *
         * @param label le nom de la courbe a afficher
         */
        @Override public void showDataSet(String label)
            {
                ArrayList<ILineDataSet> dataSe = new ArrayList<>();
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (dataSets.get(i).getLabel().equals(label))
                            {
                                dataSe.add(dataSets.get(i));
                            }
                        
                    }
                
                LineData data = new LineData(dataSe);
                
                chart.setData(data);
    
    
            }
        
        /**
         * zoomer dans le graph
         */
        @Override public void zoomIn()
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un zoom sur la zone
                chart.zoom((float) 1.2, (float) 1.2, xValue, yValue);
            }
        
        /**
         * zoomer dans le graph avec une echele
         *
         * @param scale l'echele de zoom voulu
         */
        @Override public void zoomIn(int scale)
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un zoom sur la zone
                chart.zoom((float) 1 + Math.abs(scale), (float) 1 + Math.abs(scale), xValue, yValue);
            }
        
        
        /**
         * zoomer dans le graph avec un zoom non egale en hauteru et largeur
         *
         * @param scaleW l echele en largeur
         * @param scaleH l echele en hauteru
         */
        @Override public void zoomIn(int scaleW, int scaleH)
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un zoom sur la zone
                chart.zoom((float) 1 + Math.abs(scaleW), (float) 1 + Math.abs(scaleH), xValue, yValue);
                
            }
        
        /**
         * dezoomer dans le graph
         */
        @Override public void zoomOut()
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un dezoom sur la zone
                chart.zoom((float) .8, (float) .8, xValue, yValue);
                
            }
        
        /**
         * dezoomer dans le graph avec une echele
         *
         * @param scale l'echele de zoom voulu
         */
        @Override public void zoomOut(int scale)
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un dezoom sur la zone
                chart.zoom((float) 1 - Math.abs(scale), (float) 1 - Math.abs(scale), xValue, yValue);
            }
        
        /**
         * dezoomer dans le graph avec un zoom non egale en hauteru et largeur
         *
         * @param scaleW l echele en largeur
         * @param scaleH l echele en hauteur
         */
        @Override public void zoomOut(int scaleW, int scaleH)
            {
                //on recupere la zone visualiser actuelement
                float xValue = chart.getViewPortHandler().getContentCenter().x;
                float yValue = chart.getViewPortHandler().getContentCenter().y;
                
                //on fait un dezoom sur la zone
                chart.zoom((float) 1 - Math.abs(scaleW), (float) 1 - Math.abs(scaleH), xValue, yValue);
            }
        
        /**
         * renomer le graphique
         *
         * @param newLabel le nouveau nom
         */
        @Override public void rename(String newLabel)
            {
                this.graphName = newLabel;
            }
        
        /**
         * affiche les donner du graph dans la console (debug)
         *
         * @return les donnee du graph sous forme de String
         */
        @Override
        
        public String print()
            {
                //un string qui prend en charge les retour a la ligne
                StringBuilder out = new StringBuilder();
                
                //on ajoute le nom
                out.append("Data form graph ").append(graphName).append("\n");
                
                //on ajoute les courbes
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        out.append(printer(dataSets.get(i)));
                        out.append("...\n");
                    }
                //on a finit le grpah
                out.append("--------------------------------");
                
                return out.toString();
            }
        
        
        /**
         * affiche les valeure d'une courbe, en fonction du nom, dans la console  (debug)
         *
         * @param label le nom de la courbe a afficher
         * @return la courbe en string
         */
        @Override public String printDataSet(String label)
            {
                //un string qui prend en charge les retour a la ligne
                StringBuilder out = new StringBuilder();
                boolean found = false;
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (dataSets.get(i).getLabel().equalsIgnoreCase(label))
                            { //on cherche par rapport au label
                                out.append(printer(dataSets.get(i)));
                                found = true;
                            }
                    }
                
                //si on a pas trouver de courbe avec ce nom la,on l indique
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
         * @return la courbe en String
         */
        private String printer(ILineDataSet data)
            {
                //un string qui prend en charge les retour a la ligne
                StringBuilder out = new StringBuilder();
                
                out.append("Data set : ").append(data.getLabel()).append("\n");
                
                //on parcour le tableau de coordonner
                for (int i = 0; i < data.getEntryCount(); i++)
                    {
                        Entry temp = data.getEntryForIndex(i);
                        
                        out.append("\t - x :").append(temp.getX()).append("\t y :").append(temp.getY()).append("\n");
                    }
                
                return out.toString();
            }
        
        
        /**
         * L ecouteur du graphique, qui sert a afficher un point selectionner
         *
         * @param e le point selectionner
         * @param h la courbe selectionner
         */
        @Override public void onValueSelected(Entry e, Highlight h)
            {
                //on parcour le tableau de courbe pour trouver l index de la courbe
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        if (i == h.getDataSetIndex())
                            {
                                //on stock la courbe de temporairement (l index du highlight n est pas le meme que l index dans le graph)
                                LineDataSet sel = dataSets.get(i);
                                //on appel le generateur de point
                                popup(e, sel);
                                //on appel le calculateur des state
                                setStats(sel);
                                
                                return;
                            }
                    }
    
            }
        
        /**
         * ecouteur, pour deselectionner le point
         */
        @Override public void onNothingSelected()
            {
                //on appel le degenrateur de point
                removepopval();
                //on appel le videur des states
                clearStats();
                
            }
        
        /**
         * sert lors de la deselection d un point
         */
        private void removepopval()
            {
                //on enleve la courbe du point
                removeDataSet(popval);
                //on enleve l info de la date
                chart.getDescription().setText("");
                
                show();
    
            }
        
        /**
         * sert a afficher le point selectionner
         *
         * @param e le point selectionner
         * @param h la courbe du point selectionner
         */
        private void popup(Entry e, LineDataSet h)
            {
                
                //on enleve un eventuel point selectionner precedent
                removeDataSet(popval);
                
                //on recreer un tableau avec juste un point
                ArrayList<Entry> val = new ArrayList<>();
                val.add(e.copy());
                
                //on ajoute une courbe composer d un seul point
                LineDataSet point = new LineDataSet(val, popval);
                //on recupere la meme couleur de la courbe selectionner
                point.setColor(h.getColor());
                point.setCircleColor(h.getColor());
                
                point.setLineWidth(1f);
                point.setCircleRadius(6f);
                point.setDrawCircleHole(false);
                point.setValueTextSize(15f);
                point.setFormLineWidth(1f);
                point.setDrawValues(true);
                
                
                dataSets.add(point);
                
                //on met a jour l info de la date
                DateValueFormatter format = new DateValueFormatter();
                chart.getDescription().setText(format.getFormattedValue(e.getX()));
                show();
    
            }
        
        /**
         * sert a creer une legende pour le graph
         */
        private void generate_legend()
            {
                ArrayList<LegendEntry> legend = new ArrayList<>();
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        //on ne veut pas que le point selectionner apparaisse dans la liste des legendes
                        if (!dataSets.get(i).getLabel().equalsIgnoreCase(popval))
                            {
                                legend.add(new LegendEntry(dataSets.get(i).getLabel(), Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, dataSets.get(i).getColor()));
                            }
                    }
                
                //on ecrase la legende originel
                chart.getLegend().setCustom(legend);
                
            }
        
        /**
         * recupere l id du graph
         *
         * @return l id du graph
         */
        public int getId()
            {
                return id;
            }
        
        /**
         * donne le nom du graph
         *
         * @return le nom du graph
         */
        public String getName()
            {
                return graphName;
            }
        
        /**
         * initialise le nom du graph
         *
         * @param graphName le nouveau nom du graph
         */
        public void setName(String graphName)
            {
                this.graphName = graphName;
            }
        
        /**
         * donne le graph
         *
         * @return le graph
         */
        public LineChart getChart()
            {
                return chart;
            }
        
        /**
         * donne la premiere date du graph
         *
         * @return la premiere date du graph
         */
        public float get_startdate()
            {
                return chart.getXChartMin();
            }
        
        /**
         * donne la derniere date du graph
         *
         * @return la derniere date du graph
         */
        public float get_enddate()
            {
                return chart.getXChartMax();
            }
        
        /**
         * donne le nom des courbes
         *
         * @return un tableau de nom de courbes
         */
        public ArrayList<String> get_curvelbl()
            {
                ArrayList<String> labels = new ArrayList<>();
                
                for (int i = 0; i < dataSets.size(); i++)
                    {
                        labels.add(dataSets.get(i).getLabel());
                    }
                return labels;
            }
        
        /**
         * calcule et affiche les statistiques
         *
         * @param data la courbe a calculer
         */
        public void setStats(LineDataSet data)
            {
                View t = (View) view.getParent();
                TextView txtMin = t.findViewById(R.id.txtMinValue);
                TextView txtMax = t.findViewById(R.id.txtMaxValue);
                TextView txtMoy = t.findViewById(R.id.txtMean);
                TextView txtEcartType = t.findViewById(R.id.txtDeviation);
                
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
        
        /**
         * vide les text de statistique
         */
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
    
    
   
    
