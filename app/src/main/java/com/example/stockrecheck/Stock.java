package com.example.stockrecheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Stock extends AppCompatActivity {

    ProgressBar pbbar;
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    String gloc,gbar,h_location2,h_location,h_col,h_mat,h_desc,h_con,h_b1,h_b2,h_b3,h_p1,h_p2,h_p3,h_i1,h_i2,h_i3,h_itxt1,h_itxt2,h_itxt3;
    String h_cdate,h_offset,h_bar,gmat,gseq,gcdate,h_remark,h_good,h_rust,h_clean,h_poor,h_edge,h_short,h_crook,h_note,h_notsq,h_0,h_seq;
    TextView tv_remark,tvTxtLocation,tvCol,tvChecker,tvMat,tvDesc,tvLocHead,status;
    CheckBox cgood,crust,cclean,cpoor,cedge,cshort,ccrook,cnote,cnotsq,c0,coffset;
    EditText egood,erust,eclean,epoor,eedge,eshort,ecrook,enote,enotsq,offset,remark;
    EditText b1,b2,b3,p1,p2,p3,i1,i2,i3;
    Button saveBtn,closeBtn,btnUnlock;

    Boolean outofstock = false;

    static String cal1 = "";
    static String cal2 = "";
    static String cal3 = "";
    static String calsum1 = "";
    static String calsum2 = "";
    static String calsum3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();



        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            gbar = "";
            gseq = "";
            gcdate = "";
            gmat = "";
            gloc = "";

        } else {
            gbar = extras.getString("bar");
            gseq = extras.getString("seq");
            gcdate = extras.getString("cdate");
            gmat = extras.getString("mat");
            gloc = extras.getString("loc");
        }

       // gbar = "6RR-027";

        tvChecker = findViewById(R.id.tvChecker);
        tvCol = findViewById(R.id.tvCol);
        tvDesc = findViewById(R.id.tvDesc);
        tvMat = findViewById(R.id.tvMat);
        tvTxtLocation =findViewById(R.id.tvTxtLocation);
