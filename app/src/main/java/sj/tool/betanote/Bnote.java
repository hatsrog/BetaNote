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

import core.FileFinder;
import core.Settings;

public class Bnote extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bnote);
        Intent intent = getIntent();

        Button btnExit = findViewById(R.id.btnExit);
        Button btnSave = findViewById(R.id.btnSave);
        final EditText editTextBnote = findViewById(R.id.editTextBnote);
        String filenameAssets = intent.getStringExtra("filenameAssets");
        String filename = intent.getStringExtra("filename");
        final String newFile = intent.getStringExtra("newFile");

        if (filenameAssets != null) {
            editTextBnote.setText(null);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open(filenameAssets)));

                // do reading, usually loop until end of file reading
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    editTextBnote.append(mLine + "\n");
                }
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
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(getFilesDir().toString() + "/betanote_files/"+filename));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                    editTextBnote.append(line+"\n");
                }
                br.close();
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
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
                    Settings settings = new Settings(getFilesDir().toString()+"/betanote_files/"+filename);
                    File init = new File(getFilesDir().toString()+"/betanote_files", filename);
                    try {
                        if(newFile.equals("true")) {
                            init.createNewFile();
                            String createdSettings = settings.createSettingsNodes();
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
}
