package com.example.stockrecheck.Plan;

import android.os.Bundle;

import com.example.stockrecheck.Cstock;
import com.example.stockrecheck.State;
import com.example.stockrecheck.adapter.RecyclerAdapter;
import com.example.stockrecheck.api.RetrofitClient;
import com.example.stockrecheck.dao.PlanItems;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stockrecheck.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<PlanItems> planList;
    State state;
    Spinner spin ;
    String[] lgorts = {""};
    String[] unique;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = new State();
        setContentView(R.layout.activity_plan_list_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("เลขที่แผนนับ : "+state.getPickedPlan());
        setSupportActionBar(toolbar);
        spin = (Spinner) findViewById(R.id.spinner_lgort);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(this,planList);
        recyclerView.setAdapter(recyclerAdapter);

        Call<List<PlanItems>> call = RetrofitClient.getInstance().getMyApi().listPlanDoc(state.getPickedPlan());

        call.enqueue(new Callback<List<PlanItems>>() {
            @Override
            public void onResponse(Call<List<PlanItems>> call, Response<List<PlanItems>> response) {
                planList = response.body();
                Log.d("TAG","Response = "+response);
//                Log.v("REQ",response.raw().toString());
                recyclerAdapter.setMovieList(planList);
                lgorts = new String[planList.size()];

                for (int i = 0; i < planList.size(); i++) {

                    lgorts[i]  = planList.get(i).getLocation();

                }

                 unique = new HashSet<String>(Arrays.asList(lgorts)).toArray(new String[0]);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlanListItem.this, android.R.layout.simple_spinner_item, unique);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                spin.setOnItemSelectedListener(PlanListItem.this);
            }

            @Override
            public void onFailure(Call<List<PlanItems>> call, Throwable t) {
                Log.d("TAG","Fail Response = "+t.toString());
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("SSSS", unique[position]);



        recyclerAdapter = new RecyclerAdapter(this,planList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
