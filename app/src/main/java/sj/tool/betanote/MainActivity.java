package sj.tool.betanote;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import core.FileFinder;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_PERMISSION = 1;
    SharedPreferences prefs = null;

    @Override
    protected void onResume() {
        super.onResume();
        //prefs.edit().clear().commit();
        if (prefs.getBoolean("firstrun", true))
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_PERMISSION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }
            File directory = new File(getFilesDir().toString(), "betanote_files");
            boolean hasCreated = directory.mkdirs();
            File init = new File(getFilesDir().toString()+"/betanote_files", "initiator.txt");
            try {
                init.createNewFile();
                FileWriter writer = new FileWriter(init);
                writer.append("Ce fichier marche, trop bien");
                writer.flush();
                writer.close();
                prefs.edit().putBoolean("firstrun", false).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("sj.tool.betanote", MODE_PRIVATE);
        Button btnNew = findViewById(R.id.btnNew);
        Button btnOpen = findViewById(R.id.btnOpen);
        Button btnHelp = findViewById(R.id.btnHelp);
        ScrollView scrollViewLatest = findViewById(R.id.scrollViewLatest);

        // Recherche des fichiers texte
        FileFinder fileFinder = new FileFinder();
        List<String> latest = fileFinder.findLatestNotes(getFilesDir().toString() + "/betanote_files");
        LinearLayout linearLayoutLatest = new LinearLayout(this);
        linearLayoutLatest.setOrientation(LinearLayout.VERTICAL);

        // Ajout d'un bouton par fichier texte trouvé dans les assets
        for (String latestFiles : latest) {
            final Button addButtonLatest = new Button(this);
            addButtonLatest.setText(latestFiles);
            linearLayoutLatest.addView(addButtonLatest);
            addButtonLatest.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(MainActivity.this, Bnote.class);
                    intent.putExtra("filename", addButtonLatest.getText());
                    startActivity(intent);
                }
            });
        }
        scrollViewLatest.addView(linearLayoutLatest);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Bnote.class);
                startActivity(intent);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Bnote.class);
                intent.putExtra("filenameAssets", "help.txt");
                startActivity(intent);
            }
        });
    }
}
