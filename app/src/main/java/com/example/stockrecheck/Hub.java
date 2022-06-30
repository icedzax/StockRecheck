package com.example.stockrecheck;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class Hub extends AppCompatActivity {
    Lang lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        lang = new Lang();
        Button stock,loc;

        stock = (Button)findViewById(R.id.stock);
        loc = (Button)findViewById(R.id.location);
        stock.setText(lang.map.get("stock"));
        loc.setText(lang.map.get("move"));
        stock.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Hub.this, MainActivity.class);
                startActivity(i);
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Hub.this, StockMmt.class);
                startActivity(i);
            }
        });


    }

}
