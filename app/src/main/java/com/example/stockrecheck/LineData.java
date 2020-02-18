package com.example.stockrecheck;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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

public class LineData extends AppCompatActivity {

    ProgressBar pbbar;
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    ListView lstitem;
    String gloc,glocdesc,gloc3;
    TextView tvLocDesc;
    EditText hideEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_data);

        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = findViewById(R.id.hedt);
        pbbar.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        lstitem = (ListView) findViewById(R.id.lst_item);
        tvLocDesc = (TextView) findViewById(R.id.tvLocDesc);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {

            gloc = "";
            glocdesc = "";
            //gloc3 = "";


        } else {
            gloc = extras.getString("loc");
            glocdesc = extras.getString("loc_desc");
            //gloc3 = extras.getString("gloc3");
            tvLocDesc.setText(glocdesc);
        }


        FillList fillList = new FillList();
        fillList.execute(gloc);

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


            SendParam spr = new SendParam();
            spr.execute(rsscan.trim());

            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");
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
            Toast.makeText(LineData.this, r, Toast.LENGTH_SHORT).show();

            if(ischk==true){

                Intent i = new Intent(LineData.this, Stock.class);
                i.putExtra("bar", bar.trim());
                startActivity(i);
                finish();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String locstmt = "left(location,2)";

                    if(gloc.length()>=3){
                        locstmt =" Location ";
                    }
                    if(connectionClass.getIp().equals("116")){
                        locstmt = " location ";
                    }

                        String fetch = "select barcode from tbl_physicalcount_location where barcode = '"+params[0]+"' and "+locstmt+" = '"+gloc+"' ";

                    if(!params[0].contains("-")){

                         fetch = "select barcode from tbl_physicalcount_location where barcode = '"+params[0]+"' and "+locstmt+" = '"+gloc+"' ";
                    }


                        PreparedStatement ps = con.prepareStatement(fetch);
                        ResultSet rs = ps.executeQuery();

                        Log.d("fetch",fetch);
                        while (rs.next()) {
                            bar = rs.getString("barcode");
                        }
                        if (bar == null || bar.equals("")){
                            z = "ไม่พบข้อมูลก\nกรุณาตรวจสอบช่อง";
                        }else{
                            ischk = true;
                            //z = "สำเร็จ";
                            z = params[0];
                        }


                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";
                ischk = false;
            }
            return z;
        }
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
            Toast.makeText(LineData.this, r, Toast.LENGTH_SHORT).show();


                String[] from = {"idBin","MatDesc","QTY"};
                int[] views = {R.id.Bin,R.id.MatDesc,R.id.QTY};
                final SimpleAdapter ADA = new SimpleAdapter(LineData.this,
                        dolist, R.layout.adp_list_loc, from,
                        views){
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        //LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lay);
                        TextView qty = view.findViewById(R.id.QTY);


                        //int lx = Integer.parseInt(vbelnlist.get(position).get("sscan"));
                        if(dolist.get(position).get("confirmBy")!=null){
                            qty.setTextColor(Color.RED);
                        }else{
                            qty.setTextColor(Color.BLACK);
                        }


                        if(position %2 == 1) //TODO get id to value and assign color row
                        {
                            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        else
                        {
                            view.setBackgroundColor(Color.parseColor("#F4F4F4"));
                        }


                        return view;
                    }
                };
                lstitem.setAdapter(ADA);




            /*lstitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String pick_seq = (String) obj.get("SEQ");
                    String pick_loc = (String) obj.get("Location");

                    Intent i = new Intent(LineData.this, Stock.class);
                   // i.putExtra("SEQ", pick_seq);
                    i.putExtra("bar", "6RL-002");
                    i.putExtra("Location", pick_loc);


                    startActivity(i);

                }
            });*/


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String iloc =" left(Location,2) ";

                    if(params[0].length()>=3){
                         iloc =" Location ";
                    }

                    if(connectionClass.getIp().equals("116")){
                         iloc =" Location ";
                    }

                        String query = "select *,left(Material,3) as mg from tbl_physicalcount_location where "+iloc+" = '"+params[0]+"' " ;

                        Log.d("query",query);

                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        dolist.clear();

                        while (rs.next()) {
                            Map<String, String> datanum = new HashMap<String, String>();
                            datanum.put("SEQ", rs.getString("SEQ"));
                            datanum.put("Location", rs.getString("Location"));
                            datanum.put("idBin", rs.getString("SEQ")+"-"+rs.getString("Bin"));
                            datanum.put("MatDesc", rs.getString("mg")+""+rs.getString("Size"));
                            datanum.put("QTY", rs.getString("QTY"));
                            datanum.put("confirmBy", rs.getString("confirmBy"));

                            dolist.add(datanum);
                        }

                    z = gloc;
                    //z = "Success";
                    //z = query;
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

            }
            return z;
        }
    }

}
