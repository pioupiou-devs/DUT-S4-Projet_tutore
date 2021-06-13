package fr.iut.orsay.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectionActivity extends AppCompatActivity
    {
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_selection);
                
                ArrayList<Graph> list = new ArrayList<Graph>();
                list.add(new Graph("test"));
                list.add(new Graph("oklm"));
                list.add(new Graph("jules"));
                ListView listView = (ListView) findViewById(R.id.lstCurve);
                listView.setAdapter(new ListviewAdapter(list, this));
            }
    }
