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

import fr.iut.orsay.myapplication.activity.SelectionActivity;

public class ListviewAdapter extends BaseAdapter implements ListAdapter
    {
        private final Context context;
        private ArrayList<Graph> list;
        private Graph selectedGraph;
        
        /**
         * Constructeur du listview personnalisé
         *
         * @param list    contient la liste des graphiques à ajouté à l'initialisation
         * @param context contient le lien vers la page où est situé le listview
         */
        public ListviewAdapter(ArrayList<Graph> list, Context context)
            {
                this.list = list;
                this.context = context;
            }
        
        /**
         * Récupère le nombre d'élément dans le listview
         *
         * @return le nombre d'élément dans le listview
         */
        @Override public int getCount()
            {
                return list.size();
            }
        
        /**
         * Récupère un élément en fonction de sa position
         *
         * @param pos représente la position d'un élément
         * @return un élément du listview
         */
        @Override public Graph getItem(int pos)
            {
                return list.get(pos);
            }
        
        /**
         * Recupère l'identifiant d'un élément
         *
         * @param pos représente la position d'un élément
         * @return l'identifiant de l'élément
         */
        @Override public long getItemId(int pos)
            {
                return list.get(pos).getId();
            }
        
        /**
         * Construit la répresentation graphique et les intéractions avec l'utilisateur des éléments du listview
         *
         * @param pos     représente la position d'un élément
         * @param element représente un élément
         * @param parent  représente le parent d'un élément
         * @return Un object view représentant l'élément dans la vue de l'application
         */
        @Override public View getView(int pos, View element, ViewGroup parent)
            {
                if (element == null) // s'il n'y a pas d'élément
                    { //alors on cherche grâce à un layoutInflater un élément
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        element = inflater.inflate(R.layout.listview_element, parent, false);
                    }
                
                TextView txtName = element.findViewById(R.id.txtName); //création et lien du textview
                txtName.setText(list.get(pos).getName()); //assignation du texte du listeview, ici le nom du graphique
                
                ImageButton ibtnSelect = element.findViewById(R.id.ibtnSelect); //création et lien du bouton de séléction
                //ajout d'un listeneur de click
                ibtnSelect.setOnClickListener(view ->
                {
                    this.selectedGraph = this.list.get(pos); //on récupère l'élément séléctionner
                    ((SelectionActivity) context).setToolbarTitle(this.list.get(pos).getName()); //on met à jour le bandeau d'application
                });
                
                Button btnEdit = element.findViewById(R.id.btnEdit); //création et lien du bouton de renommage
                //ajout d'un listeneur de click
                btnEdit.setOnClickListener(view ->
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context); //création d'une popup
                    builder.setTitle(R.string.modalTextBoxTitle); //nom de la popup
                    
                    final EditText input = new EditText(builder.getContext()); //création d'un élément de text éditable
                    input.setInputType(InputType.TYPE_CLASS_TEXT); //spécification du type de données
                    builder.setView(input); //ajout de l'élément de text éditable
                    
                    //si on click sur "OK"
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                    {
                        dialog.dismiss(); // on ferme la popup
                        this.list.get(pos).setName(input.getText().toString()); //on met à jour le nom du graphique
                        notifyDataSetChanged(); //demande une mise à jour graphique
                    });
                    //si on click sur "Cancel"
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel()); //on ferme la popup sans faire de modifications
                    
                    builder.show(); //affiche la popup
                });
                
                Button btnDelete = element.findViewById(R.id.btnDelete); //création et lien du bouton de suppression
                //ajout d'un listeneur de click
                btnDelete.setOnClickListener(view ->
                {
                    if (this.list.get(pos) != null) // si l'élément existe
                        this.list.remove(pos); // on l'enlève du listview
                    notifyDataSetChanged(); //demande une mise à jour graphique
                });
                
                return element;
            }
        
        /**
         * Permet d'ajouter autant de graphiques que nécéssaire dans le listview
         *
         * @param graphs var-args des graphiques à insérer dans le listView
         */
        public void addGraph(Graph... graphs)
            {
                this.list.addAll(Arrays.asList(graphs)); //ajoute les graphiques au listview
                notifyDataSetChanged(); //demande une mise à jour graphique du listview
            }
    }
