package com.example.stockrecheck;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jannarong.j on 5/1/2560.
 */
public class ConnectionClassDev {


    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ConnectionClassDev.ip = ip;
    }

    public static String ip;

    public static String getPassword() {
        return password;
    }

    static String password = "itsteel1983" ;



    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "scale_mmt";
    String un = "sa";



    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;


        // Log.d("ip",getIp()+"\n"+getPassword());

        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://199.0.0.100;"
                    + "databaseName=" + db + ";user=" + un + ";password=itsteel1983;";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }


}