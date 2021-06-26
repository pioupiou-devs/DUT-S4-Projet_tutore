package fr.iut.orsay.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class CurveActivity extends AppCompatActivity
    {
        public static Graph selectedGraph;
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            System.out.println(item);
            if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                {
                    finish();
                }
            else if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    finish();
                }
            else
                return false;
            return true;
        };
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_curve);
                
                //System.out.println("koala rouge : " + new Gson().fromJson(getIntent().getStringExtra("selectedGraph"),Graph.class));
                //selectedGraph = new Gson().fromJson(getIntent().getStringExtra("selectedGraph"),Graph.class);
                selectedGraph = FilterActivity.selectedGraph;
                selectedGraph.create_chart(findViewById(R.id.chart), this);
                selectedGraph.show();
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle(selectedGraph.getName());
                
                Button btnZoomAdd = findViewById(R.id.btnZoomAdd);
                btnZoomAdd.setOnClickListener(view ->
                {
                    System.out.println("Zoom IN");
                    selectedGraph.zoomIn();
                });
                Button btnZoomLess = findViewById(R.id.btnZoomLess);
                btnZoomLess.setOnClickListener(view ->
                {
                    System.out.println("Zoom OUT");
                    selectedGraph.zoomOut();
                });
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
                
            }
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }