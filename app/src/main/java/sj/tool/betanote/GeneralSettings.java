package sj.tool.betanote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import core.FileFinder;
import helper.GenericConstants;

public class GeneralSettings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generalsettings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void deleteAllNotes(View view)
    {
        FileFinder fileFinder = new FileFinder();
        List<String> allNotes = fileFinder.findLatestNotes(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY);
        if(allNotes.size() > 0)
        {
            for(String note : allNotes)
            {
                File fileToRemove = new File(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+note);
                fileToRemove.delete();
            }
        }
        else
        {
            Toast.makeText(this, "Aucune note à supprimer", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        int id = menuItem.getItemId();

        if(id == R.id.nav_homepage)
        {
            Intent intent = new Intent(GeneralSettings.this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_new_betanote)
        {
            Intent intent = new Intent(GeneralSettings.this, Bnote.class);
            intent.putExtra("newFile", "true");
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(GeneralSettings.this, GeneralSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }
        else if (id == R.id.nav_help)
        {
            //! Créer un nouvel Intent avec une aide dediée
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(GeneralSettings.this, About.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