//        tvLocHead = findViewById(R.id.tvLocHead);
        status = findViewById(R.id.status);
        tv_remark =findViewById(R.id.txt_remark);

        cgood = findViewById(R.id.chkGood);
        crust = findViewById(R.id.chkRust);
        cclean = findViewById(R.id.chkClean);
        cpoor = findViewById(R.id.chkPoor);
        cshort = findViewById(R.id.chkShort);
        ccrook = findViewById(R.id.chkCrooked);
        cedge = findViewById(R.id.chkEdge);
        cnote = findViewById(R.id.chkNotEqual);
        cnotsq = findViewById(R.id.chkNotsquare);
        c0 = findViewById(R.id.chkOutStock);

        egood = findViewById(R.id.edtGood);
        erust = findViewById(R.id.edtRust);
        eclean = findViewById(R.id.edtClean);
        epoor = findViewById(R.id.edtPoor);
        eedge = findViewById(R.id.edtEdge);
        eshort = findViewById(R.id.edtShort);
        ecrook = findViewById(R.id.edtCrooked);
        enote = findViewById(R.id.edtNotEqual);
        enotsq = findViewById(R.id.edtNotsquare);
        offset = findViewById(R.id.edtOffset);
        remark = findViewById(R.id.remark);
        coffset = findViewById(R.id.chkOffset);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);

        saveBtn = findViewById(R.id.btnSave);
        closeBtn = findViewById(R.id.btnClose);
        btnUnlock = findViewById(R.id.btnClose2);


        if(gbar==null){
            FillList fillList = new FillList();
            fillList.execute(gloc,gcdate,"c",gmat);
        }else{
            FillList fillList = new FillList();
            fillList.execute(gbar,gcdate,"b","");
        }



        cgood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    egood.setEnabled(true);
                    egood.requestFocus();
                }
                else{
                    egood.setEnabled(false);
                    egood.setText("");
                }
            }
        });
        crust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    erust.setEnabled(true);
                    erust.requestFocus();
                }
                else{
                    erust.setEnabled(false);
                    erust.setText("");
                }
            }
        });
        cclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    eclean.setEnabled(true);
                    eclean.requestFocus();
                }
                else{
                    eclean.setEnabled(false);
                    eclean.setText("");
                }
            }
        });
        cpoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    epoor.setEnabled(true);
                    epoor.requestFocus();
                }
                else{
                    epoor.setEnabled(false);
                    epoor.setText("");
                }
            }
        });
        cshort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    eshort.setEnabled(true);
                    eshort.requestFocus();
                }
                else{
                    eshort.setEnabled(false);
                    eshort.setText("");
                }
            }
        });
        ccrook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    ecrook.setEnabled(true);
                    ecrook.requestFocus();
                }
                else{
                    ecrook.setEnabled(false);
                    ecrook.setText("");
                }
            }
        });
        cedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    eedge.setEnabled(true);
                    eedge.requestFocus();
                }
                else{
                    eedge.setEnabled(false);
                    eedge.setText("");
                }
            }
        });
        cnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    enote.setEnabled(true);
                    enote.requestFocus();
                }
                else{
                    enote.setEnabled(false);
                    enote.setText("");
                }
            }
        });
        cnotsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    enotsq.setEnabled(true);
                    enotsq.requestFocus();
                }
                else{
                    enotsq.setEnabled(false);
                    enotsq.setText("");
                }
            }
        });

        coffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    offset.setEnabled(true);
                    offset.requestFocus();
                }
                else{
                    offset.setEnabled(false);
                    offset.setText("");
                }
            }
        });



        c0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {

                    b1.setText("0");
                    p1.setText("0");
                    i1.setText("0");

                    b2.setText("0");
                    p2.setText("0");
                    i2.setText("0");

                    b3.setText("0");
                    p3.setText("0");
                    i3.setText("0");

                    b1.setEnabled(false);
                    b2.setEnabled(false);
                    b3.setEnabled(false);

                    p1.setEnabled(false);
                    p2.setEnabled(false);
                    p3.setEnabled(false);

                    i1.setEnabled(false);
                    i2.setEnabled(false);
                    i3.setEnabled(false);

                    crust.setEnabled(false);
                    cclean.setEnabled(false);
                    ccrook.setEnabled(false);
                    cedge.setEnabled(false);
                    cgood.setEnabled(false);
                    cnote.setEnabled(false);
                    cnotsq.setEnabled(false);
                    cpoor.setEnabled(false);
                    cshort.setEnabled(false);
                    coffset.setEnabled(false);

                    erust.setText("");
                    eclean.setText("");
                    ecrook.setText("");
                    eedge.setText("");
                    egood.setText("");
                    enote.setText("");
                    enotsq.setText("");
                    epoor.setText("");
                    eshort.setText("");
                    offset.setText("");

                    erust.setEnabled(false);
                    eclean.setEnabled(false);
                    ecrook.setEnabled(false);
                    eedge.setEnabled(false);
                    egood.setEnabled(false);
                    enote.setEnabled(false);
                    enotsq.setEnabled(false);
                    epoor.setEnabled(false);
                    eshort.setEnabled(false);
                    offset.setEnabled(false);

                }
                else{

                    b1.setText("");
                    p1.setText("");
                    i1.setText("");

                    b2.setText("");
                    p2.setText("");
                    i2.setText("");

                    b3.setText("");
                    p3.setText("");
                    i3.setText("");

                    b1.setEnabled(true);
                    b2.setEnabled(true);
                    b3.setEnabled(true);

                    p1.setEnabled(true);
                    p2.setEnabled(true);
                    p3.setEnabled(true);

                    i1.setEnabled(true);
                    i2.setEnabled(true);
                    i3.setEnabled(true);

                    crust.setEnabled(true);
                    cclean.setEnabled(true);
                    ccrook.setEnabled(true);
                    cedge.setEnabled(true);
                    cgood.setEnabled(true);
                    cnote.setEnabled(true);
                    cnotsq.setEnabled(true);
                    cpoor.setEnabled(true);
                    cshort.setEnabled(true);
                    offset.setEnabled(true);

                }
            }
        });



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveOutPut sop = new SaveOutPut();
                sop.execute(gbar,"");
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(Stock.this,"CLOICK",Toast.LENGTH_SHORT).show();


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Stock.this);
                builder.setTitle("ปลดล็อค");
                builder.setMessage("ยืนยันการปลดล็อค ?");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        unLock ulck = new unLock();
                        ulck.execute();
