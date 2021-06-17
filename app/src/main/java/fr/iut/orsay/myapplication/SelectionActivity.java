package fr.iut.orsay.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SelectionActivity extends AppCompatActivity
    {
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle("None");
                
                ArrayList<Graph> list = new ArrayList<Graph>();
                list.add(new Graph("test"));
                list.add(new Graph("oklm"));
                list.add(new Graph("jules"));
                ListView lstCurve = (ListView) findViewById(R.id.lstCurve);
                lstCurve.setAdapter(new ListviewAdapter(list, this));
                Graph selectedGraph = ((ListviewAdapter) lstCurve.getAdapter()).getSelectedGraph();
                
                Button btnCreate = (Button) findViewById(R.id.btnCreate);
                btnCreate.setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.modalTextBoxTitle);
                    
                    final EditText input = (EditText) new EditText(builder.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                    {
                        dialog.dismiss();
                        Graph newGraph = new Graph(input.getText().toString());
                        ((ListviewAdapter) lstCurve.getAdapter()).addGraph(newGraph);
                    });
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                    
                    builder.show();
                    
                });
                
                Button btnExportPDF = (Button) findViewById(R.id.btnExportPDF);
                btnExportPDF.setOnClickListener(view ->
                {
                    System.out.println(selectedGraph);
                    if (selectedGraph == null)
                        return;
                    try
                        {
                            //TODO : add little popup to tell the user that's work + path
                            ExportGraph.exportToPDF(selectedGraph.getChart(), "graph");
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                });
                
                Button btnExportPNG = (Button) findViewById(R.id.btnExportPNG);
                btnExportPNG.setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        return;
                    try
                        {
                            //TODO : add little popup to tell the user that's work + path
                            ExportGraph.exportToPNG(selectedGraph.getChart(), "graph");
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    
                });
                
                Button btnExportCSV = (Button) findViewById(R.id.btnExportCSV);
                btnExportCSV.setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        return;
                    try
                        {
                            //TODO : add little popup to tell the user that's work + path
                            ExportGraph.exportToCSV(selectedGraph.getChart(), "graph");
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    
                });
                
                BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        
        
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        System.out.println(item);
                        if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                            {
                                Intent intent = new Intent(SelectionActivity.this, SelectionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                            {
                                Intent intent = new Intent(SelectionActivity.this, CurveActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        else
                            return false;
                        return true;
                    }
            };
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
