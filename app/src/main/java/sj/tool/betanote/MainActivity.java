package sj.tool.betanote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import core.DataAnalyzer;
import core.FileFinder;
import core.Settings;
import helper.GenericConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_WRITE_PERMISSION = 1;
    SharedPreferences prefs = null;

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true))
        {
            File directory = new File(getFilesDir().toString(), GenericConstants.BETANOTES_DIRECTORY);
            directory.mkdirs();
            prefs.edit().putBoolean("firstrun", false).apply();
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

        prefs = getSharedPreferences(GenericConstants.PACKAGE_NAME, MODE_PRIVATE);
        ScrollView scrollViewLatest = findViewById(R.id.scrollViewLatest);

        // Recherche des fichiers texte
        FileFinder fileFinder = new FileFinder();
        List<String> latest = fileFinder.findLatestNotes(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY);
        LinearLayout linearLayoutLatest = new LinearLayout(this);
        linearLayoutLatest.setOrientation(LinearLayout.VERTICAL);
        int count = 0;

        for (final String latestFiles : latest)
        {
            BufferedReader br = null;
            Settings settings = null;
            String bodyText = "";
            try
            {
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+latestFiles));
                settings = DataAnalyzer.extractSettings(br);
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+latestFiles));
                bodyText = DataAnalyzer.extractBodyText(br, 50);
                count++;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            // Ajout d'un bouton par fichier texte trouvé
            //! Tri les notes de la plus récente à la plus ancienne
            final Button addButtonLatest = new Button(this);
            linearLayoutLatest.addView(addButtonLatest);
            addButtonLatest.setAllCaps(false);
            String buttonContent = settings.getNode("title") + "\n" + bodyText;
            addButtonLatest.setText(buttonContent);
            addButtonLatest.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(MainActivity.this, Bnote.class);
                    intent.putExtra("filename", latestFiles);
                    intent.putExtra("newFile", "false");
                    startActivity(intent);
                }
            });
        }
        scrollViewLatest.addView(linearLayoutLatest);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Bnote.class);
                intent.putExtra("newFile", "true");
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_help)
        {
            Intent intent = new Intent(MainActivity.this, Bnote.class);
            intent.putExtra("filenameAssets", "help.txt");
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
