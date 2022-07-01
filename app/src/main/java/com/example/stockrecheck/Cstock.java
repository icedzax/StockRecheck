package com.example.stockrecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockrecheck.Plan.PlanListItem;
import com.example.stockrecheck.api.RetrofitClient;
import com.example.stockrecheck.dao.Results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class Cstock  extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    State state;
    Button btnPick,btnOut;
    TextView txt_remark;
    List<Results> fetchList;
    String[] users = {""};
    Spinner spin;
    Map<String, String> mapPlan = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cstock);

        state = new State();
        spin = (Spinner) findViewById(R.id.spinnerr);
        txt_remark = (TextView) findViewById(R.id.txt_remark);
        btnPick = (Button) findViewById(R.id.btnPick);
        btnOut = (Button) findViewById(R.id.btnOut);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlan();
                Intent i = new Intent(Cstock.this, PlanListItem.class);
                startActivity(i);
//                finish();
            }
        });
        getPlan();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), "Remark: " + mapPlan.get(users[position]), Toast.LENGTH_SHORT).show();
        txt_remark.setText(mapPlan.get(users[position]));
        state.setPickedPlan(users[position]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code

    }

    private void getPlan() {
        Call<List<Results>> call = RetrofitClient.getInstance().getMyApi().getPlanList();
        call.enqueue(new Callback<List<Results>>() {
            @Override
            public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {
                 fetchList = response.body();

                for (int i = 0; i < fetchList.size(); i++) {

                    mapPlan.put(fetchList.get(i).getPlan_doc(),fetchList.get(i).getPlan_desc());

                }
                users = new String[mapPlan.size()];
                users = mapPlan.keySet().toArray(new String[0]);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Cstock.this, android.R.layout.simple_spinner_item, users);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                spin.setOnItemSelectedListener(Cstock.this);
            }

            @Override
            public void onFailure(Call<List<Results>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has o ccured", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
