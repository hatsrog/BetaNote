package sj.tool.betanote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import core.DataAnalyzer;
import core.FileFinder;
import core.Settings;
import helper.GenericConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences prefs = null;
    List<Map<String, String>> allNotes = new ArrayList<>();
    LinearLayout linearLayoutLatest;

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    public void printAllNotes()
    {
        // Recherche des fichiers texte
        FileFinder fileFinder = new FileFinder();
        List<String> latest = fileFinder.findLatestNotes(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY);
        linearLayoutLatest = new LinearLayout(this);
        linearLayoutLatest.setOrientation(LinearLayout.VERTICAL);
        allNotes.clear();

        for (final String latestFiles : latest)
        {
            BufferedReader br = null;
            Settings settings = null;
            String bodyText = "";
            final Map<String, String> note = new HashMap<>();
            try
            {
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+latestFiles));
                settings = DataAnalyzer.extractSettings(br);
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+latestFiles));
                bodyText = DataAnalyzer.extractBodyText(br, 50);

                note.put("settings", settings.getSettingsAsString());
                note.put("body", bodyText);
                note.put("filename", latestFiles);
                allNotes.add(note);
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
            addButtonLatest.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Suppression Note")
                            .setMessage("Etes-vous sûr de vouloir supprimer cette note ?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    linearLayoutLatest.removeView(addButtonLatest);
                                    allNotes.remove(note);
                                    File fileToRemove = new File(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+latestFiles);
                                    fileToRemove.delete();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });
        }
        if(linearLayoutLatest.getChildCount() == 0)
        {
            TextView txtNoNoteFound = new TextView(this);
            txtNoNoteFound.setTextSize(30);
            txtNoNoteFound.setText("Aucune note trouvée, vous pouvez en créer une en appuyant sur le +");
            linearLayoutLatest.addView(txtNoNoteFound);
        }
    }

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

        final ScrollView scrollViewLatest = findViewById(R.id.scrollViewLatest);
        printAllNotes();
        if(scrollViewLatest.getChildCount() > 0)
            scrollViewLatest.removeAllViews();
        scrollViewLatest.addView(linearLayoutLatest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(GenericConstants.PACKAGE_NAME, MODE_PRIVATE);
        final ScrollView scrollViewLatest = findViewById(R.id.scrollViewLatest);
        final EditText editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(scrollViewLatest.getChildCount() > 0)
                    scrollViewLatest.removeAllViews();
            }

            private Timer timer=new Timer();
            private final long DELAY = 500;

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask()
                        {
                            @Override
                            public void run()
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        if(editTextSearch.getText().toString().trim().length() >= 2)
                                        {
                                            linearLayoutLatest = new LinearLayout(MainActivity.this);
                                            linearLayoutLatest.setOrientation(LinearLayout.VERTICAL);
                                            for(final Map<String, String> note : allNotes)
                                            {
                                                Settings settings = new Settings(note.get("settings"));
                                                String bodyText = note.get("body");
                                                if(containsIgnoreCase(settings.getNode("title"), editTextSearch.getText().toString()) || containsIgnoreCase(bodyText, editTextSearch.getText().toString()))
                                                {
                                                    final Button addButtonLatest = new Button(MainActivity.this);
                                                    linearLayoutLatest.addView(addButtonLatest);
                                                    addButtonLatest.setAllCaps(false);
                                                    String buttonContent = settings.getNode("title") + "\n" + bodyText;
                                                    addButtonLatest.setText(buttonContent);
                                                    addButtonLatest.setOnClickListener(new View.OnClickListener()
                                                    {
                                                        public void onClick(View v)
                                                        {
                                                            Intent intent = new Intent(MainActivity.this, Bnote.class);
                                                            intent.putExtra("filename", note.get("filename"));
                                                            intent.putExtra("newFile", "false");
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }
                                            scrollViewLatest.addView(linearLayoutLatest);
                                        }
                                        else
                                        {
                                            printAllNotes();
                                            scrollViewLatest.addView(linearLayoutLatest);
                                        }
                                    }
                                });
                            }
                        },
                        DELAY
                );
            }
        });

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

        if (id == R.id.nav_new_betanote)
        {
            Intent intent = new Intent(MainActivity.this, Bnote.class);
            intent.putExtra("newFile", "true");
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {

        }
        else if (id == R.id.nav_help)
        {
            //! Créer un nouvel Intent avec une aide dediée
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
