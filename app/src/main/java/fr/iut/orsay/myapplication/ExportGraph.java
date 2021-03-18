package fr.iut.orsay.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La classe qui gère les export de données depuis un graphique
 * @author Antonin FOUQUES
 */
public class ExportGraph
    {
        /**
         * Le constructeur de la classe, ici inutile car les methodes fonctionnent en static
         */
        public ExportGraph()
            {
            }
        
        /**
         * La méthode qui permet d'exporter un graphique au format PNG
         * @param chart le graphique à exporter
         * @param fileName nom du fichier exporté
         */
        public static void exportToPNG(Chart chart, String fileName)
            {
                if (!chart.saveToGallery(fileName))
                    System.err.println("Fail to export !");
            }
    
        /**
         * La méthode qui permet d'exporter un graphique au format PDF
         * @param chart le graphique à exporter
         * @param fileName nom du fichier exporté
         * @throws IOException retourne une erreur si l'accès à l'image généré dans la création du pdf à posé problème
         */
        public static void exportToPDF(Chart chart, String fileName) throws IOException
            {
                exportToPNG(chart, fileName);
                
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String filePath = baseDir + File.separator + "DCIM" + File.separator + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath + ".png");
                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create());
                Canvas canvas = page.getCanvas();
                
                canvas.drawBitmap(bitmap, 0f, 0f, null);
                
                pdfDocument.finishPage(page);
                pdfDocument.writeTo(new FileOutputStream(filePath + ".pdf"));
                pdfDocument.close();
            }
    
        /**
         * La méthode qui permet d'exporter les données d'un graphique au format csv
         * @param chart le graphique à exporter
         * @param fileName nom du fichier exporté
         * @throws IOException retourne une erreur si la création du fichier à posé problème
         */
        public static void exportToCSV(Chart chart, String fileName) throws IOException
            {
                
                
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String filePath = baseDir + File.separator + fileName;
                File file = new File(filePath);
                CSVWriter writer;
                
                if (file.exists() && !file.isDirectory())
                    {
                        FileWriter mFileWriter = new FileWriter(filePath, false);
                        writer = new CSVWriter(mFileWriter);
                    }
                else
                    writer = new CSVWriter(new FileWriter(filePath));
                
                
                ChartData data = chart.getData();
                
                if (data.getDataSetCount() <= 0)
                    {
                        System.err.println("No dataset to export !");
                        return;
                    }
                
                for (int i = 0; i < data.getDataSetCount(); i++)
                    {
                        IDataSet dataset = data.getDataSetByIndex(i);
                        
                        if (dataset.getEntryCount() <= 0)
                            continue;
                        
                        String[] xString = new String[dataset.getEntryCount() + 1];
                        String[] yString = new String[dataset.getEntryCount() + 1];
                        xString[0] = dataset.getLabel() + ".x";
                        yString[0] = dataset.getLabel() + ".y";
                        
                        for (int j = 0; j < dataset.getEntryCount(); j++)
                            {
                                String temp = String.valueOf(dataset.getEntryForIndex(j));
                                Matcher matcher = Pattern.compile("[0-9]*\\.?[0-9]+").matcher(temp);
                                //x
                                if (matcher.find())
                                    xString[j + 1] = matcher.group();
                                System.out.println(xString[j + 1]);
                                //y
                                if (matcher.find())
                                    yString[j + 1] = matcher.group();
                                System.out.println(yString[j + 1]);
                            }
                        
                        writer.writeNext(xString);
                        writer.writeNext(yString);
                    }
                
                writer.close();
            }
    }