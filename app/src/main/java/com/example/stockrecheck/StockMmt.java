package com.example.stockrecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockMmt extends AppCompatActivity {

    EditText hideEdt;
    ProgressBar pbbar ;
    ConnectionClass connectionClass;
    UserHelper urs;

    ListView lv_hn;
    List<Map<String, String>> hnlist  = new ArrayList<Map<String, String>>();
    TextView hn,tv_loc,tv_storage,tv_section,tv_bin,tv_sumb,tv_sumq,del,total,pole_no;
    LocationHelper locHelp;
    FillList fillList;
    LocationHelper lch;
    Line sLine;
    Lang lang;
    String scanresult,tab_id,tab_hn;
    String rmd_date,charge,bundle,qty,qa_grade,matcode,weight,rmd_grade,remark,dupName,dupBy,dupWhen,dupHn,dupMat;

    static String sstorage;
    static String ssection;
    static String sbin ;
    static String st_sec ;

    Boolean SectionMode = false;

    public static String getRs() {
        return rs;
    }

    public static void setRs(String rs) {
        StockMmt.rs = rs;
    }

    static String rs = "" ;

    int ernf;
    int erdup;

    public int getErMl() {
        return erMl;
    }

    public void setErMl(int erMl) {
        this.erMl = erMl;
    }

    int erMl;

    public int getErloc() {
        return erloc;
    }

    public void setErloc(int erloc) {
        this.erloc = erloc;
    }

    int erloc;
    int sumb = 0,sumq = 0;

    public int getErnf() {
        return ernf;
    }

    public void setErnf(int ernf) {
        this.ernf = ernf;
    }

    public int getErdup() {
        return erdup;
    }

    public void setErdup(int erdup) {
        this.erdup = erdup;
    }

    public boolean isExdup() {
        return exdup;
    }

    public void setExdup(boolean exdup) {
        this.exdup = exdup;
    }

    boolean exdup = false;

    public static String getSt_sec() {
        return st_sec;
    }

    public static void setSt_sec(String st_sec) {
        StockMmt.st_sec = st_sec;
    }

    public static String getSstorage() {
        return sstorage;
    }

    public static void setSstorage(String sstorage) {
        StockMmt.sstorage = sstorage;

    }

    public static String getSsection() {
        return ssection;
    }


    public static void setSsection(String ssection) {
        StockMmt.ssection = ssection;
    }
    public static String getSbin() {
        return sbin;
    }

    public static void setSbin(String sbin) {
        StockMmt.sbin = sbin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lang = new Lang();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_mmt);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        connectionClass = new ConnectionClass();
        urs = new UserHelper(StockMmt.this);
        lch = new LocationHelper(StockMmt.this);
        sLine = new Line();
        locHelp = new LocationHelper(this);

        LocationBin lb = new LocationBin();
        lb.execute(urs.getBranch());

//
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        connectionClass.setUdbn("PP");
        connectionClass.setUip("192.168.100.222");
        connectionClass.setUpass("");

//        if(ip!= null && ip.substring(8,10).equals("81")){
//            connectionClass.setUdbn("scale_mmt");
//            connectionClass.setUip("199.0.0.100");
//            connectionClass.setUpass("itsteel1983");
//        }

        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = (EditText)findViewById(R.id.hedt2);
        lv_hn = (ListView)findViewById(R.id.lvitem);
        tv_storage = (TextView)findViewById(R.id.tv_rmd);
        tv_storage.setText(lang.map.get("machine"));
        tv_section = (TextView)findViewById(R.id.tv_sec);
        tv_section.setText(lang.map.get("channal"));
        tv_bin = (TextView)findViewById(R.id.tv_bin);
        tv_bin.setText(lang.map.get("pole"));
        tv_sumb = (TextView)findViewById(R.id.tv_sumbun) ;
        tv_sumq = (TextView)findViewById(R.id.tv_sumqty) ;
        del = (TextView)findViewById(R.id.del) ;
        del.setText(lang.map.get("cancel"));
        tv_loc = (TextView)findViewById(R.id.tv_test);
        hn = (TextView)findViewById(R.id.hn);
        total = (TextView)findViewById(R.id.textView19);
        total.setText(lang.map.get("total"));

        pbbar.setVisibility(View.GONE);

        /*fillList = new FillList();
        fillList.execute("MR8","","");*/

        //Toast.makeText(StockMmt.this, getRs(), Toast.LENGTH_SHORT).show();

        tv_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(0);


            }
        });

        tv_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(1);

            }
        });

        tv_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPilClick();

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tab_id==null || tab_id.equals("")){
                    new AlertDialog.Builder(StockMmt.this)

                            .setTitle("ผิดพลาด")
                            .setMessage("เลือกรายการที่ต้องการยกเลิก")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StockMmt.this);
                    builder.setTitle("ยกเลิกรายการ");
                    builder.setMessage("ยืนยันการยกเลิก HN : "+tab_hn);
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AdjItem adj = new AdjItem();
                            adj.execute("D",tab_id);
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();

                        }
                    });
                    builder.show();



                }

            }
        });

        tv_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SectionMode==true){
                    SectionMode = false;
                    tv_bin.setVisibility(View.VISIBLE);
                    hn.setText("HN");
                    UpdateLoc(isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin()));
                    del.setVisibility(View.VISIBLE);
                    if(rs.length()>=9){

                            fillList = new FillList();
                            fillList.execute(getSstorage(),getSt_sec(),getSbin());


                    }


                }else{
                    SectionMode = true;
                    tv_bin.setVisibility(View.GONE);
                    hn.setText("Location");
                    UpdateLoc(isNull(getSstorage())+"-"+isNull(getSt_sec()));
                    del.setVisibility(View.GONE);
                    if(rs.length()>=6){
                        fillList = new FillList();
                        fillList.execute(getSstorage(),getSt_sec(),"");

                    }

                }
            }
        });

       /* tv_loc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                testInsert();
                return false;
            }
        });*/

        hideEdt.requestFocus();
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

            if(keycode == 120 || keycode == 520){
                //qrCam();
                hideEdt.requestFocus();

            }

        }
        return super.dispatchKeyEvent(KEvent);
    }

    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;
            AddProScan addP = new AddProScan();
            addP.execute(rsscan);


        }

        this.hideEdt.setText("");
    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            String[] from = {"charge","matcode","qty"};

            int[] views = {R.id.hn,R.id.matc,R.id.qty};
            final SimpleAdapter ADA = new SimpleAdapter(StockMmt.this,
                    hnlist, R.layout.adp_list_stock, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    //LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lay);
                    TextView qty = view.findViewById(R.id.QTY);

                    //int lx = Integer.parseInt(vbelnlist.get(position).get("sscan"));
                    /*if(hnlist.get(position).get("confirmBy")!=null){
                        qty.setTextColor(Color.RED);
                    }else{
                        qty.setTextColor(Color.BLACK);
                    }*/

                    if(position %2 == 1)
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
            lv_hn.setAdapter(ADA);

            if(SectionMode==true) {
                lv_hn.setEnabled(false);

            }
            else{
                lv_hn.setEnabled(true);
            }
                lv_hn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        tab_id = (String) obj.get("id");
                        tab_hn = (String) obj.get("charge");
                        /*tab_bd = (String) obj.get("charge");
                        tab_bd = (String) obj.get("charge");
                        tab_bd = (String) obj.get("charge");*/

                        arg1.setSelected(true);

                    }
                });

                lv_hn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);

                        String t_id = (String) obj.get("id");
                        String i_hn = (String) obj.get("charge");
                        String i_mat = (String) obj.get("matcode");
                        String i_location = (String) obj.get("location");
                        int qqty = Integer.parseInt((String) obj.get("qty"));
                        //double qweight = Integer.parseInt((String) obj.get("weight"));

                        arg1.setSelected(true);

                        itemChange(t_id,i_hn,i_mat,i_location,qqty);

                        return true;
                    }
                });



            tv_sumb.setText(""+sumb+" มัด");
            tv_sumq.setText(""+sumq+" เส้น");

            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                   /* String storage ;
                    String section ;
                    String bin ;

                    if(params[0]==null || params[0]==""){
                        storage ="";
                    } else{ storage = " where storage = '"+params[0]+"' "; }

                    if(params[1]==null || params[1]==""){
                        section ="";
                    } else{ section = " and section = '"+params[1]+"' "; }

                    if(params[2]==null || params[2]==""){
                        bin ="";
                    } else{ bin = " and bin = '"+params[2]+"' "; }*/

                    //String where = storage+section+bin+" and flag is null ";

                    String where = " where location = '"+getRs()+"' and flag is null ";

                   // Log.d("where" , where);

                    String sql = "select  *  from vw_stock_check  "+where+"   order by user_stamp desc ";
                    if(SectionMode == true){
                        where = " where location like '"+getRs()+"%' and flag is null ";
                        sql = "select  location,matcode,count(id)as b , sum(qty) as q  from vw_stock_check "+where+"  group by location,matcode ";
                    }
                    //Log.d("sql",sql);
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    int ids = 0 ;

                    hnlist.clear();
                    if(SectionMode == true){
                        while (rs.next()) {
                            //ids ++;
                            Map<String, String> datanum = new HashMap<String, String>();

                            datanum.put("charge", rs.getString("location"));
                            datanum.put("matcode", rs.getString("matcode"));
                            datanum.put("qty", rs.getString("b")+"/"+rs.getString("q"));
                            hnlist.add(datanum);

                        }
                    }else{
                        while (rs.next()) {
                            //ids ++;
                            Map<String, String> datanum = new HashMap<String, String>();
                            datanum.put("location", rs.getString("location"));
                            datanum.put("charge", rs.getString("charge")+"-"+rs.getString("bundle"));
                            datanum.put("qty", rs.getString("qty"));
                            datanum.put("weight", rs.getString("weight"));
                            datanum.put("qa_grade", rs.getString("qa_grade"));
                            datanum.put("matcode", rs.getString("matcode"));
                            datanum.put("id", rs.getString("id"));
                            hnlist.add(datanum);

                        }
                    }


                    String sqlsum = "SELECT count(id) as b,sum(qty) as q " +
                            "  FROM vw_stock_check " +
                            "   "+where+" ";
                    PreparedStatement ss = con.prepareStatement(sqlsum);
                    ResultSet srs = ss.executeQuery();
                    while (srs.next()) {
                        sumb = srs.getInt("b");
                        sumq = srs.getInt("q");
                    }

                    z = "Success";

                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;

        }
    }



    public void onSelect(final int t){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(StockMmt.this, R.style.AlertDialogCustom));
        String head ="";

        String[] sec_set = lch.MemberList.toArray(new String[lch.MemberList.size()]);

