package fr.iut.orsay.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class CurveActivity extends AppCompatActivity
    {
        
        private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item ->
        {
            System.out.println(item);
            if (getResources().getString(R.string.menuList).equalsIgnoreCase((String) item.getTitle()))
                {
                    Intent intent = new Intent(CurveActivity.this, SelectionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            else if (getResources().getString(R.string.menuFilter).equalsIgnoreCase((String) item.getTitle()))
                {
                    Intent intent = new Intent(CurveActivity.this, FilterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            else
                return false;
            return true;
        };
        
        @Override protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_curve);
                
                androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setToolbarTitle("None");
                
                //Graph test = new Graph("graph de test", 50, 50, 50, 50, this);
                
                Graph test = new Graph("graph de test");
                test.create_chart(findViewById(R.id.chart), this);
                
                ArrayList<Entry> val = new ArrayList<>();
                val.add(new Entry(100000000, 50));
                val.add(new Entry(200000000, 30));
                val.add(new Entry(300000000, 35));
                val.add(new Entry(400000000, 66));
                val.add(new Entry(500000000, 65));
                val.add(new Entry(600000000, 47));
                val.add(new Entry(700000000, 54));
                val.add(new Entry(800000000, 42));
                
                ArrayList<Entry> val2 = new ArrayList<>();
                val2.add(new Entry(1, 26));
                val2.add(new Entry(2, 47));
                val2.add(new Entry(3, 49));
                val2.add(new Entry(4, 53));
                val2.add(new Entry(5, 45));
                val2.add(new Entry(6, 40));
                val2.add(new Entry(7, 34));
                val2.add(new Entry(8, 29));
                
                ArrayList<Entry> val3 = new ArrayList<>();
                val3.add(new Entry(new java.util.Date().getTime(), 21));
                val3.add(new Entry(new java.util.Date().getTime(), 56));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                val3.add(new Entry(new java.util.Date().getTime(), 89));
                val3.add(new Entry(new java.util.Date().getTime(), 56));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                val3.add(new Entry(new java.util.Date().getTime(), 46));
                val3.add(new Entry(new java.util.Date().getTime(), 87));
                
                String name1 = "erste Kurve";
                String name2 = "zweiten Kurve";
                String name3 = "dritten Kurve";
                
                test.addDataSet(name1, val);
                //test.addDataSet(name2, val2);
                //test.addDataSet(name3,val3);
                
                //test.removeDataSet(name1);
                
                System.out.println("####################################################################");
                
                System.out.println(test.printDataSet(name1));
                System.out.println("####################################################################");
                System.out.println(test.printDataSet("error test"));
                System.out.println("####################################################################");
                System.out.println(test.print());
                
                System.out.println("####################################################################");
                
                test.show();
                
                System.out.println(test.get_startdate());
                System.out.println(test.get_enddate());
                //test.showDataSet(name1);
                
                //test.update();
                
                
                Button btnZoomAdd = findViewById(R.id.btnZoomAdd);
                btnZoomAdd.setOnClickListener(view ->
                {
                    System.out.println("Zoom IN");
                    test.zoomIn();
                });
                Button btnZoomLess = findViewById(R.id.btnZoomLess);
                btnZoomLess.setOnClickListener(view ->
                {
                    System.out.println("Zoom OUT");
                    test.zoomOut();
                });
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
                
            }
        
        public void setToolbarTitle(String title)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Selected Graph : " + title);
            }
    }