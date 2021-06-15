package fr.iut.orsay.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class SelectionActivity extends AppCompatActivity
    {
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle("None");
                
                ArrayList<Graph> list = new ArrayList<Graph>();
                //list.add(new Graph("test"));
                //list.add(new Graph("oklm"));
                //list.add(new Graph("jules"));
                ListView listView = (ListView) findViewById(R.id.lstCurve);
                listView.setAdapter(new ListviewAdapter(list, this));
            }
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