//        final String[] rmd_sections = lch.secs.toArray(new String[lch.secs.size()]);

        String[] rmd_set = lch.storages.toArray(new String[lch.storages.size()]);

        String[] set = {""};

        switch (t){
            case 0 : head = lang.map.get("save_location"); set = rmd_set;
                break;
            case 1 : head = lang.map.get("channal"); set = sec_set;
                break;
        }



        builder.setTitle(head);


        final String[] finalSet  = set;

        builder.setItems(set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                StockMmt stock = new StockMmt();
                String wsec = "";
                if(t ==1){

                    wsec = finalSet[which];

                    setSsection(finalSet[which]);
                    setSt_sec(wsec);
                    tv_section.setText((finalSet[which]));
                    UpdateSection(finalSet[which]);

                    if(SectionMode==false){
                        if(rs.length()>=9){
                            fillList = new FillList();
                            fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),isNull(getSbin()));
                        }
                    }else{
                        fillList = new FillList();
                        fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),"");
                    }



                }else{

                    setSstorage(finalSet[which]);
                    tv_storage.setText(finalSet[which]);
                    UpdateStorage((finalSet[which]));

                    if(SectionMode==false){
                        if(rs.length()>=9){
                            fillList = new FillList();
                            fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),isNull(getSbin()));
                        }
                    }else{
                        fillList = new FillList();
                        fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),"");



                    }

                }

                //getResult();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String isNull(String str){
        if(str==null){
            return "";
        }else{
            return str ;
        }

    }

    public  void  itemChange(final String id, String phn, String pmat, String location, int pqty){

        final Dialog dialog = new Dialog(StockMmt.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_manaul);
        dialog.setCancelable(true);

        final TextView hn,mat,dstr,dsec,dbin;
        EditText eqty;
        Button svbtn,cnbtn;

        svbtn =(Button)dialog.findViewById(R.id.mbtnSave) ;
        cnbtn =(Button)dialog.findViewById(R.id.mbtnCan) ;

        hn = (TextView)dialog.findViewById(R.id.d_hn);
        mat = (TextView)dialog.findViewById(R.id.d_mat);
        dstr = (TextView)dialog.findViewById(R.id.d_rmd);
        dsec = (TextView)dialog.findViewById(R.id.d_sec);
        dbin = (TextView)dialog.findViewById(R.id.d_bin);
        eqty = (EditText)dialog.findViewById(R.id.ed_qty);

        hn.setText(phn);
        mat.setText(pmat);

        String s = location.substring(0,3);
        String e = location.substring(4,6);
        String b = location.substring(7);

        dstr.setText(s);
        dsec.setText(e);
        dbin.setText(lch.fPill(b));

        eqty.setText(""+pqty);

        dstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onXSelect(0,dstr);
            }
        });

        dsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onXSelect(1,dsec);
            }
        });


        dbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(StockMmt.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_pil_picker);
                dialog.setCancelable(true);

                final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
                Button btnSv = (Button)dialog.findViewById(R.id.btnSv);
                btnSv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pil = edtPil.getText().toString();
                        dbin.setText(lch.fPill(pil));


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        svbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eqty;
                eqty = (EditText)dialog.findViewById(R.id.ed_qty);

                Boolean isfoundX = lch.StorageList.contains(dstr.getText().toString()+"-"+dsec.getText().toString()+"-"+dbin.getText().toString());
                if(isfoundX == true){

                    setErMl(0);
                    AdjItem adj = new AdjItem();
                    adj.execute(eqty.getText().toString(),dstr.getText().toString(),dsec.getText().toString(),dbin.getText().toString(),id);

                    dialog.dismiss();
                }else{
                    setErMl(1);
                        onErrorDialog(0,0,"z",0,getErMl(),dstr.getText().toString()+"-"+dsec.getText().toString()+"-"+dbin.getText().toString());

                }


            }
        });

        cnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void onXSelect(final int t, final TextView tv){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(StockMmt.this, R.style.AlertDialogCustom));
        String head ="";

        String[] sec_set = lch.section_d.toArray(new String[lch.section_d.size()]);
        String[] rmd_set = lch.storages.toArray(new String[lch.storages.size()]);
        final String[] rmd_sections = lch.sections.toArray(new String[lch.sections.size()]);

        String[] set = {""};

        switch (t){
            case 0 : head ="ตำแหน่งเก็บ"; set = rmd_set;
                break;
            case 1 : head ="ช่อง"; set = sec_set;
                break;
        }

        builder.setTitle(head);


        final String[] finalSet = set;
        builder.setItems(set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String wsec = "";
                if(t ==1){

                    wsec = rmd_sections[which];

                    tv.setText(wsec);

                }else{

                    tv.setText(finalSet[which]);
                }

                //getResult();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class AdjItem extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);

            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute(getSstorage(),getSt_sec(),getSbin());
            }else{
                Toast.makeText(StockMmt.this, z, Toast.LENGTH_SHORT).show();
            }
            tab_id = null;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    if(params[0].equals("D")){

                        String queryD = "update tbl_stock_location set flag = 'F' where id = "+params[1]+" ";

                        PreparedStatement preparedStatement = con.prepareStatement(queryD);
                        preparedStatement.executeUpdate();

                        String upC = "select matcode,charge,bundle,location from tbl_stock_location  " +
                                " where id = "+params[1]+" ";

                        PreparedStatement getD = con.prepareStatement(upC);
                        ResultSet ds = getD.executeQuery();



                        String d_charge= "",d_mat="",d_bundle="";
                        while (ds.next()) {
                            d_charge = ds.getString("charge");
                            d_bundle = ds.getString("bundle");
                            d_mat = ds.getString("matcode");
                           
                        }

                        String dels = "select top 1 location from tbl_stock_location  " +
                                " where charge = '"+d_charge+"' and bundle = '"+d_bundle+"' and matcode = '"+d_mat+"'" +
                                " and id <> "+params[1]+" order by user_stamp desc ";

                        PreparedStatement getdel = con.prepareStatement(dels);
                        ResultSet des = getdel.executeQuery();
                        String x_location="";

                        while (des.next()) {

                            x_location= des.getString("location");
                        }

                        String xbd = d_bundle;
                        if(d_bundle!=null){
                            if(Integer.parseInt(d_bundle) <= 9){
                                xbd = "0"+d_bundle;
                            }
                        }

                        if(x_location==null || x_location.equals("")){
                            sLine.PushMessage(getBatch(d_charge,xbd),d_mat,"");
                        }else{
                            sLine.PushMessage(getBatch(d_charge,xbd),d_mat,x_location);
                            
                            String updateFlag = "update tbl_stock_location set flag = NULL" +
                                    "  where id = (select top 1 id from tbl_stock_location where charge = '"+d_charge+"' and bundle = '"+d_bundle+"' and matcode = '"+d_mat+"'"  +
                                    "  and id <> "+params[1]+" and flag <> 'F'  order by user_stamp desc) ";

                            PreparedStatement prex = con.prepareStatement(updateFlag);
                            prex.executeUpdate();
                            
                        }

                        //z = "ลบสำเร็จ";
                        isSuccess = true;
                    }else{
                        String qty = "";
                        String plocation = params[1]+"-"+params[2]+"-"+params[3];
                        qty = params[0];
                        if(params[0]==null || params[0].equals("")){
                            qty = "0" ;
                        }

                        String query = "update tbl_stock_location set " +
                                "storage = '"+params[1]+"' , section = '"+params[2]+"' " +
                                ",bin = '"+params[3]+"',qty = "+qty+" , location = '"+plocation+"' where id = "+params[4]+" ";



                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                        String upeC = "select matcode,charge,bundle,location from tbl_stock_location  " +
                                " where id = "+params[4]+" ";

                        PreparedStatement geteD = con.prepareStatement(upeC);
                        ResultSet es = geteD.executeQuery();



                        String e_charge= "",e_mat="",e_bundle="";
                        while (es.next()) {
                            e_charge = es.getString("charge");
                            e_bundle = es.getString("bundle");
                            e_mat = es.getString("matcode");

                        }

                        String xbd = e_bundle;
                        if(e_bundle!=null){
                            if(Integer.parseInt(e_bundle) <= 9){
                                xbd = "0"+e_bundle;
                            }
                        }
                        sLine.PushMessage(getBatch(e_charge,xbd),e_mat,plocation);

                        //z = "ลบสำเร็จ";
                        isSuccess = true;
                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();

            }

            return z;
        }
    }

    public void testInsert(){
        final Dialog adjdialog = new Dialog(StockMmt.this);
        adjdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adjdialog.setContentView(R.layout.dialog_test);

        adjdialog.setCancelable(true);

        EditText etest;
        Button btn_test;


        btn_test = (Button)adjdialog.findViewById(R.id.btn_test);

        btn_test.setOnClickListener(new View.OnClickListener() {
            EditText etest = (EditText) adjdialog.findViewById(R.id.etest);

            public void onClick(View v) {
                String test ;
                test = etest.getText().toString().trim();
                insertSCAN(test);
                adjdialog.dismiss();

            }
        });



        adjdialog.show();

    }

    public class AddProScan extends AsyncTask<String, String, String> {

        Boolean isSuccess = false;
        String z = "";

        String exDup_loc = "";
        String exDup_time = "";
        String exDup_by = "";
        String p_bar_id ="";
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {

            if(isSuccess==true) {

                Toast.makeText(StockMmt.this, z, Toast.LENGTH_SHORT).show();

                if(isExdup()){
                    DialogConfirm(matcode,charge+'-'+bundle,exDup_loc,exDup_time,exDup_by,p_bar_id);
                }

            }else{
                onErrorDialog(getErdup(),getErnf(),z,getErloc(),getErMl(),getRs());
            }

            pbbar.setVisibility(View.GONE);
            FillList fillList = new FillList();
            fillList.execute(getSstorage(),getSt_sec(),getSbin());
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    if (rs.length() >= 9) {
                        setErnf(0);
                        setErdup(0);
                        String barCheck = "";
                        String dupCheck = "";
                        String checkEx = "Select bar_id,rmd_charge,r_bundle,r_qty,matcode,rmd_qa_grade,rmd_weight,rmd_grade,rmd_remark,rmd_date from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                        PreparedStatement getCheck = con.prepareStatement(checkEx);
                        ResultSet cks = getCheck.executeQuery();
                        while (cks.next()) {
                            barCheck = cks.getString("bar_id");
                            charge = cks.getString("rmd_charge");
                            bundle = cks.getString("r_bundle");
                            qty = cks.getString("r_qty");
                            qa_grade = cks.getString("rmd_qa_grade");
                            matcode = cks.getString("matcode");
                            weight = cks.getString("rmd_weight");
                            rmd_grade = cks.getString("rmd_grade");
                            remark = cks.getString("rmd_remark");
                            rmd_date = cks.getString("rmd_date");
                        }

                        if (barCheck == null || barCheck.equals("")) {
                            setErnf(1);
                        } else {

                            String checkDup = "select top 1 * from vw_stock_check \n" +
                                    "where charge ='" + charge + "' and bundle = " + bundle + " and  matcode = '" + matcode + "' and location = '"+getRs()+"' and flag is null \n" +
                                    "order by user_stamp desc";
                            PreparedStatement cd = con.prepareStatement(checkDup);
                            ResultSet cdps = cd.executeQuery();
                            while (cdps.next()) {
                                dupCheck = cdps.getString("item_barcode");
                                dupHn = cdps.getString("charge") + "-" + cdps.getString("bundle");
                                dupMat = cdps.getString("matcode");
                                dupName = cdps.getString("location");
                                dupBy = cdps.getString("user_add");
                                dupWhen = cdps.getString("user_stamp");
                            }

                            if (dupCheck == null || dupCheck.equals("")) {

                                String checkex = "select user_add,user_stamp,location from vw_stock_check \n" +
                                        "where charge ='" + charge + "' and bundle = " + bundle + " and  matcode = '" + matcode + "'  and flag is null \n" ;
                                PreparedStatement cex = con.prepareStatement(checkex);
                                ResultSet ceps = cex.executeQuery();

                                while (ceps.next()) {
                                    exDup_loc = ceps.getString("location");
                                    exDup_time = ceps.getString("user_stamp");
                                    exDup_by = ceps.getString("user_add");
                                }

                                if (exDup_loc == null || exDup_loc.equals("")) {

                                    String sflag = "update  tbl_stock_location   " +
                                            " set flag = 'A'  where charge = '"+charge+"' and bundle = '"+bundle+"' and matcode = '"+matcode+"' and flag is null ";

                                    PreparedStatement pflag = con.prepareStatement(sflag);
                                    pflag.executeUpdate();

                                    String sql = "insert into tbl_stock_location   " +
                                            "values ('" + getSstorage() + "','" + getSt_sec() + "','" + getSbin() + "'," + "'" + getRs() + "'," +
                                            "'" + matcode + "','" + barCheck + "','" + charge + "','"+getBatch(charge,bundle)+"','" + bundle + "'," + qty + "," + weight + "," +
                                            "'" + rmd_grade + "','" + qa_grade + "','" + remark + "','" + urs.getUserName() + "',CURRENT_TIMESTAMP , '"+rmd_date+"',NULL,convert(nvarchar(20),CURRENT_TIMESTAMP,112) ) ";

                                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                                    preparedStatement.executeUpdate();

                                    isSuccess = true;
                                    z = "บันทึกเรียบร้อยแล้ว";

                                    String bd = bundle;
                                    if(bundle!=null){
                                        if(Integer.parseInt(bundle) <= 9){
                                            bd = "0"+bundle;
                                        }
                                    }
                                    sLine.PushMessage(getBatch(charge,bundle),matcode,getRs());

                                }else{
                        //TODO
                                    setExdup(true);
                                    p_bar_id = barCheck;
                                    isSuccess = true;
                                }


                            } else {
                                setErdup(1);
                            }

                        }

                    }else{
                        setErloc(1);
                    }
                }

            } catch (Exception ex) {
                   isSuccess = false;
                   //Log.d("err",ex.getMessage().toString());
                    z = ex.getMessage().toString();


            }
            return z ;
        }
    }

    public class ConfirmAdd extends AsyncTask<String, String, String> {

        Boolean isSuccess = false;
        String z = "";

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {

            if(isSuccess==true) {

                Toast.makeText(StockMmt.this, z, Toast.LENGTH_SHORT).show();


            }else{
                //onErrorDialog(getErdup(),getErnf(),z,getErloc(),getErMl(),getRs());
            }

            pbbar.setVisibility(View.GONE);
            FillList fillList = new FillList();
            fillList.execute(getSstorage(),getSt_sec(),getSbin());
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    if (rs.length() >= 9) {

                        String checkEx = "Select bar_id,rmd_charge,r_bundle,r_qty,matcode,rmd_qa_grade,rmd_weight,rmd_grade,rmd_remark,rmd_date from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                        PreparedStatement getCheck = con.prepareStatement(checkEx);
                        ResultSet cks = getCheck.executeQuery();
                        while (cks.next()) {
                            charge = cks.getString("rmd_charge");
                            bundle = cks.getString("r_bundle");
                            qty = cks.getString("r_qty");
                            qa_grade = cks.getString("rmd_qa_grade");
                            matcode = cks.getString("matcode");
                            weight = cks.getString("rmd_weight");
                            rmd_grade = cks.getString("rmd_grade");
                            remark = cks.getString("rmd_remark");
                            rmd_date = cks.getString("rmd_date");
                        }


                                    String sflag = "update  tbl_stock_location   " +
                                            " set flag = 'A'  where charge = '"+charge+"' and bundle = '"+bundle+"' and matcode = '"+matcode+"' and flag is null ";

                                    PreparedStatement pflag = con.prepareStatement(sflag);
                                    pflag.executeUpdate();

                                    String sql = "insert into tbl_stock_location   " +
                                            "values ('" + getSstorage() + "','" + getSt_sec() + "','" + getSbin() + "'," + "'" + getRs() + "'," +
                                            "'" + matcode + "','" + params[0] + "','" + charge + "','"+getBatch(charge,bundle)+"','" + bundle + "'," + qty + "," + weight + "," +
                                            "'" + rmd_grade + "','" + qa_grade + "','" + remark + "','" + urs.getUserName() + "',CURRENT_TIMESTAMP , '"+rmd_date+"',NULL,convert(nvarchar(20),CURRENT_TIMESTAMP,112)) ";

                                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                                    preparedStatement.executeUpdate();

                                    isSuccess = true;
                                    z = "บันทึกเรียบร้อยแล้ว";

                                    String bd = bundle;
                                    if(bundle!=null){
                                        if(Integer.parseInt(bundle) <= 9){
                                            bd = "0"+bundle;
                                        }
                                    }
                                    sLine.PushMessage(getBatch(charge,bundle),matcode,getRs());

                                }else{

                    }
                }

            } catch (Exception ex) {
                isSuccess = false;
                //Log.d("err",ex.getMessage().toString());
                z = ex.getMessage().toString();


            }
            return z ;
        }
    }

    String getBatch(String charge,String bundle){
        String batch = "";
        int i=Integer.parseInt(bundle);
        if(charge.substring(0,1).equals("7") || charge.substring(0,1).equals("8")){

           batch = charge+""+String.format("%02d",  i);
        }else{
            batch = charge+""+String.format("%03d",  i);
        }
        return batch;
    }

    public void onErrorDialog(int eDup , int notFound, String err, int eLoc, final int eMl,String rsx) {

        String msg = "";
        if (eMl == 1) {
            msg = "ไม่มี " + rsx + " ในระบบ \nกรุณาเลือกใหม่";

        }else{

            if (notFound == 1) {
                msg = "ไม่พบข้อมูล \nQR : " + scanresult;
            } else if (eLoc == 1) {
                msg = "กรุณาเลือก ช่อง-เสาก่อนยิง";
            } else if (eDup == 1) {
                msg = "ยิงซ้ำ !!\nHN : " + dupHn + "\nMat : " + dupMat + "\nLocation : " + dupName + "\nเมื่อ : " + dupWhen + "\nโดย : " + dupBy + "\n\nQR : " + scanresult;
            } else if (notFound == 0 && eDup == 0 && eLoc == 0) {
                msg = err + "\n\nQR : " + scanresult;
            }
        }

        new AlertDialog.Builder(StockMmt.this)

                .setTitle("ผิดพลาด")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       /* if (eMl == 1) {
                            onPilClick();
                        }*/
                    }
                })
                /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         // do nothing
                     }
                 })  */

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

