package fr.iut.orsay.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import fr.iut.orsay.myapplication.DatabaseTools;
import fr.iut.orsay.myapplication.Graph;
import fr.iut.orsay.myapplication.GraphData;
import fr.iut.orsay.myapplication.ListViewFilter;
import fr.iut.orsay.myapplication.R;

@RequiresApi(api = Build.VERSION_CODES.N) public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher
    {
        private static final String DATABASE_URL = "jdbc:mariadb://192.168.1.81:3306/pt?user=usr1&password=pt1";
        
        private PreparedStatement getTypes_ps;
        private PreparedStatement getSensor_ps;
        
        private GraphData graphData;
        private Graph selectedGraph;
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                {
                    setResult(0, getIntent().putExtra("selectedGraph", selectedGraph));
                    finish();
                }
            else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                {
                    
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    Intent intent = new Intent(FilterActivity.this, CurveActivity.class);
                    try
                        {
                            for (Map.Entry<String, ArrayList<Entry>> entry : graphData.getData().entrySet())
                                {
                                    selectedGraph.addDataSet(entry.getKey(), entry.getValue());
                                }
                        }
                    catch (SQLException | ExecutionException | InterruptedException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    intent.putExtra("selectedGraph", selectedGraph);
                    startActivity(intent);
                }
            else
                return false;
            return true;
        };
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
        
        /**
         * check si une string est dans le format demandé
         *
         * @param date
         * @return retourne vrai si la date est dans le format demandé, faux sinon
         */
        public static boolean isValidDate(String date)
            {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
                dateFormat.setLenient(false);
                try
                    {
                        dateFormat.parse(date.trim());
                    }
                catch (ParseException e)
                    {
                        return false;
                    }
                return true;
            }
        
        /**
         * charge la page de filtres
         *
         * @param savedInstanceState
         */
        @Override protected void onCreate(Bundle savedInstanceState)
            {
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
                
                selectedGraph = (Graph) getIntent().getSerializableExtra("selectedGraph");
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle(selectedGraph.getName());
                
                if (selectedGraph.getChart() != null)
                    {
                        ArrayList<String> currentData = selectedGraph.get_curvelbl();
                        ArrayList<ArrayList<String>> splittedCurrentData = new ArrayList<>();
                        for (int i = 0; i < currentData.size(); i++)
                            {
                                String[] line = currentData.get(i).split(":");
                                splittedCurrentData.add(new ArrayList<>(Arrays.asList(line[0], line[1])));
                            }
                        graphData.setGraphsData(splittedCurrentData);
                        
                        graphData.setEndDate(null);
                        graphData.setStartDate(null);
                        listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                        currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
                        
                    }
                
                CompletableFuture<Connection> databaseConnecting = CompletableFuture.supplyAsync(() ->
                {
                    try
                        {
                            return DatabaseTools.openConnection(DATABASE_URL);
                        }
                    catch (SQLException | ClassNotFoundException throwables)
                        {
                            throwables.printStackTrace();
                        }
                    return null;
                });
                
                try
                    {
                        graphData = new GraphData(databaseConnecting.get(), FilterActivity.this);
                    }
                catch (SQLException | InterruptedException | ExecutionException throwables)
                    {
                        throwables.printStackTrace();
                    }
                
                getTypes_ps = null;
                getSensor_ps = null;
                try
                    {
                        getTypes_ps = graphData.getConnection().prepareStatement(FilterActivity.this.getString(R.string.get_types_with_specified_sensor));
                        getSensor_ps = graphData.getConnection().prepareStatement(FilterActivity.this.getString(R.string.get_sensors_with_specified_type));
                    }
                catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }
                
                graphData.setGraphsData(new ArrayList<>());
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        
        /**
         * est appelé lors d'un click sur l'un des radio buttons : envoie la requete pour obtenir la
         * liste des capteurs ou la liste des types de données
         *
         * @param view
         * @throws SQLException
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @SuppressLint("NonConstantResourceId") public void onRadioButtonClicked(View view) throws SQLException, ExecutionException, InterruptedException
            {
                boolean checked = ((RadioButton) view).isChecked();
                
                switch (view.getId())
                    {
                        case R.id.radioSensor:
                            if (checked)
                                {
                                    ArrayList<String> sensors = DatabaseTools.getSensors(graphData.getConnection(), this.getString(R.string.get_sensors));
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, sensors);
                                    spnSelector.setAdapter(adapter);
                                }
                            break;
                        case R.id.radioType:
                            if (checked)
                                {
                                    ArrayList<String> types = DatabaseTools.getTypes(graphData.getConnection(), this.getString(R.string.get_types));
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, types);
                                    spnSelector.setAdapter(adapter);
                                }
                            break;
                    }
            }
        
        /**
         * est appelé lors d'un click sur un élément du spinner : envoie une requête pour récupérer
         * les données correspondant au type de donnée ou au capteur sélectionné
         *
         * @param adapterView
         * @param view
         * @param i
         * @param l
         */
        @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (adapterView.getId() == R.id.spnSelector)
                    {
                        if (radioSensor.isChecked())
                            {
                                String sensor = (String) adapterView.getItemAtPosition(i);
                                int numSensor = Integer.parseInt(sensor.substring(0, 1));
                                ArrayList<String> types = null;
                                try
                                    {
                                        types = DatabaseTools.getTypes(numSensor, getTypes_ps);
                                    }
                                catch (SQLException | ExecutionException | InterruptedException throwables)
                                    {
                                        throwables.printStackTrace();
                                    }
                                listViewFilterAdapterDataList = new ListViewFilter(FilterActivity.this, types);
                            }
                        else if (radioType.isChecked())
                            {
                                String type = (String) adapterView.getItemAtPosition(i);
                                int numIPSO = Integer.parseInt(type.substring(0, 1));
                                ArrayList<String> sensors = null;
                                try
                                    {
                                        sensors = DatabaseTools.getSensors(numIPSO, getSensor_ps);
                                    }
                                catch (SQLException | ExecutionException | InterruptedException throwables)
                                    {
                                        throwables.printStackTrace();
                                    }
                                listViewFilterAdapterDataList = new ListViewFilter(FilterActivity.this, sensors);
                            }
                        dataList.setAdapter(listViewFilterAdapterDataList);
                    }
            }
        
        
        @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        
        /**
         * ajoute les données sélectionnées dans la list view "current data"
         *
         * @param view
         */
        public void addToCurrentData(View view)
            {
                ArrayList<String> valueToAdd;
                if (radioSensor.isChecked())
                    {
                        ArrayList<String> currentSelectedTypes = listViewFilterAdapterDataList.getSelectedData();
                        for (int i = 0; i < currentSelectedTypes.size(); i++)
                            {
                                valueToAdd = new ArrayList<>(Arrays.asList(spnSelector.getSelectedItem().toString(), currentSelectedTypes.get(i)));
                                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                                if (!currentData.contains(valueToAdd))
                                    {
                                        currentData.add(valueToAdd);
                                        graphData.setGraphsData(currentData);
                                    }
                            }
                    }
                else if (radioType.isChecked())
                    {
                        ArrayList<String> currentSelectedSensors = listViewFilterAdapterDataList.getSelectedData();
                        for (int i = 0; i < currentSelectedSensors.size(); i++)
                            {
                                valueToAdd = new ArrayList<>(Arrays.asList(currentSelectedSensors.get(i), spnSelector.getSelectedItem().toString()));
                                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                                if (!currentData.contains(valueToAdd))
                                    {
                                        currentData.add(valueToAdd);
                                        graphData.setGraphsData(currentData);
                                    }
                            }
                    }
                listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
            }
        
        /**
         * supprime les données sélectionnées dans "current data"
         *
         * @param view
         */
        public void removeFromCurrentData(View view)
            {
                ArrayList<String> selectedData = listViewFilterAdapterCurrentData.getSelectedData();
                for (int i = 0; i < selectedData.size(); i++)
                    {
                        String[] splitted = selectedData.get(i).split(":");
                        ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                        currentData.remove(new ArrayList<>(Arrays.asList(splitted[0], splitted[1])));
                        graphData.setGraphsData(currentData);
                    }
                listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
            }
        
        @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        
        @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        
        /**
         * récupère dans les chaines de caractère saisies dans les champs de date
         *
         * @param editable
         */
        @Override public void afterTextChanged(Editable editable)
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm", Locale.FRANCE);
                if (isValidDate(editable.toString()))
                    {
                        if (startDateEditText.getText() == editable)
                            {
                                try
                                    {
                                        graphData.setStartDate(dateFormat.parse(editable.toString()));
                                    }
                                catch (ParseException e)
                                    {
                                        e.printStackTrace();
                                    }
                            }
                        else if (endDateEditText.getText() == editable)
                            {
                                try
                                    {
                                        graphData.setEndDate(dateFormat.parse(editable.toString()));
                                    }
                                catch (ParseException e)
                                    {
                                        e.printStackTrace();
                                    }
                            }
                    }
            }
        
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
