package com.example.stockrecheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
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

    public static String getCurMatGroup() {
        return curMatGroup;
    }

    public static void setCurMatGroup(String curMatGroup) {
        MainActivity.curMatGroup = curMatGroup;
    }

    public static String curMatGroup = "ALL";

    public static Boolean getIsInit() {
        return isInit;
    }

    public static void setIsInit(Boolean isInit) {
        MainActivity.isInit = isInit;
    }

    public static Boolean isInit = false;



    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        MainActivity.pos = pos;
    }

    public static int pos = 0 ;

    public static String getCurSec() {
        return curSec;
    }

    public static void setCurSec(String curSec) {
        MainActivity.curSec = curSec;
    }

    public static String getCurBin() {
        return curBin;
    }

    public static void setCurBin(String curBin) {
        MainActivity.curBin = curBin;
    }

    private   static  String curSec ="";
    private  static  String curBin ="";

    ProgressBar pbbar;
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    ListView lstdo;
    EditText hideEdt;
    TextView tv_section,tv_bin,tv_re;
    LocationHelper lch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_count);

        hideEdt = findViewById(R.id.hedt3);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
//        btndiff = findViewById(R.id.btnwv);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        lch = new LocationHelper(MainActivity.this);
        connectionClass = new ConnectionClass();
        lstdo = (ListView) findViewById(R.id.lstdo);

        tv_section = (TextView)findViewById(R.id.tv_sec);
        tv_bin = (TextView)findViewById(R.id.tv_bin);
        tv_re = (TextView)findViewById(R.id.tv_re);


/*smoothScrollToPosition(22);
        btndiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Webview.class);
                startActivity(i);
            }
        });*/

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

        tv_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionPicker();

            }
        });

        tv_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binPicker();

            }
        });

        tv_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FillList fillList = new FillList();
                fillList.execute(getCurSec(),getCurBin());*/
//                lstdo.smoothScrollToPositionFromTop(getPos(),0,0);
                matGroupPicker();
            }
        });



    }

    public void DetailClick(View v) {
        ListView lv = lstdo ;
        int position = lv.getPositionForView(v);
//        Toast.makeText(MainActivity.this, "pos : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            if(keycode == 120 || keycode == 520){

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

    public void sectionPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
        String head ="";
        String[] set = {""};
        String[] sec_set = lch.section_d.toArray(new String[lch.section_d.size()]);
        final String[] rmd_sections = lch.sections.toArray(new String[lch.sections.size()]);

        head ="ช่อง"; set = sec_set;
        builder.setTitle(head);
        final String[] finalSet = set;

        builder.setItems(set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String wsec = "";

                    wsec = rmd_sections[which];
                    setCurSec(wsec);
                    tv_section.setText(getCurSec());

                FillList fillList = new FillList();
                setTvMat("ALL");
                fillList.execute(getCurSec(),getCurBin(),getCurMatGroup());
                setPos(0);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void matGroupPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));

        final String[] set = arrMatGroup(getCurSec(),getCurBin());

        builder.setTitle("กลุ่มสินค้า");
        final String[] finalSet = set;

        builder.setItems(set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

             /*   setCurMatGroup(set[which]);

                tv_re.setText(set[which]);*/

                setTvMat(set[which]);
                FillList fillList = new FillList();
                fillList.execute(getCurSec(),getCurBin(),getCurMatGroup());

                setPos(0);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public  void setTvMat(String param){
        if(param== null){
            param ="";
        }
        setCurMatGroup(param);
        tv_re.setText(getCurMatGroup());

    }

    private String isNull(String str){
        if(str==null){
            return "";
        }else{

            return str ;
        }

    }

    public void binPicker(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pil = isNull(edtPil.getText().toString());

                setCurBin(pil);
                tv_bin.setText(getCurBin());
                setTvMat("ALL");
                FillList fillList = new FillList();
                fillList.execute(getCurSec(),getCurBin(),getCurMatGroup());
                setPos(0);
                dialog.dismiss();
            }
        });

        dialog.show();
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
        String temp[]  = new String[2];



        List<Map<String, String>> dolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
//            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            final SimpleAdapter ADA ;

            temp[0] = "";

                String[] from = {"LOC","QTY","MatDesc","confirmBy"};
                int[] views = {R.id.LOC,R.id.qty_count,R.id.mat,R.id.qty_piece};
                  ADA = new SimpleAdapter(MainActivity.this,
                        dolist, R.layout.adp_list_stock_count, from,
                        views)/*{
                  LinearLayout lsec;
                  TextView section;

                      @Override
                      public View getView(final int position, View convertView, ViewGroup parent) {


                          View view = super.getView(position, convertView, parent);
//


                          return view;
                      }



              *//*        @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                          if(isInit){
                              Log.d("POS", String.valueOf(position));
                              setPos(position);
                              Log.d("LAST POST", String.valueOf(getPos()));
                          }

                          View view = super.getView(position, convertView, parent);
//                          Toast.makeText(MainActivity.this, "pos : "+position, Toast.LENGTH_SHORT).show();

*//**//*
                          if (convertView == null) {

                              lsec = view.findViewById(R.id.lsection);
                              section = view.findViewById(R.id.section);


                              temp[1] = dolist.get(position).get("LOC");

                              if (!temp[0].equals(temp[1])) {
                                  temp[0] = temp[1];
                                  lsec.setVisibility(View.VISIBLE);
                                  section.setText(temp[0]);
                              } else {
                                  lsec.setVisibility(View.GONE);
                              }
                    //*Log.d("Section-1",temp[0]);
//                    Log.d("Section-2",temp[1]);



                          }*//**//*
                          return view;
                      }*//*

                }*/;
