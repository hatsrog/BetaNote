package sj.tool.betanote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import core.DataAnalyzer;
import core.Settings;
import helper.GenericConstants;
import helper.SettingsConstants;

public class Bnote extends AppCompatActivity {

    EditText editTextTitle = null;
    EditText editTextBnote = null;
    Settings settings = null;
    String newFile = "";
    String filename = "";

    @Override
    public void onBackPressed()
    {
        saveNote();
        super.onBackPressed();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bnote);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        editTextBnote = findViewById(R.id.editTextBnote);
        editTextTitle = findViewById(R.id.editTextTitle);
        filename = intent.getStringExtra("filename");
        newFile = intent.getStringExtra("newFile");

        if (filename != null) {
            editTextBnote.setText(null);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+filename));
                String bodyText = DataAnalyzer.extractBodyText(br);
                if(bodyText != null)
                {
                    editTextBnote.setText(bodyText);
                }
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+filename));
                settings = new Settings(DataAnalyzer.extractSettingsAsString(br));
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
            finally
            {
                try
                {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(settings != null)
        {
            String getTitle = settings.getNode(SettingsConstants.KEY_TITLE);
            if (getTitle != null) {
                editTextTitle.setText(getTitle);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_nosave) {
            return true;
        }
        else if(id == R.id.action_encrypt)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        if(editTextBnote != null)
        {
            try {
                if(newFile.equals("true")) {

                    Settings settingsNewFile = new Settings();
                    settingsNewFile.createSettingsNodes();
                    if(editTextTitle.getText().toString().trim().length() > 0)
                    {
                        settingsNewFile.setNode(SettingsConstants.KEY_TITLE, editTextTitle.getText().toString());
                    }
                    if(editTextBnote.getText().toString().trim().length() > 0)
                    {
                        new File(getFilesDir().toString(), GenericConstants.BETANOTES_DIRECTORY);
                        String filename = Math.random() * 10000 + 1+".txt";
                        File init = new File(getFilesDir().toString()+"/"+GenericConstants.BETANOTES_DIRECTORY, filename);
                        init.createNewFile();
                        String createdSettings = settingsNewFile.getSettingsAsString();
                        FileWriter writer = new FileWriter(init);
                        writer.append(createdSettings);
                        writer.append(editTextBnote.getText());
                        writer.flush();
                        writer.close();
                    }
                }
                else if(newFile.equals("false"))
                {
                    File init = new File(getFilesDir().toString()+"/"+GenericConstants.BETANOTES_DIRECTORY, filename);
                    if(settings != null)
                    {
                        settings.setNode(SettingsConstants.KEY_LASTMODIFICATION, Calendar.getInstance().getTime().toString());
                    }
                    if(editTextTitle != null)
                    {
                        settings.setNode(SettingsConstants.KEY_TITLE, editTextTitle.getText().toString());
                    }
                    if(editTextBnote != null)
                    {
                        String storeText = editTextBnote.getText().toString();
                        FileWriter writer = new FileWriter(init);
                        writer.write("");
                        writer.append(settings.getSettingsAsString());
                        writer.append(storeText);
                        writer.flush();
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
