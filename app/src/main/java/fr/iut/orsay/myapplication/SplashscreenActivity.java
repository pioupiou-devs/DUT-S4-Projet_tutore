package fr.iut.orsay.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashscreenActivity extends AppCompatActivity
    {
        @Override protected void onCreate(@Nullable Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.splashscreen);// créer le lien avec le fichier xml associé
                
                ProgressBar spinner = findViewById(R.id.progressBar_splashcreen); //créer le spinner et le lie à celui de la vue
                spinner.setVisibility(View.GONE); //stop le spinner
                spinner.setVisibility(View.VISIBLE); //affiche le spinner
                
                int SPLASH_TIME_OUT = 3000;
                //Créer un événement qui s'executera au bout de 3 secondes
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                {
                    //création de l'outil de navigation vers la page SelectionActivity
                    Intent intent = new Intent(SplashscreenActivity.this, SelectionActivity.class);
                    startActivity(intent); //démarre la page SelectionActivity
                    finish();//termine la page courante
                    
                }, SPLASH_TIME_OUT);
            }
    }
