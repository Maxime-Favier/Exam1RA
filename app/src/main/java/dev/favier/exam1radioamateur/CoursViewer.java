package dev.favier.exam1radioamateur;

import android.content.res.Resources;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class CoursViewer extends AppCompatActivity {

    WebView courWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_viewer);
        setupControl();
    }

    private void setupControl(){
        courWebView = findViewById(R.id.courWebView);
        // set javascript enable
        WebSettings webSettings = courWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // file:///android_asset/html/mol_error.html
        courWebView.loadUrl("file:///android_asset/html/cours.html");
    }
}