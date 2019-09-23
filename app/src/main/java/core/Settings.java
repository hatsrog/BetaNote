package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Settings {

    private String _pathFile;
    private File _file;

    public Settings(String pathFile)
    {
        this._pathFile = pathFile;
        this._file = new File(pathFile);
    }

    public Map<String, String> getAllSettings()
    {
        Map<String, String> settings = new HashMap<>();

        return settings;
    }

    public void setNode(String updatedKey, String updatedValue)
    {
        Map<String, String> settings = getAllSettings();
        for(String key : settings.keySet())
        {
            if(key.equals(updatedKey))
            {
                // Write new value here
            }
        }
    }

    public String createSettingsNodes()
    {
        String newSettings = "<< \n"+
                                "lastModification:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").toString()+"\n"+
                             ">> \n";
        return  newSettings;
    }
}