//


            lstdo.setAdapter(ADA);
//            lstdo.smoothScrollToPosition(10);
            lstdo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String pickBarCode = (String) obj.get("BarCode");
//                    Toast.makeText(MainActivity.this, pickBarCode, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, Stock.class);
                    i.putExtra("bar", pickBarCode);
                    setPos(arg2);


                    startActivity(i);
//                    Toast.makeText(MainActivity.this, "pos : "+arg2, Toast.LENGTH_SHORT).show();
                }

            });

            lstdo.setSelection(getPos());


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String ibin = " and bin = '"+params[1]+"' " ;

                    String imat = " and replace(replace([MatGroup],'เหล็ก',''),'เส้น','') = '"+params[2]+"' " ;

                    if(params[1]==null || params[1].equals("") || params[1].equals("null")){
                        ibin = "";
                    }
                    if(params[2]==null || params[2].equals("") || params[2].equals("null") || params[2].equals("ALL")){
                        imat = "";
                    }
                    String plant = "";
                    switch (usrHelper.getPlant()){
                        case  "ZUBB" : plant = " plant in ('P8','P1') ";
                            break;
                        case  "SPN" : plant = " plant = 'WPN' ";
                            break;
                        case  "OPS" : plant = " plant = 'WPN' ";
                            break;
                    }

                    String query = "select * from STOCK.dbo.vw_list_bin where "+plant+" and  location = '"+params[0]+"' "+ibin+" "+imat+" order by location,bin";

//                    String query = "select * from STOCK.dbo.vw_list_bin where location = '1R' order by location,bin";
                    Log.d("query",query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    dolist.clear();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("LOC", rs.getString("Location")+"-"+rs.getString("bin"));
                        datanum.put("Bin", rs.getString("Bin"));
                        datanum.put("MatDesc", rs.getString("MatDesc"));
                        datanum.put("QTY", rs.getString("QTY"));
                        datanum.put("BarCode", rs.getString("BarCode"));
                        datanum.put("confirmBy", rs.getString("confirmBy"));


                        dolist.add(datanum);
                    }

                    z = "Success";

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

    public String[] arrMatGroup(String loc, String bin) {

            String result[] = new String[60];
            try {

                Connection con =  connectionClass.CONN();

                if (con == null) {

                } else {
                    String sbin = " and bin = '"+isNull(bin)+"' ";
                    if(bin==null || bin.equals("") || bin.equals("null") ){
                        sbin = "";
                    }
                    int size = 0;

                    String plant = "";
                    switch (usrHelper.getPlant()){
                        case  "ZUBB" : plant = " plant in ('P8','P1') ";
                            break;
                        case  "SPN" : plant = " plant = 'WPN' ";
                            break;
                    }


                    String cq = "SELECT COUNT(DISTINCT(replace(replace([MatGroup],'เหล็ก',''),'เส้น',''))) as x\n" +
                            "                    FROM [STOCK].[dbo].[tbl_physicalcount_location]\n" +
                            "                    where "+plant+" and [MatGroup] is not null and location = '"+isNull(loc)+"' "+sbin+" ";
                    PreparedStatement cts = con.prepareStatement(cq);
                    ResultSet co = cts.executeQuery();

                    while (co.next()){
                        size = co.getInt("x");
                    }
                    result =  new String[size+1];
                    result[0] = "ALL";
                    String squery = "SELECT replace(replace([MatGroup],'เหล็ก',''),'เส้น','') as rr\n" +
                            "  FROM [STOCK].[dbo].[tbl_physicalcount_location]\n" +
                            "   where "+plant+" and [MatGroup] is not null and location ='"+isNull(loc)+"'  "+sbin+" \n" +
                            "  group by   replace(replace([MatGroup],'เหล็ก',''),'เส้น','')" ;
                    PreparedStatement sts = con.prepareStatement(squery);
                    ResultSet sbs = sts.executeQuery();
                    int i = 1 ;
                    while (sbs.next()) {

                        result[i] = sbs.getString("rr");
                        i++;


                    }

                }
            } catch (Exception ex) {

            }
        return result;

    }

    @Override
    protected void onResume() {
//        Toast.makeText(MainActivity.this, "onresume", Toast.LENGTH_SHORT).show();
        FillList fillList = new FillList();
        fillList.execute(getCurSec(),getCurBin(),getCurMatGroup());
        //lstdo.smoothScrollToPositionFromTop(0);
//        lstdo.smoothScrollToPosition(7);
        setIsInit(true);



        super.onResume();
        lstdo.smoothScrollToPositionFromTop(13,0,0);

    }


}
