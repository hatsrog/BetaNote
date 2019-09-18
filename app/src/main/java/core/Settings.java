package core;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private String _pathFile;

    public Settings(String pathFile)
    {
        this._pathFile = pathFile;
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

    public void createSettingsNodes()
    {

    }
}
