package fr.iut.orsay.myapplication;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    {
    
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                LineChart mChart = findViewById(R.id.chart);
                mChart.setTouchEnabled(true);
                mChart.setPinchZoom(true);

                //DatabaseTools.getValues(this, 1);
                try {
                    DatabaseTools.openConnection("jdbc:mariadb://localhost/PT?useUnicode=yes&characterEncoding=UTF-8");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                ArrayList<Entry> values = new ArrayList<>();
                values.add(new Entry(1, 50));
                values.add(new Entry(2, 30));
                values.add(new Entry(3, 100));
                values.add(new Entry(5, 12));
                values.add(new Entry(7, 98));
                values.add(new Entry(8, 42));
                values.add(new Entry(6, 33));
                values.add(new Entry(4, 66));
                
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
                        mChart.setData(data);
                    }
            }
    }