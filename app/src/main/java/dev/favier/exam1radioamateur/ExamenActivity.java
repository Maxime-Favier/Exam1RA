package dev.favier.exam1radioamateur;

import android.view.Menu;
import android.view.MenuInflater;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.zip.Inflater;

public class ExamenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // setup top Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.examen_menu, menu);
        return true;
    }
}
