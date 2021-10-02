package com.example.stockrecheck;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Webview extends AppCompatActivity {

    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        connectionClass = new ConnectionClass();

        WebView webView = (WebView) findViewById(R.id.webv);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if(connectionClass.getUip().equals("192.168.116.222")){
            webView.loadUrl("http://192.168.116.222/wh/physicalcount/frmcountlist.aspx");
        }else{
            webView.loadUrl("http://wh.zubbsteel.com/physicalcount/frmcountlist.aspx");
        }

    }

}
