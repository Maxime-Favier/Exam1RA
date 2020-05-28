package dev.favier.exam1radioamateur;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.NavUtils;

public class Remerciements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remerciements);
    }

    // open f6kgl website
    public void openF6KGL(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f6kgl-f5kff.fr/"));
        startActivity(browserIntent);
    }

    // open garageisep website
    public void openGarageIsep(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://garageisep.com/"));
        startActivity(browserIntent);
    }


}
