package sj.tool.betanote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import core.FileFinder;
import core.Settings;

public class Bnote extends AppCompatActivity {

    EditText editTextTitle = null;
    EditText editTextBnote = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bnote);
        Intent intent = getIntent();

        Button btnExit = findViewById(R.id.btnExit);
        Button btnSave = findViewById(R.id.btnSave);
        editTextBnote = findViewById(R.id.editTextBnote);
        editTextTitle = findViewById(R.id.editTextTitle);
        final String filenameAssets = intent.getStringExtra("filenameAssets");
        String filename = intent.getStringExtra("filename");
        final String newFile = intent.getStringExtra("newFile");

        if (filenameAssets != null) {
            editTextBnote.setText(null);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open(filenameAssets)));

                extractData(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
        }
        if (filename != null) {
            editTextBnote.setText(null);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/betanote_files/"+filename));
                extractData(br);
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
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bnote.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextBnote != null)
                {
                    File directory = new File(getFilesDir().toString(), "betanote_files");
                    String filename = Math.random() * 10000 + 1+".txt";
                    File init = new File(getFilesDir().toString()+"/betanote_files", filename);
                    try {
                        if(newFile.equals("true")) {
                            Settings settings = new Settings();
                            init.createNewFile();
                            settings.createSettingsNodes();
                            if(editTextTitle != null)
                            {
                                settings.setNode("title", editTextTitle.getText().toString());
                            }
                            String createdSettings = settings.getSettingsAsString();
                            FileWriter writer = new FileWriter(init);
                            writer.append(createdSettings);
                            writer.append(editTextBnote.getText());
                            writer.flush();
                            writer.close();
                        }
                        else if(newFile.equals("false"))
                        {
                            //! Ouvrir le fichier et y remplacer par les modifications
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void extractData(BufferedReader br) throws IOException {
        String line;
        Boolean endOfSettings = false;
        String strsettings = "";
        Settings settings = null;
        StringBuilder text = new StringBuilder();

        while ((line = br.readLine()) != null) {
            if(line.trim().equals(">>"))
            {
                endOfSettings = true;
                continue;
            }
            if(!endOfSettings)
            {
                if(!line.trim().equals("<<"))
                {
                    strsettings += line + "\n";
                }
            }
            else
            {
                text.append(line);
                text.append('\n');
                editTextBnote.append(line+"\n");
            }
        }
        if(strsettings != null)
        {
            settings = new Settings(strsettings);
            String getTitle = settings.getNode("title");
            if(getTitle != null)
            {
                editTextTitle.setText(getTitle);
            }
        }
    }
}