//                        onRestart();
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
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Stock.this);
                builder.setTitle("ปิดจบ");
                builder.setMessage("ยืนยันการปิดจบ ?");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SaveOutPut sop = new SaveOutPut();
                        sop.execute(gbar,"C");
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
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    pCal(h_i1,h_itxt1,"1");


            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pCal(h_i2,h_itxt2,"2");
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pCal(h_i3,h_itxt3,"3");
            }
        });


    }

    void lockScreen(){
        status.setVisibility(View.VISIBLE);
        status.setText("ปิดจบโดย "+h_con);

        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);

        p1.setEnabled(false);
        p2.setEnabled(false);
        p3.setEnabled(false);

        i1.setEnabled(false);
        i2.setEnabled(false);
        i3.setEnabled(false);

        crust.setEnabled(false);
        c0.setEnabled(false);
        cclean.setEnabled(false);
        ccrook.setEnabled(false);
        cedge.setEnabled(false);
        cgood.setEnabled(false);
        cnote.setEnabled(false);
        cnotsq.setEnabled(false);
        cpoor.setEnabled(false);
        cshort.setEnabled(false);
        coffset.setEnabled(false);


    }

    String getTV(TextView tv){
        if(tv.getText().toString()==null || tv.getText().toString().equals("")){
            return "NULL";
        }
        return "'"+tv.getText().toString()+"'";
    }
    String getEDT(EditText edt){
        if(edt.getText().toString()==null || edt.getText().toString().equals("")){
            return "NULL";
        }
        return edt.getText().toString();
    }

    String getEDTre(EditText edt){
        if(edt.getText().toString()==null || edt.getText().toString().equals("")){
            return "NULL";
        }
        return "'"+edt.getText().toString()+"'";
    }

    Integer offSet(String offset){
        if(offset==null || offset.equals("") || offset.equals("null")  || offset.equals("NULL") ){
            return  0;
        }else{
            return Integer.valueOf(offset);
        }
    }

    String getEDTC(EditText edt){
        if(edt.getText().toString()==null || edt.getText().toString().equals("")){
            return "";
        }
        return edt.getText().toString();
    }


    String getLocText(String param){
        String m = "";
        String r = "";
        if(param!= null && param.length() >= 2){

        if(param.substring(2,3).equals("L")) {
            r = "ซ้าย";
        }else if(param.substring(2,3).equals("R")){
            r = "ขวา";
        }

      /*  if(param.substring(1,2).equals("F")) {
            m = " หน้า";
        }else if(param.substring(1,2).equals("R")){
            m = " หลัง";
        }*/
            return "ช่อง "+param.substring(0,1)+r;
      }else{
            return "";
        }

    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);

            b1.setText(h_b1);
            b2.setText(h_b2);
            b3.setText(h_b3);

            p1.setText(h_p1);
            p2.setText(h_p2);
            p3.setText(h_p3);

            i1.setText(h_i1);
            i2.setText(h_i2);
            i3.setText(h_i3);
            if(h_remark==null || h_remark.equals("")){

            }else{
//                remark.setVisibility(View.VISIBLE);
                remark.setText(h_remark);
            }


            if(h_good!=null){
                egood.setText(h_good);
                cgood.setChecked(true);
            }
            if(h_rust!=null){
                erust.setText(h_rust);
                crust.setChecked(true);
            }
            if(h_clean!=null){
                eclean.setText(h_clean);
                cclean.setChecked(true);
            }
            if(h_poor!=null){
                epoor.setText(h_poor);
                cpoor.setChecked(true);
            }
            if(h_edge!=null){
                eedge.setText(h_edge);
                cedge.setChecked(true);
            }
            if(h_short!=null){
                eshort.setText(h_short);
                cshort.setChecked(true);
            }
            if(h_crook!=null){
                ecrook.setText(h_crook);
                ccrook.setChecked(true);
            }
            if(h_note!=null){
                enote.setText(h_note);
                cnote.setChecked(true);
            }
            if(h_notsq!=null){
                enotsq.setText(h_notsq);
                cnotsq.setChecked(true);
            }
            if(h_offset!=null  ){
                if(h_offset==null || h_offset.equals("0")){
                    offset.setText("");
                    coffset.setChecked(false);
                }else{
                    offset.setText(h_offset);
                    coffset.setChecked(true);
                }

            }
            if(h_remark!=null){
                remark.setText(h_remark);

            }

            if(h_0!=null){
                c0.setChecked(true);
            }
