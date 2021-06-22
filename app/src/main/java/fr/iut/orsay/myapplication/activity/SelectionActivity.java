package fr.iut.orsay.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import fr.iut.orsay.myapplication.ExportGraph;
import fr.iut.orsay.myapplication.Graph;
import fr.iut.orsay.myapplication.ListviewAdapter;
import fr.iut.orsay.myapplication.R;

public class SelectionActivity extends AppCompatActivity
    {
        private Graph selectedGraph;
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);
                
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle("None");
                
                ArrayList<Graph> list = new ArrayList<>();
                list.add(new Graph("test"));
                list.add(new Graph("oklm"));
                list.add(new Graph("jules"));
                ListView lstCurve = findViewById(R.id.lstCurve);
                lstCurve.setAdapter(new ListviewAdapter(list, this));
                selectedGraph = ((ListviewAdapter) lstCurve.getAdapter()).getSelectedGraph();
                
                Button btnCreate = findViewById(R.id.btnCreate);
                btnCreate.setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.modalTextBoxTitle);
                    
                    final EditText input = new EditText(builder.getContext());
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
                
                Button btnExportPDF = findViewById(R.id.btnExportPDF);
                btnExportPDF.setOnClickListener(view ->
                {
                    System.out.println(selectedGraph);
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            String path = ExportGraph.exportToPDF(selectedGraph.getChart(), "graph");
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show();
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                });
                
                Button btnExportPNG = findViewById(R.id.btnExportPNG);
                btnExportPNG.setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            String path = ExportGraph.exportToPNG(selectedGraph.getChart(), "graph");
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show();
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    
                });
                
                Button btnExportCSV = findViewById(R.id.btnExportCSV);
                btnExportCSV.setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            String path = ExportGraph.exportToCSV(selectedGraph.getChart(), "graph");
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show();
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    
                });
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        
        
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    if (selectedGraph == null) //TODO : check that we can go to filter with a new graph or a "real" graph + cannot without
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    Intent intent = new Intent(SelectionActivity.this, FilterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectedGraph", selectedGraph);
                    startActivity(intent);
                }
            else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                {
                    if (selectedGraph == null) //TODO : check that we can go to filter with a new graph or a "real" graph + cannot without
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    Intent intent = new Intent(SelectionActivity.this, CurveActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            else
                return false;
            return true;
        };
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
