package com.example.stockrecheck;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OndayActivity extends AppCompatActivity {

    final Context context = this;
    View vxl ;
    int mYear;
    int mMonth;
    int mDay;
    TextView mDateDisplay;
    ProgressBar pbbar ;
    TextView tx_ch,tx_pil,txtRsLoc,txt_loc;
    EditText hideEdt;

    CurLocation cl;
    MLocation loc;
    FetchLocation fetchloclist;
    ConnectionClass connectionClass;
    UserHelper usrHelper;
    Button btnLocSv;
    static final int DATE_DIALOG_ID = 0;
    String getMat,dupLoc,idate,paramdate,ch,pill,pil,scanresult,dupMat,dupDat,dupSeq,gdat,gloc;
    List<Map<String, String>> countlist  = new ArrayList<Map<String, String>>();
    ListView lv_location;

    static String xfr,xrl,xch,xpil,xloc;

    ContextThemeWrapper cw = new ContextThemeWrapper( this, R.style.AlertDialogTheme );
    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onday);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fetchloclist = new FetchLocation();
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();

        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        txt_loc = (TextView)findViewById(R.id.tv_locationx);
        hideEdt = (EditText)findViewById(R.id.hedt2);
        pbbar.setVisibility(View.GONE);


        cl = new CurLocation();
        loc = new MLocation();

        lv_location = findViewById(R.id.lvitem);
        mDateDisplay = findViewById(R.id.mDateDisplay);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        FillList fillList = new FillList();

        if(gdat==null){

            fillList.execute(paramdate);
        }else{
            fillList.execute(gdat);
        }

        //Toast.makeText(OndayActivity.this, loc.getCloc(), Toast.LENGTH_LONG).show();



        loc.setFr("R");
        loc.setLr("L");


        if(cl.l==null || cl.l.equals("")){
            txt_loc.setText("เลือกช่อง");
        }else{
            txt_loc.setText(cl.l);
        }

        if(!txt_loc.getText().toString().equals("เลือกช่อง")){
            loc.setCurLoc(true);
            loc.setCloc(txt_loc.getText().toString());
        }


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            gdat = "";
            gdat = "";


        } else {
            gloc = extras.getString("cdat");
            gdat = extras.getString("gloc");
        }






        hideEdt.requestFocus();
        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
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
                hideEdt.requestFocus();
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }

    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;

            if(loc.getCurLoc()==false){
                new AlertDialog.Builder(context)

                        .setTitle("ผิดพลาด")
                        .setMessage("เลือกช่องก่อนทำ")
                        .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                    onLocationClick(vxl);

                            }
                        })
                        /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 // do nothing
                             }
                         })  */

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                ChkCount cck = new ChkCount();
                cck.execute(loc.getCloc(),paramdate,rsscan);
            }


            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");
    }

    private void updateDisplay() {
        String m;
        String d;
        this.mDateDisplay.setText(
                new StringBuilder()

                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear + 543).append(" "));

        if((mMonth+1)< 10){
            m =  "0"+(mMonth+1);
        }else{
            m = String.valueOf((mMonth + 1));
        }
        if((mDay)<= 9){
            d =  "0"+(mDay);
        }else{
            d = String.valueOf((mDay));
        }

        this.idate = mYear+"-"+m+"-"+d;
        this.paramdate = mYear+""+m+""+d;

        // Toast.makeText(MainActivity.this, "id "+this.idate+"\n"+"pd "+this.paramdate, Toast.LENGTH_LONG).show();

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();

                    FillList fillList = new FillList();
                    fillList.execute(paramdate);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

        }
        return null;
    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            String[] from = {"idBin","MatDesc","QTY"};
            int[] views = {R.id.Bin,R.id.MatDesc,R.id.QTY};
            final SimpleAdapter ADA = new SimpleAdapter(OndayActivity.this,
                    countlist, R.layout.adp_list_loc_line, from,
                    views){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    //LinearLayout lnx = (LinearLayout) view.findViewById(R.id.lay);
                    TextView qty = view.findViewById(R.id.QTY);


                    //int lx = Integer.parseInt(vbelnlist.get(position).get("sscan"));
                    if(countlist.get(position).get("confirmBy")!=null){
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
            lv_location.setAdapter(ADA);


            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String where = "where CountDate = '"+params[0]+"' ";

                    String sql = "select  *,left(Material,3) as mg from tbl_physicalcount_location  "+where+"  ";
                    Log.d("selx",sql);
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    int ids = 0 ;

                    countlist.clear();
                    while (rs.next()) {
                        ids ++;
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("SEQ", rs.getString("SEQ"));
                        datanum.put("Location", rs.getString("Location"));
                        datanum.put("idBin", rs.getString("Location")+"-"+rs.getString("Bin"));
                        datanum.put("MatDesc", rs.getString("mg")+""+rs.getString("Size"));
                        datanum.put("QTY", rs.getString("QTY"));
                        datanum.put("confirmBy", rs.getString("confirmBy"));
                        datanum.put("CountDate", rs.getString("CountDate"));
                        datanum.put("Material", rs.getString("Material"));
                        countlist.add(datanum);

                    }

                    z = "Success";

                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;

        }
    }


    public void onDateclick(View v) {
        showDialog(DATE_DIALOG_ID);
    }

    public  void  onLocationClick(View v){

        final Dialog loc_dialog = new Dialog(OndayActivity.this);
        loc_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loc_dialog.setContentView(R.layout.dialog_location_picker);

        loc_dialog.setCancelable(true);

        final TextView tx_fr, tx_rl;

        tx_ch = (TextView) loc_dialog.findViewById(R.id.d_ch);
        tx_fr = (TextView) loc_dialog.findViewById(R.id.d_fr);
        tx_rl = (TextView) loc_dialog.findViewById(R.id.d_rl);
        tx_pil = (TextView) loc_dialog.findViewById(R.id.d_pil);
        txtRsLoc = (TextView) loc_dialog.findViewById(R.id.txtRsLoc);
        btnLocSv = (Button) loc_dialog.findViewById(R.id.btnLocSv);
        btnLocSv.setVisibility(View.GONE);
        if (loc.getCloc().equals("") && cl.l == null) {

        } else {
            if (cl.l == null || cl.l.equals("")) {
                locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                checkCH(loc.getCh());
                checkPIL(loc.getPill());
                tx_ch.setText(loc.getCh());
                tx_pil.setText(loc.getPill());

                if (loc.getLr().equals("R")) {
                    tx_rl.setText("ขวา");
                } else {
                    tx_rl.setText("ซ้าย");
                }
                if (loc.getFr().equals("F")) {
                    tx_fr.setText("หน้า");
                } else {
                    tx_fr.setText("หลัง");
                }
                txtRsLoc.setText(loc.getCloc());

            } else {
                loc.setFr(cl.fr);
                loc.setLr(cl.lr);
                loc.setCh(cl.ch);
                loc.setPill(cl.pill);

                locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                checkCH(loc.getCh());
                checkPIL(loc.getPill());
                tx_ch.setText(loc.getCh());
                tx_pil.setText(loc.getPill());

                if (loc.getLr().equals("R")) {
                    tx_rl.setText("ขวา");
                } else {
                    tx_rl.setText("ซ้าย");
                }
                if (loc.getFr().equals("F")) {
                    tx_fr.setText("หน้า");
                } else {
                    tx_fr.setText("หลัง");
                }
                txtRsLoc.setText(loc.getCloc());

            }

        }

        btnLocSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_loc.setText(loc.getCloc());
                xch = loc.getCh();
                xfr = loc.getFr();
                xrl = loc.getLr();
                xpil = loc.getPill();
                xloc = loc.getCloc();
                loc_dialog.dismiss();

            }
        });

        tx_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChClick(view);

            }
        });
        tx_pil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPilClick(view);

            }
        });
        tx_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tx_fr.getText().toString().equals("หน้า")) {
                    tx_fr.setText("หลัง");
                    loc.setFr("R");
                    locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                } else {
                    tx_fr.setText("หน้า");
                    loc.setFr("F");
                    locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                }
            }
        });
        tx_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tx_rl.getText().toString().equals("ซ้าย")) {
                    tx_rl.setText("ขวา");
                    loc.setLr("R");
                    locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                } else {
                    tx_rl.setText("ซ้าย");
                    loc.setLr("L");
                    locationChecker(loc.getFr(), loc.getLr(), loc.getCh(), loc.getPill());
                }
            }
        });

        loc_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (xch == null) {
                    xch = "";
                }
                if (xrl == null) {
                    xrl = "";
                }
                if (xfr == null) {
                    xfr = "";
                }
                if (xpil == null) {
                    xpil = "";
                }
                if (xloc == null) {
                    xloc = "";
                }

                loc.setCh(xch);
                loc.setPill(xpil);
                loc.setLr(xrl);
                loc.setFr(xfr);
                loc.setCloc(xloc);

                cl.setFr(xfr);
                cl.setPill(xpil);
                cl.setLr(xrl);
                cl.setCh(xch);
                cl.setCurlocation(xloc);

                //Toast.makeText(ReceiveItem.this, "fr "+xfr+"\nrl "+xrl+"\nch "+xch+"\npill "+xpil+"\nloc "+xloc, Toast.LENGTH_LONG).show();
                //Toast.makeText(ReceiveItem.this, "fr "+tx_fr.getText().toString()+"\nrl "+tx_rl.getText().toString()+"\nch "+tx_ch.getText().toString()+"\npill "+tx_pil.getText().toString()+"\nloc "+txtRsLoc.getText().toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(ReceiveItem.this, "fr "+cl.fr+"\nrl "+cl.lr+"\nch "+cl.ch+"\npill "+cl.pill+"\nloc "+cl.l, Toast.LENGTH_LONG).show();
                //Toast.makeText(ReceiveItem.this, "FFRR "+loc.getFr()+"\nRRLL "+loc.getLr()+"\nCCHH "+loc.getCh()+"\nPP "+loc.getPill()+"\nLL "+loc.getCloc(), Toast.LENGTH_LONG).show();
                //locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());

            }
        });

        loc_dialog.show();


    }

    public void onChClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle("เลือกช่อง");
        String[] animals = {"ช่อง 1","ช่อง 2", "ช่อง 3", "ช่อง 4", "ช่อง 5","ช่อง 6","ช่อง 7","ช่อง 8","ช่อง 9","ช่อง 10"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: ch = "1";
                        break;
                    case 1: ch = "2";
                        break;
                    case 2: ch = "3";
                        break;
                    case 3: ch = "4";
                        break;
                    case 4: ch = "5";
                        break;
                    case 5: ch = "6";
                        break;
                    case 6: ch = "7";
                        break;
                    case 7: ch = "8";
                        break;
                    case 8: ch = "9";
                        break;
                    case 9: ch = "10";
                        break;

                }
                loc.setCh(ch);
                tx_ch.setText(loc.getCh());
                checkCH(loc.getCh());
                locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  void  onPilClick(View v){

        final Dialog dialog = new Dialog(OndayActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pil = edtPil.getText().toString();
                /*if(pil.equals("")||pil==null){
                    pil = "0";
                }*/
                loc.setPill(pil);
                tx_pil.setText(loc.getPill());
                checkPIL(loc.getPill());
                locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private  void  checkCH(String ch){
        if(ch.length()<1){
            tx_ch.setTextColor(Color.parseColor("#FA032C"));
            tx_ch.setBackground(getResources().getDrawable(R.drawable.border_red));

        }
        else{
            tx_ch.setTextColor(Color.parseColor("#00B94C"));
            tx_ch.setBackground(getResources().getDrawable(R.drawable.border_green));
        }

    }
    private  void  checkPIL(String pil){
        if(pil.length()<1){
            tx_pil.setTextColor(Color.parseColor("#FA032C"));
            tx_pil.setBackground(getResources().getDrawable(R.drawable.border_red));
        }
        else{
            tx_pil.setTextColor(Color.parseColor("#00B94C"));
            tx_pil.setBackground(getResources().getDrawable(R.drawable.border_green));

        }

    }


    public void locationChecker(String pfr,String prl,String pch,String ppil){
        Boolean isFound = false;
        loc.setCloc(pch+""+pfr+""+prl+"-"+ppil.trim());
        isFound = loc.checkLocation(loc.getCloc());
        if(isFound==true){
            txtRsLoc.setTextColor(Color.parseColor("#00B94C"));
            txtRsLoc.setBackgroundColor(Color.parseColor("#D0FFDC"));
            btnLocSv.setVisibility(View.VISIBLE);
        }else{
            txtRsLoc.setTextColor(Color.parseColor("#FA032C"));
            txtRsLoc.setBackgroundColor(Color.parseColor("#ffcfcc"));
            btnLocSv.setVisibility(View.GONE);
        }
        this.txtRsLoc.setText(loc.getCloc());

    }

    public class ChkCount extends AsyncTask<String, String, String> {

        String z = "";
        Boolean ischk = false;
        Boolean miss = false;



        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(OndayActivity.this, r, Toast.LENGTH_SHORT).show();

            if(ischk==true){

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(OndayActivity.this);
                builder.setTitle("นับซ้ำ");
                builder.setMessage("มีการนับสินค้านี้ไปแล้วที่ "+dupLoc);
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(OndayActivity.this, Stock.class);
                        i.putExtra("loc", dupLoc);
                        i.putExtra("cdate", dupDat);
                        i.putExtra("mat", dupMat);
                        startActivity(i);
                        finish();
                        dialog.dismiss();

                    }
                });

                builder.show();

            }else{
                if(miss==true){
                    new AlertDialog.Builder(context)

                            .setTitle("ผิดพลาด")
                            .setMessage("ไม่พบข้อมูล\n"+loc.getCloc()+" "+getMat)
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     // do nothing
                                 }
                             })  */

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{

                    InsertTag ist = new InsertTag();
                    ist.execute(paramdate,getMat,loc.getCloc());


                }

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    String getmat = "select top 1 matcode from [PP].[dbo].[vw_barcode_item]  WHERE bar_id = '"+params[2]+"' order by rmd_date desc    ";

                    PreparedStatement gps = con.prepareStatement(getmat);
                    ResultSet grs = gps.executeQuery();


                    while (grs.next()) {
                        getMat = grs.getString("matcode");

                    }

                    if(getMat==null || getMat.equals("")){
                        z = "ไม่พบข้อมูล";
                        //ischk = false;
                        miss = true;
                    }else{

                        String fetch = "select SEQ,Material,countdate,location from tbl_physicalcount_location where location = '"+params[0].substring(0,3)+"' and Bin = '"+params[0].substring(params[0].length() - 2)+"' and countdate = '"+params[1]+"' and Material = '"+getMat+"'  ";

                        PreparedStatement ps = con.prepareStatement(fetch);
                        ResultSet rs = ps.executeQuery();

                        Log.d("fetch",fetch);

                        while (rs.next()) {
                            dupSeq = rs.getString("SEQ");
                            dupLoc = rs.getString("location");
                            dupMat = rs.getString("Material");
                            dupDat = rs.getString("countdate");
                        }

                        if (dupLoc == null || dupLoc.equals("")){
                            ischk = false;


                        }else{
                            ischk = true;
                            z = "เรียบร้อยแล้ว";
                        }
                    }


                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";
                ischk = false;
            }
            return z;
        }
    }



    public class InsertTag extends AsyncTask<String, String, String> {

        String z = "";
        String iloc,imat,idat,iseq;


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
           // Toast.makeText(OndayActivity.this, z, Toast.LENGTH_SHORT).show();
           // Toast.makeText(OndayActivity.this, idat+"\n"+imat+"\n"+iloc, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(OndayActivity.this, Stock.class);
            i.putExtra("loc", iloc);
            i.putExtra("cdate", idat);
            i.putExtra("mat", imat);
            startActivity(i);
            finish();


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String storep = " exec [dbo].[createtag] '"+params[0]+"','"+params[1]+"','"+params[2]+"' ";
                    PreparedStatement ps = con.prepareStatement(storep);
                    ps.executeUpdate();

                   // Log.d("store", storep);

                    String iquery = " select top 1 countdate,location,material from tbl_physicalcount_location where countdate = '"+params[0]+"' and material = '"+params[1]+"' and location = '"+loc.getCloc().substring(0,3)+"' order by seq desc  ";
                    PreparedStatement ips = con.prepareStatement(iquery);
                    ResultSet irs = ips.executeQuery();

                    while (irs.next()){
                        iloc = irs.getString("location");
                        idat = irs.getString("countdate");
                        imat = irs.getString("material");
                    }

                    z = "สำเร็จ";

                    }

            } catch (Exception ex) {
                z = ex.getMessage();
                //ischk = false;
            }
            return z;
        }
    }

}