//TODO -> appsync -> update db
    public void DialogConfirm(String mat, String hn , String loc, String time, String by, final String c_bar_id) {

        String msg = "";

            msg = hn+"\n"+mat+"\nเคยถูกนับเข้าที่ "+loc+"\nเวลา "+time.substring(0,16)+"\nโดย "+by;


        new AlertDialog.Builder(StockMmt.this)

                .setTitle("ยืนยันการทำซ้ำ")
                .setMessage(msg)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConfirmAdd cfa = new ConfirmAdd();
                        cfa.execute(c_bar_id);
                    }
                })
                 .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         // do nothing
                     }
                 })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void UpdateStorage(String txt){


//        lch.arrStorge = new String[]{""};
        tv_storage.setText(txt);
        getResult();
        lch.MemberList.clear();


        for (Map<String, String> entry : lch.seclist) {
            for (String key : entry.keySet()) {
                String value = entry.get(key);
                if(key.equals(txt)){
                    lch.MemberList.add(value);
                }
            }
        }


//        int i = 0;
//        String key[] = new String[lch.hashMap.size()];
//        for (Map.Entry<String, String> entry : lch.hashMap.entrySet()) {
//            if(entry.getValue().equals(txt)){
//
//                lch.MemberList.add(entry.getKey());
////                value[i++] = entry.getValue();
////                i++;
//            }
//        }




        Collections.sort(lch.MemberList);
     /*   lch.arrStorge = new String[i];
        lch.arrStorge =  key ;*/


    }

    public void UpdateSection(String txt){

        tv_section.setText(txt);
        getResult();

    }

    public void UpdateBin(String txt){
        tv_bin.setText(txt);
        getResult();


    }

    public void UpdateLoc(String txt){


            tv_loc.setText(txt);

    }

    private void getResult(){

        //rs = isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin());

        if(SectionMode==true){
            UpdateLoc(isNull(getSstorage())+"-"+isNull(getSt_sec()));
        }else{
            UpdateLoc(isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin()));
        }

        setRs(isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin()));


        Boolean isfound = lch.StorageList.contains(getRs());
        if(isfound == true){
            tv_loc.setBackgroundColor(Color.parseColor("#21DF86"));
            setErMl(0);
        }else{
            tv_loc.setBackgroundColor(Color.parseColor("#ffff4444"));
            setErMl(1);
            if(rs.length()>=9){
                onErrorDialog(0,0,"z",0,getErMl(),rs);
            }

        }


    }

    public  void  onPilClick(){

        final Dialog dialog = new Dialog(StockMmt.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);
        btnSv.setText(lang.map.get("confirm"));
        pole_no = (TextView)dialog.findViewById(R.id.textView3);
        pole_no.setText(lang.map.get("pole"));
        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pil = edtPil.getText().toString();

                setSbin(lch.fPill(pil));
                UpdateBin(pil);
                fillList = new FillList();
                fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),isNull(getSbin()));
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public class LocationBin extends AsyncTask<String, String, String> {

        //String z = "";
        //Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            //   pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            //  pbbar.setVisibility(View.GONE);



        }
        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con =  connectionClass.CONN();

                if (con == null) {

                } else {

                    switch (params[0]){
                        case  "ZUBB" : params[0] = " plant2 in ('ZUBB') ";
                            break;
                        case  "RS" : params[0] = " plant2 in ('ZUBB') ";
                            break;
                        case  "SPN" : params[0] = " plant2 = 'WPN' ";
                            break;
                        case  "OPS" : params[0] = " plant2 in ('OPS','OC5','OC6') ";
                            break;
                        case  "OCP" : params[0] = " plant2 in ('OPS','OC5','OC6') ";
                            break;
                        case  "SPS" : params[0] = " plant2 in ('SPS') ";
                            break;
                        case  "MMT" : params[0] = " plant2 in ('MR7','MR8','MMT') ";
                            break;
                    }



                    String query = "select location from vw_storage where  "+params[0]+" " ;
                    PreparedStatement ts = con.prepareStatement(query);
                    ResultSet bs = ts.executeQuery();
                    locHelp.StorageList.clear();
                    while (bs.next()) {
                        locHelp.StorageList.add(bs.getString("location"));
                    }

                    String squery = "select storage from vw_storage where "+params[0]+" group by storage" ;
                    PreparedStatement sts = con.prepareStatement(squery);
                    ResultSet sbs = sts.executeQuery();
                    locHelp.storages.clear();
                    while (sbs.next()) {
                        locHelp.storages.add(sbs.getString("storage"));
                    }

                  /*  String equery = "SELECT section\n" +
                            "     ,replace(case when right(section,1) ='R' then left(section,1)+' ขวา'\n" +
                            "\t when right(section,1) ='L' then left(section,1)+' ซ้าย'\n" +
                            "\t else section  end ,'A','10') section_d\n" +
                            "\n" +
                            "  FROM vw_storage where plant = '"+params[0]+"' \n" +
                            "  group by section" ;*/

                    String equery = " SELECT section,case when right(section,1) ='R' then left(section,1)+' ขวา' \n" +
                            "when right(section,1) ='L' then left(section,1)+' ซ้าย' \n" +
                            "when right(section,1) ='C' then left(section,1)+' กลาง' \n" +
                            "else section  end section_d,storage \n" +
                            "FROM vw_storage where  "+params[0]+" \n" +
                            "group by section,storage ";

                    PreparedStatement ets = con.prepareStatement(equery);
                    ResultSet ebs = ets.executeQuery();

                    locHelp.section_d.clear();
                    locHelp.sections.clear();
//                    locHelp.hashMap.clear();
                    locHelp.seclist.clear();

                    int i = 0;
                    while (ebs.next()) {

                        locHelp.section_d.add(ebs.getString("section_d"));
                        locHelp.sections.add(ebs.getString("section"));

                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put(ebs.getString("storage"),ebs.getString("section"));
                        locHelp.seclist.add(datanum);
//                        locHelp.mapStorage.put(ebs.getString("storage"),ebs.getString("section");
//                        i++;
                    }
                }
            } catch (Exception ex) {

            }
            return "";
        }
    }

}
