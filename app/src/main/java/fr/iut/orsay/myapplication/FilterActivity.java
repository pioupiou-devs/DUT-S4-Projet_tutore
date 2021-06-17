package fr.iut.orsay.myapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {
    private static final String DATABASE_URL = "jdbc:mariadb://78.116.137.76:3306/pt?user=usr1&password=pt1";

    private PreparedStatement getTypes_ps;
    private PreparedStatement getSensor_ps;

    private ArrayList<String> currentSelectedSensors;
    private ArrayList<String> currentSelectedTypes;

    private GraphData graphData;

    //android widget
    private Spinner spnSelector;
    private RadioButton radioType;
    private RadioButton radioSensor;
    private ListView dataList;
    private ListView currentData_lv;
    private ListViewFilter listViewFilterAdapterDataList;
    private ListViewFilter listViewFilterAdapterCurrentData;
    private EditText startDateEditText;
    private EditText endDateEditText;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //get widgets references
        radioType = findViewById(R.id.radioType);
        radioSensor = findViewById(R.id.radioSensor);
        dataList = findViewById(R.id.lstDataList);
        spnSelector = findViewById(R.id.spnSelector);
        currentData_lv = findViewById(R.id.lstCurrentData);
        startDateEditText = findViewById(R.id.startDate);
        endDateEditText = findViewById(R.id.endDate);

        //set listeners
        spnSelector.setOnItemSelectedListener(this);
        startDateEditText.addTextChangedListener(this);
        endDateEditText.addTextChangedListener(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);

        CompletableFuture<Connection> databaseConnecting = CompletableFuture.supplyAsync(() -> {
            Connection co = null;

            try {
                co = DatabaseTools.openConnection(DATABASE_URL);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            return co;
        });

        try {
            graphData = new GraphData(databaseConnecting.get(), FilterActivity.this);
        } catch (SQLException | InterruptedException | ExecutionException throwables) {
            throwables.printStackTrace();
        }

        getTypes_ps = null;
        getSensor_ps = null;
        try {
            getTypes_ps = graphData.getConnection().prepareStatement(FilterActivity.this.getString(R.string.get_types_with_specified_sensor));
            getSensor_ps = graphData.getConnection().prepareStatement(FilterActivity.this.getString(R.string.get_sensors_with_specified_type));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        graphData.setGraphsData(new ArrayList<>());
    
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    
    }
    
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    System.out.println(item);
                    if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                        {
                            Intent intent = new Intent(FilterActivity.this, FilterActivity.class); //TODO : replace with correct class
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                        {
                            Intent intent = new Intent(FilterActivity.this, FilterActivity.class); //TODO : replace with correct class
                            intent.putExtra("GraphData",new HashMap<>()); //TODO : replace the empty hasmap with data export from the request
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    else
                        return false;
                    return true;
                }
        };
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRadioButtonClicked(View view) throws SQLException, ExecutionException, InterruptedException {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioSensor:
                if (checked) {
                    ArrayList<String> sensors = DatabaseTools.getSensors(graphData.getConnection(), this.getString(R.string.get_sensors));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, sensors);
                    spnSelector.setAdapter(adapter);
                }
                break;
            case R.id.radioType:
                if (checked) {
                    ArrayList<String> types = DatabaseTools.getTypes(graphData.getConnection(), this.getString(R.string.get_types));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, types);
                    spnSelector.setAdapter(adapter);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spnSelector){
            if(radioSensor.isChecked()){
                String sensor = (String) adapterView.getItemAtPosition(i);
                int numSensor = Integer.parseInt(sensor.substring(0, 1));
                ArrayList<String> types = null;
                try {
                    types = DatabaseTools.getTypes(numSensor, getTypes_ps);
                } catch (SQLException | ExecutionException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, types);
                //dataList.setAdapter(adapter);
                listViewFilterAdapterDataList = new ListViewFilter(FilterActivity.this, types);
            }
            else if(radioType.isChecked()){
                String type = (String) adapterView.getItemAtPosition(i);
                int numIPSO = Integer.parseInt(type.substring(0, 1));
                ArrayList<String> sensors = null;
                try {
                    sensors = DatabaseTools.getSensors(numIPSO, getSensor_ps);
                } catch (SQLException | ExecutionException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
                listViewFilterAdapterDataList = new ListViewFilter(FilterActivity.this, sensors);
            }
            dataList.setAdapter(listViewFilterAdapterDataList);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void addToCurrentData(View view){
        ArrayList<String> valueToAdd;
        if(radioSensor.isChecked()){
            currentSelectedTypes = listViewFilterAdapterDataList.getSelectedData();
            for(int i = 0; i < currentSelectedTypes.size(); i++) {
                valueToAdd = new ArrayList<>(Arrays.asList(spnSelector.getSelectedItem().toString(), currentSelectedTypes.get(i)));
                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                if (!currentData.contains(valueToAdd)) {
                    currentData.add(valueToAdd);
                    graphData.setGraphsData(currentData);
                }
            }
        }
        else if(radioType.isChecked()){
            currentSelectedSensors = listViewFilterAdapterDataList.getSelectedData();
            for(int i = 0; i < currentSelectedSensors.size(); i++){
                valueToAdd = new ArrayList<>(Arrays.asList(currentSelectedSensors.get(i), spnSelector.getSelectedItem().toString()));
                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                if(!currentData.contains(valueToAdd)) {
                    currentData.add(valueToAdd);
                    graphData.setGraphsData(currentData);
                }
            }
        }
        ArrayList<String> concatenateCurrentData = new ArrayList<>();
        for (int i = 0; i < graphData.getGraphsData().size(); i++){
            concatenateCurrentData.add(graphData.getGraphsData().get(i).get(0) + ":" + graphData.getGraphsData().get(i).get(1));
        }
        listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, concatenateCurrentData);
        currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
    }

    public void removeFromCurrentData(View view) {
        ArrayList<String> selectedData = listViewFilterAdapterCurrentData.getSelectedData();
        for (int i = 0; i < selectedData.size(); i++) {
            String[] splitted = selectedData.get(i).split(":");
            ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
            currentData.remove(new ArrayList<>(Arrays.asList(splitted[0], splitted[1])));
            graphData.setGraphsData(currentData);
        }

        ArrayList<String> concatenateCurrentData = new ArrayList<>();
        for (int i = 0; i < graphData.getGraphsData().size(); i++) {
            concatenateCurrentData.add(graphData.getGraphsData().get(i).get(0) + ":" + graphData.getGraphsData().get(i).get(1));
        }
        listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, concatenateCurrentData);
        currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        //if (editable.)
        //TODO : check le format date pour les deux EditText
    }
}
