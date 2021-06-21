package fr.iut.orsay.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class ListViewFilter extends BaseAdapter implements ListAdapter
    {
        
        private final Context context;
        private final ArrayList<String> data;
        private final ArrayList<String> selectedData;
        
        public ListViewFilter(Context context, ArrayList<String> data)
            {
                this.context = context;
                this.data = data;
                selectedData = new ArrayList<>();
            }
        
        public ArrayList<String> getSelectedData()
            {
                return selectedData;
            }
        
        @Override public int getCount()
            {
                return data.size();
            }
        
        @Override public Object getItem(int i)
            {
                return data.get(i);
            }
        
        @Override public long getItemId(int i)
            {
                return i;
            }
        
        @Override public View getView(int i, View view, ViewGroup viewGroup)
            {
                if (view == null)
                    {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.simple_list_item_multiple_choice, viewGroup, false);
                    }
                Button txt = view.findViewById(R.id.textView2);
                txt.setText(data.get(i));
                
                txt.setOnClickListener(view1 ->
                {
                    if (selectedData.contains(data.get(i)))
                        {
                            selectedData.remove(data.get(i));
                            view1.setBackgroundColor(Color.WHITE); //TODO : fix les couleurs
                        }
                    else
                        {
                            selectedData.add(data.get(i));
                            view1.setBackgroundColor(Color.GRAY);
                        }
                });
                
                return view;
            }
    }
