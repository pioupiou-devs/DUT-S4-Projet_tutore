package fr.iut.orsay.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
        private final ActivityResultLauncher<Intent> filterActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
            assert result.getData() != null;
            this.selectedGraph = (Graph) result.getData().getSerializableExtra("selectedGraph");
            System.out.println(result.getData().getExtras());
        });
        
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            
            
            if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    
                    Intent filterIntent = new Intent(this, FilterActivity.class);
                    filterIntent.putExtra("selectedGraph", selectedGraph);
                    filterActivityLauncher.launch(filterIntent);
                }
            else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    else if (selectedGraph.getChart() == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    
                    Intent chartIntent = new Intent(this, CurveActivity.class);
                    chartIntent.putExtra("selectedGraph", selectedGraph);
                    startActivity(chartIntent);
                    //chartActivityLauncher.launch(chartIntent);
                }
            else
                return false;
            return true;
        };
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);
                
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle("None");
                
                ListView lstCurve = findViewById(R.id.lstCurve);
                ArrayList<Graph> list = (getIntent().getSerializableExtra("graphList") != null) ? (ArrayList<Graph>) getIntent().getSerializableExtra("graphList") : new ArrayList<>();
                lstCurve.setAdapter(new ListviewAdapter(list, this));
                selectedGraph = (getIntent().getSerializableExtra("selectedGraph") != null) ? (Graph) getIntent().getSerializableExtra("selectedGraph") : ((ListviewAdapter) lstCurve.getAdapter()).getSelectedGraph();
                
                findViewById(R.id.btnCreate).setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.modalTextBoxTitle);
                    
                    final EditText input = new EditText(builder.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                    {
                        dialog.dismiss();
                        selectedGraph = new Graph(input.getText().toString());
                        ((ListviewAdapter) lstCurve.getAdapter()).addGraph(selectedGraph);
                        setToolbarTitle(selectedGraph.getName());
                    });
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                    
                    builder.show();
                    
                });
                
                findViewById(R.id.btnExportPDF).setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
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
                
                findViewById(R.id.btnExportPNG).setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
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
                
                findViewById(R.id.btnExportCSV).setOnClickListener(view ->
                {
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
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
                
                ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(navListener);
            }
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
