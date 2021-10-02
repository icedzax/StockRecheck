package com.example.stockrecheck;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by jannarong.j on 12/21/2018.
 */

public class Line {

    private static String LOG_TAG = "LINE";

    public void PushMessage( String hn, String mat, String loc){

       String url ="https://hook.zubbsteel.com/line-ci/index.php/bin/upbin?hn="+hn+"&mat="+mat+"&loc="+loc+" ";
       url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Get","");
    }

    public void flagDel( String hn, String mat){

        String url ="https://hook.zubbsteel.com/line-ci/index.php/bin/del?hn="+hn+"&mat="+mat+" ";
        url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Get","");
    }

    public void postBin( String body){

        String url ="https://hook.zubbsteel.com/line-ci/binapi/dev";
        url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Post",body);
    }

    public void postDelBin( String body){

        String url ="https://hook.zubbsteel.com/line-ci/binapi/dev";
        url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Post",body);
    }

    InputStream ByGetMethod(String ServerURL) {
        InputStream DataInputStream = null;
        try {

            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            cc.setReadTimeout(5000);
            cc.setConnectTimeout(5000);
            cc.setRequestMethod("GET");
            cc.setDoInput(true);
            cc.connect();


            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    InputStream ByPostMethod(String ServerURL,String Param_p) {

        InputStream DataInputStream = null;
        try {

            String PostParam = Param_p;
            //
            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            cc.setReadTimeout(8000);
            cc.setConnectTimeout(8000);
            cc.setRequestMethod("POST");
            cc.setDoInput(true);
            cc.connect();

            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            dos.flush();
            dos.close();

            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in PostData", e);
        }
        return DataInputStream;

    }

    String ConvertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
            }
        }
        return response.toString();


    }



    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg) {

            InputStream is = null;
            String URL = arg[0];
            String param = arg[2];
          //  Log.d(LOG_TAG, "URL: " + URL);
            String res = "";


            if (arg[1].equals("Post")) {

                is = ByPostMethod(URL,param);

            } else {

                is = ByGetMethod(URL);
            }
            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

          //  Log.d(LOG_TAG, "Result: " + result);
        }
    }



}
