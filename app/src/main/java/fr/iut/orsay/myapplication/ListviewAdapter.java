package fr.iut.orsay.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListviewAdapter extends BaseAdapter implements ListAdapter
    {
        private final Context context;
        private ArrayList<Graph> list;
        private Graph selectedGraph;
        
        public ListviewAdapter(ArrayList<Graph> list, Context context)
            {
                this.list = list;
                this.context = context;
            }
        
        @Override public int getCount()
            {
                return list.size();
            }
        
        @Override public Graph getItem(int pos)
            {
                return list.get(pos);
            }
        
        @Override public long getItemId(int pos)
            {
                return list.get(pos).getId();
            }
        
        @Override public View getView(int pos, View element, ViewGroup parent)
            {
                if (element == null)
                    {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        element = inflater.inflate(R.layout.listview_element, parent, false);
                    }
                
                TextView txtName = (TextView) element.findViewById(R.id.txtName);
                txtName.setText(list.get(pos).getName());
                
                Button btnEdit = (Button) element.findViewById(R.id.btnEdit);
                btnEdit.setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.modalTextBoxTitle);
                    
                    final EditText input = (EditText) new EditText(builder.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                    {
                        dialog.dismiss();
                        this.list.get(pos).setName(input.getText().toString());
                        notifyDataSetChanged();
                    });
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                    
                    builder.show();
                });
                
                Button btnDelete = (Button) element.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(view ->
                {
                    if (this.list.get(pos) != null)
                        this.list.remove(pos);
                    notifyDataSetChanged();
                });
                
                ImageButton ibtnSelect = (ImageButton) element.findViewById(R.id.ibtnSelect);
                ibtnSelect.setOnClickListener(view ->
                {
                    this.selectedGraph = this.list.get(pos);
                    ((SelectionActivity) context).setToolbarTitle(this.list.get(pos).getName());
                });
                
                return element;
            }
        
        public Graph getSelectedGraph()
            {
                return selectedGraph;
            }
        
        public void addGraph(Graph... graphs)
            {
                this.list.addAll(Arrays.asList(graphs));
                notifyDataSetChanged();
            }
    }
