package sj.tool.betanote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        TextView t2 = findViewById(R.id.textViewSourceLink);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        int id = menuItem.getItemId();

        if(id == R.id.nav_homepage)
        {
            Intent intent = new Intent(About.this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_new_betanote)
        {
            Intent intent = new Intent(About.this, Bnote.class);
            intent.putExtra("newFile", "true");
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(About.this, GeneralSettings.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_help)
        {
            //! Créer un nouvel Intent avec une aide dediée
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(About.this, About.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(About.this, MainActivity.class);
        startActivity(intent);
    }
}
