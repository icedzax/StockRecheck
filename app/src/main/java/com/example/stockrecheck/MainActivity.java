package com.example.stockrecheck;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressBar pbbar;
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    ListView lstdo;
    EditText hideEdt;
    Button btndiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideEdt = findViewById(R.id.hedt);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        btndiff = findViewById(R.id.btnwv);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        lstdo = (ListView) findViewById(R.id.lstdo);

        FillList fillList = new FillList();
        fillList.execute();

        btndiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Webview.class);

                startActivity(i);
            }
        });

        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(hideEdt.getText().toString().trim().contains("DEMO")){
                        demo = hideEdt.getText().toString().trim().replace("DEMO","").replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();

                    }else{
                        demo = hideEdt.getText().toString().trim().replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").trim();
                    }
                    insertSCAN(demo);
                }

                return false;
            }

        });


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            if(keycode == 120){

                hideEdt.requestFocus();

            }

        }
        return super.dispatchKeyEvent(KEvent);
    }
    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){

           /* SendParam spr = new SendParam();
            spr.execute(rsscan.trim());*/

            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";

        List<Map<String, String>> dolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            String[] from = {"loc","loc_desc"};
            int[] views = {R.id.SEQ,R.id.Location};
            final SimpleAdapter ADA = new SimpleAdapter(MainActivity.this,
                    dolist, R.layout.adp_list_ar, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);


                    LinearLayout lnloc = view.findViewById(R.id.lnloc);
                    if(dolist.get(position).get("loc").substring(0,2).equals("2F") || dolist.get(position).get("loc").substring(0,2).equals("2R") || dolist.get(position).get("loc").equals("E7")){
                        lnloc.setBackgroundColor(Color.parseColor("#FFD100"));
                    }else{
                        lnloc.setBackgroundColor(Color.WHITE);
                    }

                    return view;
                }
            };

            lstdo.setAdapter(ADA);
            lstdo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String pick_loc = (String) obj.get("loc");
                    String pick_locdesc = (String) obj.get("loc_desc");
                    //String pick_loc3= (String) obj.get("loc3");

                    Intent i = new Intent(MainActivity.this, LineData.class);
                    i.putExtra("loc", pick_loc);
                    i.putExtra("loc_desc", pick_locdesc);
                    //i.putExtra("loc3", pick_loc3);

                    startActivity(i);

                }
            });


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {



                    String query = "select * from tbl_location";

                    Log.d("query",query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    dolist.clear();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("loc", rs.getString("loc"));
                        datanum.put("loc_desc", rs.getString("loc_desc"));
                        //datanum.put("loc3", rs.getString("location"));


                        dolist.add(datanum);
                    }

                    z = "Success";
                    //z = query;
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

            }
            return z;
        }
    }

    public class SendParam extends AsyncTask<String, String, String> {

        String z = "";
        String bar = "";
        Boolean ischk = false;



        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            if(ischk==true){

                Intent i = new Intent(MainActivity.this, Stock.class);
                i.putExtra("bar", bar.trim());
                startActivity(i);
                //finish();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {



                    String fetch = "select barcode from tbl_physicalcount_location where barcode = '"+params[0]+"' ";
                    PreparedStatement ps = con.prepareStatement(fetch);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        bar = rs.getString("barcode");
                    }
                    if (bar == null || bar.equals("")){
                        z = "ไม่พบข้อมูล";
                    }else{
                        ischk = true;
                        z = "สำเร็จ";
                    }




                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";
                ischk = false;
            }
            return z;
        }
    }

}
