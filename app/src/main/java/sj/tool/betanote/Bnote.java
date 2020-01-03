package sj.tool.betanote;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import core.DataAnalyzer;
import core.Settings;
import helper.GenericConstants;
import helper.SettingsConstants;
import yuku.ambilwarna.AmbilWarnaDialog;

import static core.Crypto.decrypt;
import static core.Crypto.encrypt;
import static core.Crypto.getSalt;
import static helper.DateTime.getUnixTime;

public class Bnote extends AppCompatActivity {

    EditText editTextTitle = null;
    EditText editTextBnote = null;
    Settings settings = null;
    String newFile = "";
    String filename = "";
    String password = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        ImageButton buttonReturn = findViewById(R.id.imageButtonSave);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId());
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        editTextBnote = findViewById(R.id.editTextBnote);
        editTextTitle = findViewById(R.id.editTextTitle);
        filename = intent.getStringExtra("filename");
        newFile = intent.getStringExtra("newFile");

        buttonReturn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_bold:
                                Toast.makeText(Bnote.this, "Bold", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.action_italic:
                                Toast.makeText(Bnote.this, "Italic", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.action_underline:
                                Toast.makeText(Bnote.this, "Underline", Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });

        if (filename != null) {
            editTextBnote.setText(null);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+filename));
                settings = new Settings(DataAnalyzer.extractSettingsAsString(br));
                br = new BufferedReader(new FileReader(getFilesDir().toString() + "/"+ GenericConstants.BETANOTES_DIRECTORY +"/"+filename));

                if(settings.getNode(SettingsConstants.ENCRYPT) != null && settings.getNode(SettingsConstants.ENCRYPT).equals("1"))
                {
                    final String bodyText = DataAnalyzer.extractBodyText(br, true);
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    final AlertDialog builder = new AlertDialog.Builder(this)
                        .setTitle("Mot de passe")
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Annuler", null)
                        .setView(input)
                        .show();

                    Button dialogPositive = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button dialogNegative = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

                    dialogPositive.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            String localPassword = input.getText().toString().trim();
                            if(!localPassword.equals(""))
                            {
                                try
                                {
                                    PBEKeySpec spec = new PBEKeySpec(localPassword.toCharArray(), settings.getNode(SettingsConstants.ENCRYPTSALT).getBytes(), 1000, 256);
                                    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                                    byte[] hash = skf.generateSecret(spec).getEncoded();

                                    byte[] decoded = Base64.getDecoder().decode(bodyText);
                                    byte[] decryptedData = decrypt(hash,decoded);
                                    String txtDecrypted = new String(decryptedData, "UTF-8");
                                    if(!txtDecrypted.equals(""))
                                    {
                                        editTextBnote.setText(txtDecrypted);
                                        password = localPassword;
                                        builder.dismiss();
                                    }
                                }
                                catch (NoSuchAlgorithmException e)
                                {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                catch (Exception e)
                                {
                                    // En cas de mauvais mot de passe
                                    Toast.makeText(Bnote.this, "Mauvais mot de passe", Toast.LENGTH_LONG).show();
                                    input.setText("");
                                }
                            }
                        }
                    });

                    dialogNegative.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(Bnote.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    builder.setOnCancelListener(new AlertDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Intent intent = new Intent(Bnote.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    final String bodyText = DataAnalyzer.extractBodyText(br, false);
                    editTextBnote.setText(bodyText);
                }
                if(settings.nodeExists(SettingsConstants.BACKGROUNDCOLOR))
                {
                    try
                    {
                        int backgroundcolor = Integer.parseInt(settings.getNode(SettingsConstants.BACKGROUNDCOLOR));
                        editTextBnote.setBackgroundColor(backgroundcolor);
                    }
                    catch(NumberFormatException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                }
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
            if(settings.nodeExists(SettingsConstants.TITLE))
            {
                editTextTitle.setText(settings.getNode(SettingsConstants.TITLE));
            }
        }
        else
        {
            settings = new Settings();
            settings.createSettingsNodes();
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

        if (id == R.id.action_nosave)
        {
            Intent intent = new Intent(Bnote.this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_encrypt)
        {
            String encryptNode = settings.getNode(SettingsConstants.ENCRYPT);
            if(encryptNode == null)
            {
                settings.setNode(SettingsConstants.ENCRYPT, "0");
                encryptNode = "0";
            }
            if(encryptNode.equals("0"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Mot de passe");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password = input.getText().toString().trim();
                        if(!password.equals(""))
                        {
                            try
                            {
                                settings.setNode(SettingsConstants.ENCRYPTSALT, getSalt());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            settings.setNode(SettingsConstants.ENCRYPT, "1");
                            Toast.makeText(Bnote.this, "Cryptage activé", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
            else if(encryptNode.equals("1"))
            {
                settings.setNode(SettingsConstants.ENCRYPT, "0");
                Toast.makeText(Bnote.this, "Cryptage désactivé", Toast.LENGTH_LONG).show();
            }
        }
        else if(id == R.id.action_backgroundcolor)
        {
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    editTextBnote.setBackgroundColor(color);
                    settings.setNode(SettingsConstants.BACKGROUNDCOLOR, color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    // cancel was selected by the user
                }

            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveNote() {
        if(editTextBnote != null)
        {
            try {
                String txtBNote = editTextBnote.getText().toString();
                if(settings != null && settings.getNode(SettingsConstants.ENCRYPT).equals("1"))
                {
                    String txtToEncrypt = editTextBnote.getText().toString();
                    if(settings.nodeExists(SettingsConstants.ENCRYPTSALT) && !password.equals(""))
                    {
                        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), settings.getNode(SettingsConstants.ENCRYPTSALT).getBytes(), 1000, 256);
                        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                        byte[] key = skf.generateSecret(spec).getEncoded();

                        byte[] bytes = txtToEncrypt.getBytes("UTF-8");
                        byte[] encryptedData = encrypt(key,bytes);
                        txtBNote = java.util.Base64.getEncoder().encodeToString(encryptedData);
                    }
                }

                if(newFile.equals("true")) {

                    if(settings != null)
                    {
                        if(editTextTitle.getText().toString().trim().length() > 0)
                        {
                            settings.setNode(SettingsConstants.TITLE, editTextTitle.getText().toString());
                        }
                        if(editTextBnote.getText().toString().trim().length() > 0)
                        {
                            new File(getFilesDir().toString(), GenericConstants.BETANOTES_DIRECTORY);
                            String filename = getUnixTime()+".txt";
                            File init = new File(getFilesDir().toString()+"/"+GenericConstants.BETANOTES_DIRECTORY, filename);
                            init.createNewFile();
                            String createdSettings = settings.getSettingsAsString();
                            FileWriter writer = new FileWriter(init);
                            writer.append(createdSettings);
                            writer.append(txtBNote);
                            writer.flush();
                            writer.close();
                        }
                    }
                }
                else if(newFile.equals("false"))
                {
                    File init = new File(getFilesDir().toString()+"/"+GenericConstants.BETANOTES_DIRECTORY, filename);
                    if(settings != null)
                    {
                        settings.setNode(SettingsConstants.LASTMODIFICATION, getUnixTime());
                    }
                    if(editTextTitle != null)
                    {
                        settings.setNode(SettingsConstants.TITLE, editTextTitle.getText().toString());
                    }
                    if(editTextBnote != null)
                    {
                        FileWriter writer = new FileWriter(init);
                        writer.write("");
                        writer.append(settings.getSettingsAsString());
                        writer.append(txtBNote);
                        writer.flush();
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
