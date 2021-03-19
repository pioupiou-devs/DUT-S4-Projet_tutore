package fr.iut.orsay.myapplication;

import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTools {

    public static Connection openConnection(String url) throws SQLException {
        Connection co = null;
        //url = "jdbc:mariadb://localhost/PT?useUnicode=yes&characterEncoding=UTF-8";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            co = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("il manque le driver oracle");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("impossible de se connecter a l'url : " + url);
            System.exit(1);
        }
        System.out.println("coucou ! " + co.getMetaData());
        return co;
    }

    //"jdbc:mariadb://localhost/PT?useUnicode=yes&characterEncoding=UTF-8";

    public static void getValues(Context context, int numSensor){
        //executeQuery(context.getResources().getString(R.string.get_values) + numSensor);

        //graph -> arraylist avec temps/date et valeur
    }
}