//            tvChecker.setText(""+h_con);

            tvCol.setText("เสาที่ "+h_col);
            tvDesc.setText(h_desc);
            tvMat.setText(h_mat);
            tvTxtLocation.setText(h_location);
//            tvLocHead.setText(h_location);



                if(h_con==null || h_con.equals("")) {
                    saveBtn.setVisibility(View.VISIBLE);
                    closeBtn.setVisibility(View.VISIBLE);
                }else{
                    saveBtn.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.GONE);
                    lockScreen();

//                    if(usrHelper.getUserName().equals("Surasak.w") || usrHelper.getUserName().equals("Tanida.p") || usrHelper.getUserName().equals("Supunsa.k")){
//                        btnUnlock.setVisibility(View.VISIBLE);
//                    }
                    if(usrHelper.getUnlock().equals("10")){
                        btnUnlock.setVisibility(View.VISIBLE);
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

                    String query = "select *,left(Location,2) as Location2,format(SEQ,'000') as SEQF  from STOCK.dbo.tbl_physicalcount_location where barcode = '"+params[0]+"' and countdate = '"+params[1]+"' ";;

                    if(params[2].equals("c")){
                        query = "select *,left(Location,2) as Location2,format(SEQ,'000') as SEQF  from STOCK.dbo.tbl_physicalcount_location where Location = '"+params[0]+"' and countdate = '"+params[1]+"' and Material = '"+params[3]+"'  ";
                    }


//                    Log.d("query",query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        h_cdate = rs.getString("countDate");
                        h_offset = rs.getString("offset");
                        h_remark = rs.getString("Remark");
                        h_col = rs.getString("Bin");
                        h_desc = rs.getString("MatDesc");
                        h_location = rs.getString("Location");
                        h_location2 = rs.getString("Location2");
                        h_mat = rs.getString("Material");
                        h_con = rs.getString("confirmBy");
                        h_bar = rs.getString("barcode");

                        h_b1 = rs.getString("Bundle1");
                        h_b2 = rs.getString("Bundle2");
                        h_b3 = rs.getString("Bundle3");

                        h_p1 = rs.getString("PerBundle1");
                        h_p2 = rs.getString("PerBundle2");
                        h_p3 = rs.getString("PerBundle3");

                        h_i1 = rs.getString("remainder1");
                        h_i2 = rs.getString("remainder2");
                        h_i3 = rs.getString("remainder3");

                        h_itxt1 = rs.getString("remainder_txt1");
                        h_itxt2 = rs.getString("remainder_txt2");
                        h_itxt3 = rs.getString("remainder_txt3");

                        if(h_itxt1 == null) h_itxt1 ="";
                        if(h_itxt2 == null) h_itxt2 ="";
                        if(h_itxt3 == null) h_itxt3 ="";

                        if(h_i1 == null) h_i1 ="";
                        if(h_i2 == null) h_i2 ="";
                        if(h_i3 == null) h_i3 ="";

                        cal1 = h_itxt1 ;
                        cal2 = h_itxt2 ;
                        cal3 = h_itxt3 ;

                        calsum1 = h_i1;
                        calsum2 = h_i2;
                        calsum3 = h_i3;

                        h_good = rs.getString("good_product");
                        h_rust = rs.getString("rust");
                        h_clean = rs.getString("clean");
                        h_poor = rs.getString("poor_package");
                        h_edge = rs.getString("cleft_edge");
                        h_short = rs.getString("short_long");
                        h_crook = rs.getString("crooked");
                        h_note = rs.getString("not_equal");
                        h_notsq = rs.getString("not_square");
                        h_0 = rs.getString("outofstock");
                        h_seq = rs.getString("SEQF");


                        if(h_i1 == null) h_i1 ="";
                        if(h_i2 == null) h_i2 ="";
                        if(h_i3 == null) h_i3 ="";

                        if(h_itxt1 == null) h_itxt1 ="";
                        if(h_itxt2 == null) h_itxt2 ="";
                        if(h_itxt3 == null) h_itxt3 ="";


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

      int sumQTY(String b1,String b2,String b3,String p1,String p2,String p3,String i1,String i2,String i3){

          int r1 = (toInt(b1)*toInt(p1))+toInt(i1);
          int r2 = (toInt(b2)*toInt(p2))+toInt(i2);
          int r3 = (toInt(b3)*toInt(p3))+toInt(i3);

        return r1+r2+r3;
    }

    int toInt(String i){
        if(i == null || i.equals("") || i.equals("NULL")){
            return  0 ;
        }else{
            return Integer.parseInt(i);
        }
    }


    public class SaveOutPut extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(Stock.this,r,Toast.LENGTH_SHORT).show();
         /*   if(gbar==null){
                Intent i = new Intent(Stock.this, OndayActivity.class);
                i.putExtra("cdat",gcdate);
                i.putExtra("gloc",gloc);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(Stock.this, LineData.class);
                String chkput = h_location2;

                if(h_location2.equals("2F") || h_location2.equals("2R")){
                    chkput = h_location;
                }

                if(connectionClass.getUip().equals("192.168.116.222")){
                    chkput = h_location;
                }


                i.putExtra("loc", chkput);
                i.putExtra("loc_desc", getLocText(gbar));

                startActivity(i);
                finish();

            }*/

            onBackPressed();


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String close = "";
                    String upreI1 = isNULL(cal1);
                    String upreI2 = isNULL(cal2);
                    String upreI3 = isNULL(cal3);
                    if(params[1].equals("C")){
                        close = " , confirmBy =  '"+usrHelper.getUserName()+"' ";
                    }

                    if(getEDTC(i1)==null || getEDTC(i1).equals("") || getEDTC(i1).equals("0")){
                        upreI1 = "NULL";
                    }
                    if(getEDTC(i2)==null || getEDTC(i2).equals("") || getEDTC(i2).equals("0")){
                        upreI2 = "NULL";
                    }
                    if(getEDTC(i3)==null || getEDTC(i3).equals("") || getEDTC(i3).equals("0")){
                        upreI3 = "NULL";
                    }

                    String supdate = "update STOCK.dbo.tbl_physicalcount_location " +
                            "set Bundle1 = "+getEDT(b1)+",PerBundle1 = "+getEDT(p1)+" , remainder1 = "+getEDT(i1)+" ,  " +
                            "Bundle2 = "+getEDT(b2)+",PerBundle2 = "+getEDT(p2)+" , remainder2 = "+getEDT(i2)+" , " +
                            "Bundle3 = "+getEDT(b3)+",PerBundle3 = "+getEDT(p3)+" , remainder3 = "+getEDT(i3)+" " +
                            ",remainder_txt1 = "+upreI1+" , remainder_txt2 = "+upreI2+" ,remainder_txt3 = "+upreI3+""+
                            ",QTY = "+sumQTY(getEDT(b1),getEDT(b2),getEDT(b3),getEDT(p1),getEDT(p2),getEDT(p3),getEDT(i1),getEDT(i2),getEDT(i3))+" " +
                            " ,poor_package = "+getEDT(epoor)+",short_long = "+getEDT(eshort)+" " +
                            " ,good_product = "+getEDT(egood)+",rust = "+getEDT(erust)+" " +
                            " ,clean = "+getEDT(eclean)+" ,crooked = "+getEDT(ecrook)+" " +
                            " ,cleft_edge = "+getEDT(eedge)+" ,not_square = "+getEDT(enotsq)+" " +
                            " ,not_equal = "+getEDT(enote)+",UserScan =  '"+usrHelper.getUserName()+"' "+close+" " +
                            " ,ChangeDate = CURRENT_TIMESTAMP " +
                            " , Remark = "+getEDTre(remark)+" , offset = '"+offSet(getEDT(offset))+"' "+
                            "where barcode = '"+h_bar+"' and countDate = '"+h_cdate+"' and confirmBy is null ";



                    PreparedStatement preparedStatement = con.prepareStatement(supdate);
                    preparedStatement.executeUpdate();

                    Log.d("update",supdate);

                    z = "Success";


                }
            } catch (Exception ex) {
                z = ex.getMessage();

            }
            return z;
        }
    }

    public class unLock extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);

            onBackPressed();
           // Toast.makeText(Stock.this,r,Toast.LENGTH_SHORT).show();
         /*   if(gbar==null){
                Intent i = new Intent(Stock.this, OndayActivity.class);
                i.putExtra("cdat",gcdate);
                i.putExtra("gloc",gloc);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(Stock.this, LineData.class);
                String chkput = h_location2;

                if(h_location2.equals("2F") || h_location2.equals("2R")){
                    chkput = h_location;
                }

                if(connectionClass.getUip().equals("192.168.116.222")){
                    chkput = h_location;
                }


                i.putExtra("loc", chkput);
                i.putExtra("loc_desc", getLocText(gbar));

                startActivity(i);
                finish();

            }*/

          //  onBackPressed();


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    String supdate = "update STOCK.dbo.tbl_physicalcount_location " +
                            "set confirmBy = NULL  " +
                            "where barcode = '"+h_bar+"' and countDate = '"+h_cdate+"' ";

                    PreparedStatement preparedStatement = con.prepareStatement(supdate);
                    preparedStatement.executeUpdate();

                   // Log.d("update",supdate);

                    z = "Success";


                }
            } catch (Exception ex) {
                z = ex.getMessage();

            }
            return z;
        }
    }

    String isNULL(String s){
        if(s==null || s=="null"){
            return "NULL";
        }else{
            return "'"+s+"'";
        }

    }

    int cal(TextView v){

        String s = v.getText().toString();
        String input = s;

        if(s.length()>=1){
        if(s.substring(0,1).equals("+"))  {
            input = s.substring(1);
        }
            String numbers[] = input.split("\\+");
            int sum = 0;
            for (String number : numbers) {
                Integer n = Integer.parseInt(number);
                sum += n;
            }
            return  sum;
        }else{
            return 0;
        }

    }

    String calTxt(TextView v){

        String s = v.getText().toString();
        String input = s;
        if(s==null || s.equals("")){
            input =   "0";
        }else{
            if(s.substring(0,1).equals("+"))  {
                input = s.substring(1);
            }
        }


        return  input;
    }

    public void pCal(String i, String itxt, final String idx){

        final Dialog adjdialog = new Dialog(Stock.this);
        //adjdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adjdialog.setTitle("รวมเศษ");
        adjdialog.setContentView(R.layout.dialog_adjust);

        adjdialog.setCancelable(true);
        final TextView tvCal;
        Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt0,btPlus,btDel,svbtn,cnbtn;

        tvCal = adjdialog.findViewById(R.id.tvCal);
       final TextView tvSum = adjdialog.findViewById(R.id.tvSum);

        svbtn = adjdialog.findViewById(R.id.btnQtysv);
        cnbtn = adjdialog.findViewById(R.id.btnCan);

        switch (idx){
            case "1" :  if(getEDT(i1) ==null || getEDT(i1).equals("") || getEDT(i1).equals("NULL")){
                tvSum.setText(""+calsum1); tvCal.setText(""+cal1);
            }else {
                tvSum.setText(""+getEDT(i1));
            }
            if(cal1==null || cal1.equals("")){
                tvCal.setText(""+getEDTC(i1));
            }else{
                tvCal.setText(""+cal1);
            }
                break;
            case "2" : tvCal.setText(""+cal2); if(getEDT(i2) ==null || getEDT(i2)==""){ tvSum.setText(""+calsum2); } else {tvSum.setText(""+getEDT(i2));}
                break;
            case "3" : tvCal.setText(""+cal3); if(getEDT(i3) ==null || getEDT(i3)==""){ tvSum.setText(""+calsum3); } else {tvSum.setText(""+getEDT(i3));}
                break;
        }



        bt1 =adjdialog.findViewById(R.id.bt1);
        bt2 =adjdialog.findViewById(R.id.bt2);
        bt3 =adjdialog.findViewById(R.id.bt3);
        bt4 =adjdialog.findViewById(R.id.bt4);
        bt5 =adjdialog.findViewById(R.id.bt5);
        bt6 =adjdialog.findViewById(R.id.bt6);
        bt7 =adjdialog.findViewById(R.id.bt7);
        bt8 =adjdialog.findViewById(R.id.bt8);
        bt9 =adjdialog.findViewById(R.id.bt9);
        bt0 =adjdialog.findViewById(R.id.bt0);
        btPlus =adjdialog.findViewById(R.id.btPlus);
        btDel =adjdialog.findViewById(R.id.btDel);



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "1");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "2");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "3");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "4");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "5");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "6");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "7");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "8");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "9");
                tvSum.setText(""+cal(tvCal));
            }
        });
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCal.setText(tvCal.getText() + "0");
                tvSum.setText(""+cal(tvCal));

            }
        });
        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String l = tvCal.getText().toString();
                if(l==null || l.equals("")){

                }else{
                    l = l.substring(0,1);
                    if(!l.equals("+")){
                        tvCal.setText(tvCal.getText() + "+");

                    }
                }

                tvSum.setText(""+cal(tvCal));

            }
        });


        /*tvSum.addTextChangedListener(new TextWatcher() {



            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                tvSum.setText();
            }
        });*/

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str=tvCal.getText().toString();
                if (str.length() >=1 ) {
                    str = str.substring(0, str.length() - 1);
                    tvCal.setText(str);
                }

                tvSum.setText(""+cal(tvCal));
            }
        });


        svbtn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {




                        switch (idx){
                            case "1" : cal1 = calTxt(tvCal);  i1.setText(""+cal(tvCal)); calsum1 = String.valueOf(cal(tvCal));
                                break;
                            case "2" : cal2 = calTxt(tvCal); i2.setText(""+cal(tvCal)); calsum2 = String.valueOf(cal(tvCal));
                                break;
                            case "3" : cal3 = calTxt(tvCal); i3.setText(""+cal(tvCal)); calsum3 = String.valueOf(cal(tvCal));
                                break;
                        }

                        adjdialog.dismiss();



            }
        });
        cnbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                adjdialog.dismiss();
            }
        });


        adjdialog.show();

    }

  /*  @Override
    public void onBackPressed() {

     *//*   Intent i = new Intent(Stock.this, OndayActivity.class);
        i.putExtra("cdat",gcdate);
        i.putExtra("gloc",gloc);
        startActivity(i);
        finish();*//*
    }*/



}
