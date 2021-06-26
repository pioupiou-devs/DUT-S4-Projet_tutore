package fr.iut.orsay.myapplication;

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
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.N) public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher
    {
        
        private static final String DATABASE_URL = "jdbc:mariadb://78.116.137.76:3306/pt?user=usr1&password=pt1";
        //private static final String DATABASE_URL = "jdbc:mariadb://192.168.44.18:3306/PT?user=user&password=user"; //TODO: changer l'url
        public static Graph selectedGraph;
        private PreparedStatement getTypes_ps;
        private PreparedStatement getSensor_ps;
        private GraphData graphData;
        //initialisation du listener de la barre de navigation du bas
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                {
                    //passage du graph sélectionné à la vue "list"
                    SelectionActivity.selectedGraph = selectedGraph;
                    finish(); //extinction de la vue actuelle
                }
            else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                {
                    //message d'erreur
                    if (selectedGraph == null)
                        {
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    //execution de la requete pour obtenir toutes les données des courbes et passage à la vue du graph
                    Intent chartIntent = new Intent(FilterActivity.this, CurveActivity.class);
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
                    //passage du graph sélectionné à la vue "curve"
                    CurveActivity.selectedGraph = selectedGraph;
                    //démarrage de l'activité "curve"
                    startActivity(chartIntent);
                }
            else
                return false;
            return true;
        };
        //android widget reference
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
         * @param date data a checker
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
         * @param savedInstanceState état de l'instance
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
                
                //si le graph sélectionné contient déjà des courbes : recharger les données sélectionnées dans la vue "filter"
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
                        
                        graphData.setEndDate(new Date((long) selectedGraph.get_enddate()));
                        graphData.setStartDate(new Date((long) selectedGraph.get_startdate()));
                        listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                        currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
                    }
                
                //affichage de l'entête avec le nom du graph sélectionné
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle(selectedGraph.getName());
                
                //établissement de la connexion à la bd de manière asynchrone
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
                
                //initialisation des preparedStatement pour les requêtes à la bd
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
                
                //déclaration de l'arraylist pour pouvoir ajouter librement des données par la suite
                graphData.setGraphsData(new ArrayList<>());
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        
        /**
         * est appelé lors d'un click sur l'un des radio buttons : envoie la requete pour obtenir la
         * liste des capteurs ou la liste des types de données
         *
         * @param view conteneur de notre bouton radio
         * @throws SQLException erreur retournée en cas d'erreur d'accès à la bd
         * @throws ExecutionException erreur retournée en cas d'erreur lors de l'exécution de la requête SQL
         * @throws InterruptedException erreur retournée en cas d'erreur lors de l'exécution de la requête SQL
         */
        @SuppressLint("NonConstantResourceId") public void onRadioButtonClicked(View view) throws SQLException, ExecutionException, InterruptedException
            {
                boolean checked = ((RadioButton) view).isChecked();
                
                //execution de la requete pour obtenir la liste des capteurs/types de données
                //et ajout de cette liste dans le sélecteur en dessus des radio buttons
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
         * @param adapterView conteneur de l'élément sélectionné, ici l'adapter
         * @param view conteneur de l'adapter
         * @param i position de l'élément sélectionné
         * @param l id de l'élément sélectionné
         */
        @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (adapterView.getId() == R.id.spnSelector)
                    {
                        if (radioSensor.isChecked())
                            {
                                //récupération de l'élément sélectionné
                                String sensor = (String) adapterView.getItemAtPosition(i);
                                int numSensor = Integer.parseInt(sensor.substring(0, 1));
                                ArrayList<String> types = null;
                                try
                                    {
                                        //exécution de la requête
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
                                //récupération de l'élément sélectionné
                                String type = (String) adapterView.getItemAtPosition(i);
                                int numIPSO = Integer.parseInt(type.substring(0, 1));
                                ArrayList<String> sensors = null;
                                try
                                    {
                                        //exécution de la requête
                                        sensors = DatabaseTools.getSensors(numIPSO, getSensor_ps);
                                    }
                                catch (SQLException | ExecutionException | InterruptedException throwables)
                                    {
                                        throwables.printStackTrace();
                                    }
                                listViewFilterAdapterDataList = new ListViewFilter(FilterActivity.this, sensors);
                            }
                        //ajout de la liste des données dans la list view sur la droite dans la vue
                        dataList.setAdapter(listViewFilterAdapterDataList);
                    }
            }
        
        
        @Override public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        
        /**
         * ajoute les données sélectionnées dans la list view "current data"
         *
         * @param view vue (conteneur)
         */
        public void addToCurrentData(View view)
            {
                ArrayList<String> valueToAdd;
                if (radioSensor.isChecked())
                    {
                        //récupération des éléments sélectionnés
                        ArrayList<String> currentSelectedTypes = listViewFilterAdapterDataList.getSelectedData();
                        for (int i = 0; i < currentSelectedTypes.size(); i++)
                            {
                                valueToAdd = new ArrayList<>(Arrays.asList(spnSelector.getSelectedItem().toString(), currentSelectedTypes.get(i)));
                                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                                //check si la donnée n'est pas déjà dans la liste "current data"
                                if (!currentData.contains(valueToAdd))
                                    {
                                        currentData.add(valueToAdd);
                                        graphData.setGraphsData(currentData);
                                    }
                            }
                    }
                else if (radioType.isChecked())
                    {
                        //récupération des éléments sélectionnés
                        ArrayList<String> currentSelectedSensors = listViewFilterAdapterDataList.getSelectedData();
                        for (int i = 0; i < currentSelectedSensors.size(); i++)
                            {
                                valueToAdd = new ArrayList<>(Arrays.asList(currentSelectedSensors.get(i), spnSelector.getSelectedItem().toString()));
                                ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                                //check si la donnée n'est pas déjà dans la liste "current data"
                                if (!currentData.contains(valueToAdd))
                                    {
                                        currentData.add(valueToAdd);
                                        graphData.setGraphsData(currentData);
                                    }
                            }
                    }
                //ajout des données dans la list view "current data"
                listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
            }
        
        /**
         * supprime les données sélectionnées dans "current data"
         *
         * @param view vue (conteneur)
         */
        public void removeFromCurrentData(View view)
            {
                
                //récupération des éléments sélectionnés
                ArrayList<String> selectedData = listViewFilterAdapterCurrentData.getSelectedData();
                for (int i = 0; i < selectedData.size(); i++)
                    {
                        String[] splitted = selectedData.get(i).split(":");
                        ArrayList<ArrayList<String>> currentData = graphData.getGraphsData();
                        currentData.remove(new ArrayList<>(Arrays.asList(splitted[0], splitted[1])));
                        graphData.setGraphsData(currentData);
                    }
                //ajout du nouveau jeu de données dans la list view "current data"
                listViewFilterAdapterCurrentData = new ListViewFilter(FilterActivity.this, graphData.getConcatenatedCurrentData());
                currentData_lv.setAdapter(listViewFilterAdapterCurrentData);
            }
        
        @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }
        
        @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }
        
        /**
         * récupère dans les chaines de caractère saisies dans les champs de date
         *
         * @param editable objet contenant le texte de l'edittext éditée
         */
        @Override public void afterTextChanged(Editable editable)
            {
                //check si les dates sont conformes
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm", Locale.FRANCE);
                if (isValidDate(editable.toString()))
                    {
                        if (startDateEditText.getText() == editable)
                            {
                                try
                                    {
                                        //initialisation/remplacement de la date de début
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
                                        //initialisation/remplacement de la date de fin
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
