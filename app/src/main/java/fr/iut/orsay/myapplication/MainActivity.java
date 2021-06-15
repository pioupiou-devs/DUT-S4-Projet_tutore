 package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

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
                 
                
                
                //Graph test = new Graph("graph de test", 50, 50, 50, 50, this);
    
                Graph test = new Graph("graph de test", 50, 50, 50, 50, findViewById(R.id.chart));
    
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
                val2.add(new Entry(1, 0));
                val2.add(new Entry(2, 1));
                val2.add(new Entry(3, 0));
                val2.add(new Entry(4, 1));
                val2.add(new Entry(5, 0));
                val2.add(new Entry(6, 1));
                val2.add(new Entry(7, 0));
                val2.add(new Entry(8, 1));
    
                ArrayList<Entry> val3 = new ArrayList<>();
                val3.add(new Entry(new java.util.Date().getTime(), 21));
                val3.add(new Entry(new java.util.Date().getTime(), 56));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                val3.add(new Entry(new java.util.Date().getTime(), 89));
                val3.add(new Entry(new java.util.Date().getTime(), 56));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                val3.add(new Entry(new java.util.Date().getTime(), 46));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                
                String name1= "erste Kurve";
                String name2= "zweiten Kurve";
                String name3= "dritten Kurve";
                
                test.addDataSet(name1,val);
                //test.addDataSet(name2,val2);
                //test.addDataSet(name3,val3);
                
                //test.removeDataSet(name1);
    
                System.out.println("####################################################################");
                
                System.out.println(test.printDataSet(name1));
                System.out.println("####################################################################");
                System.out.println(test.printDataSet("error test"));
                System.out.println("####################################################################");
                System.out.println(test.print());
    
                System.out.println("####################################################################");
                
                test.show();
                
                //test.showDataSet(name1);
    
                //test.update();
                
                
            }
            
            
            
            String formatter(){
            
            
            return "";
               // LineDataSet set1;
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
                        System.out.println("data: " + data.toString());
                        mChart.setData(data);
                    }
                
                Button button = findViewById(R.id.button);
                button.setOnClickListener(v ->
                {
                    ExportGraph.exportToPNG(mChart, "TESTDATA");
                });
            }
    }