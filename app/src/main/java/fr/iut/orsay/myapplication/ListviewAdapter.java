package fr.iut.orsay.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter implements ListAdapter
    {
        private ArrayList<String> list = new ArrayList<>();
        private Context context;
    
        public ListviewAdapter(ArrayList<String> list, Context context)
            {
                this.list = list;
                this.context = context;
            }
    
        @Override public int getCount()
            {
                return list.size();
            }
        
        @Override public Object getItem(int pos)
            {
                return list.get(pos);
            }
        
        @Override public long getItemId(int pos)
            {
                return 0;//list.get(pos).getId();
            }
        
        @Override public View getView(int pos, View view, ViewGroup viewGroup)
            {
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.listview_element,null);
                }
                TextView txtName = (TextView) view.findViewById(R.id.txtName);
                txtName.setText(list.get(pos));
                Button btnEdit = (Button)view.findViewById(R.id.btnEdit);
                Button btnDelete = (Button)view.findViewById(R.id.btnDelete);
                
                btnEdit.setOnClickListener(view1 ->
                {
                    //TODO : call edit
                });
                btnDelete.setOnClickListener(view12 ->
                {
                    //TODO : call delete
                });
                return view;
            }
    }
