package fr.iut.orsay.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La classe qui gère les export de données depuis un graphique
 *
 * @author Antonin FOUQUES
 */
public class ExportGraph
    {
        /**
         * Le constructeur de la classe, ici inutile car les methodes de la classe fonctionnent en static
         */
        public ExportGraph()
            {
            }
        
        /**
         * La méthode qui permet d'exporter un graphique au format PNG
         *
         * @param chart    le graphique à exporter
         * @param fileName nom du fichier exporté
         * @return chemin où à été exporté le fichier PNG
         */
        public static String exportToPNG(Chart chart, String fileName) throws IOException
            {
                if (chart.saveToGallery(fileName))//Si l'export png fail
                    throw new IOException("Fail to export !"); //signale le message d'erreur
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            }
        
        /**
         * La méthode qui permet d'exporter un graphique au format PDF
         *
         * @param chart    le graphique à exporter
         * @param fileName nom du fichier exporté
         * @return chemin où à été exporté le fichier PDF
         * @throws IOException retourne une erreur si l'accès à l'image généré dans la création du pdf à posé problème
         */
        public static String exportToPDF(Chart chart, String fileName) throws IOException
            {
                exportToPNG(chart, fileName); //Créer l'image à inséré dans le fichier PDF
                
                String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(); //Récupère le chemin vers le dossier de stockage
                String filePath = baseDir + File.separator + fileName; //Création du chemin total
                Bitmap bitmap = BitmapFactory.decodeFile(filePath + ".png"); //Transformation du fichier png en bitmap
                new File(filePath + ".png").delete();//suppression du fichier png inutile
                PdfDocument pdfDocument = new PdfDocument(); //Création de l'outil de création de fichier pdf
                PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create()); //Création d'une page du document
                Canvas canvas = page.getCanvas(); //Création d'un canvas (l'outil de dessin) à partir de la paged du document
                
                canvas.drawBitmap(bitmap, 0f, 0f, null); //Déssin du bitmap sur le canvas
                
                pdfDocument.finishPage(page); //Intégration de la page dans le document
                pdfDocument.writeTo(new FileOutputStream(filePath + ".pdf")); //Exportation du document
                pdfDocument.close(); //Fermeture de l'outil de création de fichier pdf
                
                return filePath + ".pdf";
            }
        
        /**
         * La méthode qui permet d'exporter les données d'un graphique au format csv
         *
         * @param chart    le graphique à exporter
         * @param fileName nom du fichier exporté
         * @return chemin où à été exporté le fichier CSV
         * @throws IOException retourne une erreur si la création du fichier à posé problème
         */
        public static String exportToCSV(Chart chart, String fileName) throws Exception
            {
                
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath(); //Récupère le chemin vers le dossier de stockage
                String filePath = baseDir + File.separator + fileName; //Création du chemin total
                File file = new File(filePath + ".csv"); //Création du fichier
                CSVWriter writer; //Création de l'outil d'écriture du fichier CSV
                
                if (file.exists() && !file.isDirectory()) //Vérification que le fichier n'éxiste pas ou qu'il ne soit pas un dossier
                    {
                        FileWriter mFileWriter = new FileWriter(filePath + ".csv", false); //Création de l'outil d'écriture du fichier CSV (ici en écrasant le contenu)
                        writer = new CSVWriter(mFileWriter);
                    }
                else
                    writer = new CSVWriter(new FileWriter(filePath + ".csv"));
                
                
                ChartData data = chart.getData(); //Récupération des données du graphique
                
                if (data.getDataSetCount() <= 0) //Vérification qu'il y ait des données dans le graphique
                    throw new Exception("No dataset to export !");
                
                ArrayList<String[]> datasets = new ArrayList<>();
                for (int i = 0; i < data.getDataSetCount(); i++) //pour chaque datasets
                    {
                        IDataSet dataset = data.getDataSetByIndex(i);
                        
                        if (dataset.getEntryCount() <= 0) //passer au dataset suivant s'il est vide
                            continue;
                        
                        String[] xString = new String[dataset.getEntryCount() + 1]; //Création du tableau représentant l'axe du temps
                        String[] yString = new String[dataset.getEntryCount() + 1]; //Création du tableau représentant l'axe des données
                        xString[0] = dataset.getLabel() + ".temps"; //Création du label de colonne du temps
                        yString[0] = dataset.getLabel() + ".donnée"; //Création du label de colonne des données
                        
                        for (int j = 0; j < dataset.getEntryCount(); j++) //pour chaque données
                            {
                                String temp = String.valueOf(dataset.getEntryForIndex(j)); //Récupération des données en string
                                Matcher matcher = Pattern.compile("[0-9]*\\.?[0-9]+").matcher(temp); //Création d'une regex sur la string de données
                                //Récupération de l'axe du temps
                                if (matcher.find())
                                    xString[j + 1] = matcher.group();
                                //Récupération de l'axe des données
                                if (matcher.find())
                                    yString[j + 1] = matcher.group();
                            }
                        
                        datasets.add(xString); //ajout du temps au tableau
                        datasets.add(yString); //ajout des données au tableau
                    }
                
                int longest = 0;
                for (String[] s : datasets) //Calcul du tableau le plus long
                    if (longest < s.length)
                        longest = s.length;
                
                for (int i = 0; i < longest; i++) //Boucle jusqu'à la taille de tableau le plus long
                    {
                        String[] temp = new String[datasets.size()];
                        int index = 0;
                        for (String[] strings : datasets) //pour chaque dataset
                            if (strings[i] != null) //s'il l'emplacement n'est pas vide
                                {
                                    temp[index] = strings[i]; //ajout de la donnée au tableau temporaire
                                    index++;
                                }
                        writer.writeNext(temp); //ajout du contenu du tableau temporaire au document CSV
                    }
                
                writer.close(); //Fermeture de l'outil d'écriture du fichier CSV
                
                return filePath + ".csv";
            }
    }