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
        public static Graph selectedGraph;
        //launcher pour l'activity filter
        private final ActivityResultLauncher<Intent> filterActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
        });
        //Listener sur les élements de la barre de navigation
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            //si nous avons clicker sur le bouton filter
            if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    //si un graphique n'est pas séléctionné
                    if (selectedGraph == null)
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    //sinon
                    Intent filterIntent = new Intent(this, FilterActivity.class); //créer l'outil de navigation vers la page filter
                    FilterActivity.selectedGraph = selectedGraph; //ajouter le graphique séléctionner à la page filter
                    filterActivityLauncher.launch(filterIntent); //démarrer la page filter
                }
                //si nous avons clicker sur le bouton Curve
            else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                {
                    //si un graphique n'est pas séléctionné
                    if (selectedGraph == null)
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    else if (selectedGraph.getChart() == null) //si le graphique est vide
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    
                    Intent chartIntent = new Intent(this, CurveActivity.class); //créer l'outil de navigation vers la page curve
                    startActivity(chartIntent); //démarrer la page filter
                }
            else
                return false;
            return true;
        };
    
        /**
         * A la création de la page selection
         * @param savedInstanceState état de l'instance
         */
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);// créer le lien avec le fichier xml associé
                
                //Demande des permission à l'utilisateur
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET ,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar); //créer la barre du haut d'écran et le lie à celui de la vue
                setSupportActionBar(toolbar); //ajouter cette barre à l'activité courante
                setToolbarTitle("None"); //définir le nom par défaut de cette activité
                
                ListView lstCurve = findViewById(R.id.lstCurve); //créer le listview et le lie à celui de la vue
                lstCurve.setAdapter(new ListviewAdapter(new ArrayList<>(), this)); //ajoute de la configuration personalisé au listview
                
                //si on click sur le bouton de création de graphiques
                findViewById(R.id.btnCreate).setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);//création d'une popup
                    builder.setTitle(R.string.modalTextBoxTitle);//nom de la popup
                    
                    final EditText input = new EditText(builder.getContext()); //création d'un élément de text éditable
                    input.setInputType(InputType.TYPE_CLASS_TEXT); //spécification du type de données
                    builder.setView(input);//ajout de l'élément de text éditable
    
                    //si on click sur "OK"
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                    {
                        dialog.dismiss();// on ferme la popup
                        selectedGraph = new Graph(input.getText().toString()); //création du graphique
                        ((ListviewAdapter) lstCurve.getAdapter()).addGraph(selectedGraph); //ajout du graphique au listview
                        setToolbarTitle(selectedGraph.getName()); //mise à jour du nom du listview
                    });
                    //si on click sur "Cancel"
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                    
                    builder.show(); //affiche la popup
                    
                });
                
                //si on click sur le bouton d'export PDF
                findViewById(R.id.btnExportPDF).setOnClickListener(view ->
                {
                    //si un graphique n'est pas séléctionné
                    if (selectedGraph == null)
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null) //si le graphique est vide
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            //sinon
                            String path = ExportGraph.exportToPDF(selectedGraph.getChart(), "graph"); //exporter le graphique au format PDF
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show(); //afficher le chemin vers le fichier
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace(); //afficher l'erreur
                        }
                });
    
                //si on click sur le bouton d'export PNG
                findViewById(R.id.btnExportPNG).setOnClickListener(view ->
                {
                    //si un graphique n'est pas séléctionné
                    if (selectedGraph == null)
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null) //si le graphique est vide
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            System.out.println(selectedGraph.print());
                            String path = ExportGraph.exportToPNG(selectedGraph.getChart(), "graph"); //exporter le graphique au format PNG
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show();  //afficher le chemin vers le fichier
                        }
                    catch (IOException e)
                        {
                            e.printStackTrace(); //afficher l'erreur
                        }
                    
                });
    
                //si on click sur le bouton d'export CSV
                findViewById(R.id.btnExportCSV).setOnClickListener(view ->
                {
                    //si un graphique n'est pas séléctionné
                    if (selectedGraph == null)
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.selected_graph), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    else if (selectedGraph.getChart() == null) //si le graphique est vide
                        {
                            //afficher un message d'erreur
                            Toast.makeText(this, getResources().getString(R.string.goToFilter), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    try
                        {
                            String path = ExportGraph.exportToCSV(selectedGraph.getChart(), "graph"); //exporter le graphique au format CSV
                            Toast.makeText(this, "File exported at " + path, Toast.LENGTH_SHORT).show();  //afficher le chemin vers le fichier
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace(); //afficher l'erreur
                        }
                    
                });
                
                //ajout de l'évenement de click sur la barre de navigation
                ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(navListener);
            }
    
        /**
         * Définit le titre de la barre du haut
         * @param title titre à insérer
         */
        public void setToolbarTitle(String title)
            {
                //si le titre est valide on met à jour le titre de la page
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
