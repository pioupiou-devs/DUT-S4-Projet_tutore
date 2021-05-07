package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    {
    
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                LineChart mChart = findViewById(R.id.chart);
                mChart.setTouchEnabled(true);
                mChart.setPinchZoom(true);
                
                ArrayList<Entry> values = new ArrayList<>();
                values.add(new Entry(1, 50));
                values.add(new Entry(2, 30));
                values.add(new Entry(3, 35));
                values.add(new Entry(4, 66));
                values.add(new Entry(5, 65));
                values.add(new Entry(6, 47));
                values.add(new Entry(7, 54));
                values.add(new Entry(8, 42));
                
                
                
                LineDataSet set1;
                if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0)
                    {
                        set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                        set1.setValues(values);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                    }
                else
                    {
                        set1 = new LineDataSet(values, "Sample Data");
                        set1.setDrawIcons(false);
                        
                        
                        //set1.enableDashedLine(10f, 5f, 0f);
                        
                        //set1.enableDashedHighlightLine(10f, 5f, 0f);
                        
                        /*//to make the smooth line as the graph is adrapt change so smooth curve
                        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        //to enable the cubic density : if 1 then it will be sharp curve
                        set1.setCubicIntensity(0.2f);*/
                        
                        
                        set1.setColor(Color.BLACK);
                        set1.setCircleColor(Color.BLACK);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setDrawCircleHole(false);
                        set1.setValueTextSize(9f);
                        //set1.setDrawFilled(true);
                        set1.setDrawFilled(false);
                        set1.setFormLineWidth(1f);
                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                        set1.setFormSize(15.f);
                        
                        /*if (Utils.getSDKInt() >= 18)
                            {
                                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                                set1.setFillDrawable(drawable);
                            }
                        else
                            {
                                set1.setFillColor(Color.DKGRAY);
                            }*/
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);
                        
                        mChart.setDrawBorders(true); //encadre la graph
                        
                        // don't forget to refresh the drawing
                        //chart.invalidate();
                        
                        LineData data = new LineData(dataSets);
                        mChart.setData(data);
                    }
               // LineDataSet set1;
                /*if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0)
                    {
                        set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                        set1.setValues(values);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                    }
                else
                    {
                        set1 = new LineDataSet(values, "Sample Data");
                        set1.setDrawIcons(false);
                        set1.enableDashedLine(10f, 5f, 0f);
                        set1.enableDashedHighlightLine(10f, 5f, 0f);
                        set1.setColor(Color.DKGRAY);
                        set1.setCircleColor(Color.DKGRAY);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setDrawCircleHole(false);
                        set1.setValueTextSize(9f);
                        set1.setDrawFilled(true);
                        set1.setFormLineWidth(1f);
                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                        set1.setFormSize(15.f);
                        if (Utils.getSDKInt() >= 18)
                            {
                                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                                set1.setFillDrawable(drawable);
                            }
                        else
                            {
                                set1.setFillColor(Color.DKGRAY);
                            }
                        
                        
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);
                        
                        LineData data = new LineData(dataSets);
                        
                        mChart.setData(data);
                    }
                    
                 */
                
                
                Graph test = new Graph("graph de test", 5, 5, 5, 5);
    
                ArrayList<Entry> val = new ArrayList<>();
                val.add(new Entry(1, 50));
                val.add(new Entry(2, 30));
                val.add(new Entry(3, 35));
                val.add(new Entry(4, 66));
                val.add(new Entry(5, 65));
                val.add(new Entry(6, 47));
                val.add(new Entry(7, 54));
                val.add(new Entry(8, 42));
    
                ArrayList<Entry> val2 = new ArrayList<>();
                val2.add(new Entry(1, 12));
                val2.add(new Entry(2, 65));
                val2.add(new Entry(3, 78));
                val2.add(new Entry(4, 98));
                val2.add(new Entry(5, 65));
                val2.add(new Entry(6, 78));
                val2.add(new Entry(7, 64));
                val2.add(new Entry(8, 78));
                
                String name1= "erste Kurve";
                String name2= "zweiten Kurve";
                
                test.addDataSet(name1,val);
                test.addDataSet(name2,val2);
    
                System.out.println("####################################################################");
                
                System.out.println(test.printDataSet(name1));
                System.out.println("####################################################################");
                System.out.println(test.printDataSet("error test"));
                System.out.println("####################################################################");
                System.out.println(test.print());
    
                System.out.println("####################################################################");
                
            }
    }