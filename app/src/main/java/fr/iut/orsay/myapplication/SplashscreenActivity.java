package fr.iut.orsay.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashscreenActivity extends AppCompatActivity
    {
        @Override protected void onCreate(@Nullable Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.splashscreen);
                //Objects.requireNonNull(getSupportActionBar()).hide();
                ProgressBar spinner = findViewById(R.id.progressBar_splashcreen);
                spinner.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                
                int SPLASH_TIME_OUT = 3000;
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                {
                    Intent intent = new Intent(SplashscreenActivity.this, SelectionActivity.class);
                    startActivity(intent);
                    finish();
                    
                }, SPLASH_TIME_OUT);
            }
    }
