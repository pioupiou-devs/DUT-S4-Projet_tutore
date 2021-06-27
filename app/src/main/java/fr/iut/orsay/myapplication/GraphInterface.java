package fr.iut.orsay.myapplication;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface GraphInterface
    {
        /**
         * pour ajouter une courbe a un graphique
         *
         * @param label le nom de la courbe
         * @param dataSet les donnees de la courbe
         */
        void addDataSet(String label, ArrayList<Entry> dataSet);
    
        /**
         * supprimer une courbe
         *
         * @param label le nom de la courbe a supprimer
         */
        void removeDataSet(String label);
    
        /**
         * afficher le graphique
         */
        void show();
    
        /**
         * afficher une unique courbe
         *
         * @param label le nom de la courbe a afficher
         */
        void showDataSet(String label);
        
        /**
         * zoomer dans le graph
         */
        void zoomIn();
    
        /**
         * zoomer dans le graph avec une echele
         *
         * @param scale l'echele de zoom voulu
         */
        void zoomIn(int scale);
    
        /**
         * zoomer dans le graph avec un zoom non egale en hauteru et largeur
         *
         * @param scaleW l echele en largeur
         * @param scaleH l echele en hauteru
         */
        void zoomIn(int scaleW, int scaleH);
    
        /**
         * dezoomer dans le graph
         */
        void zoomOut();
    
        /**
         * dezoomer dans le graph avec une echele
         *
         * @param scale l'echele de zoom voulu
         */
        void zoomOut(int scale);
    
        /**
         * dezoomer dans le graph avec un zoom non egale en hauteru et largeur
         *
         * @param scaleW l echele en largeur
         * @param scaleH l echele en hauteur
         */
        void zoomOut(int scaleW, int scaleH);
    
        /**
         * renomer le grahhique
         * @param newLabel le nouveau nom du graphique
         */
        void rename(String newLabel);
    
        /**
         * transformer le graphique en un String
         *
         * @return le graphique sous format de String
         */
        String print();
    
        /**
         * transformer une courbe en string
         *
         * @param label le nom de la courbe a transformer
         * @return la courbe en string
         */
        String printDataSet(String label);
    }
