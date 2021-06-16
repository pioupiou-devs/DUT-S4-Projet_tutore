package fr.iut.orsay.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                
                BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        
        
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        System.out.println(item);
                        if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                            {
                                Intent intent = new Intent(SelectionActivity.this, SelectionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        else if (getResources().getString(R.string.menuCurve).equalsIgnoreCase((String) item.getTitle()))
                            {
                                Intent intent = new Intent(SelectionActivity.this, CurveActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        else
                            return false;
                        return true;
                    }
            };
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }
