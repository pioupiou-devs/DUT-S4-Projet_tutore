package fr.iut.orsay.myapplication;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private static final String DATABASE_URL = "jdbc:mariadb://78.116.137.76:3306/pt?user=usr1&password=pt1";
    private Connection connection;
    private PreparedStatement getTypes_ps;
    private PreparedStatement getSensor_ps;

    private Spinner spnSelector;
    private RadioButton radioType;
    private RadioButton radioSensor;
    private ListView dataList;
    private ListView currentData_lv;

    private String selectedSensor;
    private int selectedNumSensor;
    private String selectedType;
    private int selectedNumIPSO;
    private ArrayList<String> currentData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //get widgets references
        radioType = (RadioButton) findViewById(R.id.radioType);
        radioSensor = (RadioButton) findViewById(R.id.radioSensor);
        dataList = (ListView) findViewById(R.id.lstDataList);
        dataList.setOnItemClickListener(this);
        spnSelector = (Spinner) findViewById(R.id.spnSelector);
        spnSelector.setOnItemSelectedListener(this);
        currentData_lv = (ListView) findViewById(R.id.lstCurrentData);
        currentData = new ArrayList<>();

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
            connection = databaseConnecting.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        getTypes_ps = null;
        getSensor_ps = null;
        try {
            getTypes_ps = connection.prepareStatement(FilterActivity.this.getString(R.string.get_types_with_specified_sensor));
            getSensor_ps = connection.prepareStatement(FilterActivity.this.getString(R.string.get_sensors_with_specified_type));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRadioButtonClicked(View view) throws SQLException, ExecutionException, InterruptedException {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioSensor:
                if (checked) {
                    ArrayList<String> sensors = DatabaseTools.getSensors(connection, this.getString(R.string.get_sensors));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, sensors);
                    spnSelector.setAdapter(adapter);
                }
                break;
            case R.id.radioType:
                if (checked) {
                    ArrayList<String> types = DatabaseTools.getTypes(connection, this.getString(R.string.get_types));
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
                selectedSensor = (String) adapterView.getItemAtPosition(i);
                selectedNumSensor = Integer.parseInt(selectedSensor.substring(0, 1));
                ArrayList<String> types = null;
                try {
                    types = DatabaseTools.getTypes(selectedNumSensor, getTypes_ps);
                } catch (SQLException | ExecutionException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, types);
                dataList.setAdapter(adapter);
            }
            else if(radioType.isChecked()){
                selectedType = (String) adapterView.getItemAtPosition(i);
                selectedNumIPSO = Integer.parseInt(selectedType.substring(0, 1));
                ArrayList<String> sensors = null;
                try {
                    sensors = DatabaseTools.getSensors(selectedNumIPSO, getSensor_ps);
                } catch (SQLException | ExecutionException | InterruptedException throwables) {
                    throwables.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, sensors);
                dataList.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.lstDataList){
            if (radioSensor.isChecked()){
                selectedType = (String) adapterView.getItemAtPosition(i);
                selectedNumIPSO = Integer.parseInt(selectedType.substring(0, 1));
            }
            else if(radioType.isChecked()){
                selectedSensor = (String) adapterView.getItemAtPosition(i);
                selectedNumSensor = Integer.parseInt(selectedSensor.substring(0, 1));
            }
        }
        currentData.add("sensor : " + selectedSensor + ", type : " + selectedType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, currentData);
        currentData_lv.setAdapter(adapter);
    }
}
