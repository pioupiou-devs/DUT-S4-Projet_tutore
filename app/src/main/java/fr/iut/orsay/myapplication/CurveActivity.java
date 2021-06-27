package fr.iut.orsay.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class CurveActivity extends AppCompatActivity
    {
        public static Graph selectedGraph;
        //Listener sur les élements de la barre de navigation
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            //si nous avons clicker sur le bouton list
            if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                {
                    finish(); //termine l'activité courante
                }
            //si nous avons clicker sur le bouton filter
            else if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    finish(); //termine l'activité courante
                }
            else
                return false;
            return true;
        };
    
        /**
         * A la création de la page curve
         *
         * @param savedInstanceState état de l'instance
         */
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_curve); // créer le lien avec le fichier xml associé
                
                selectedGraph = FilterActivity.selectedGraph; // récupère le graphique dans Filter
                selectedGraph.create_chart(findViewById(R.id.chart), this); // créer le graphique et le lie à la page
                selectedGraph.show(); // affiche le graphique
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);  //créer la barre du haut d'écran et le lie à celui de la vue
                setSupportActionBar(toolbar); //ajouter cette barre à l'activité courante
                setToolbarTitle(selectedGraph.getName());  //définir le nom du graphique séléctionné
    
                //si on click sur le bouton de zoom avant
                findViewById(R.id.btnZoomAdd).setOnClickListener(view ->
                {
                    System.out.println("Zoom IN");
                    selectedGraph.zoomIn();
                });
    
                //si on click sur le bouton de zoom arrière
                findViewById(R.id.btnZoomLess).setOnClickListener(view ->
                {
                    System.out.println("Zoom OUT");
                    selectedGraph.zoomOut();
                });
    
                //ajout de l'évenement de click sur la barre de navigation
                ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(navListener);
                
            }
    
        /**
         * Définit le titre de la barre du haut
         *
         * @param title titre à insérer
         */
        public void setToolbarTitle(String title)
            {
                //si le titre est valide on met à jour le titre de la page
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }