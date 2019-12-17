package core;

import java.util.HashMap;
import java.util.Map;

import helper.SettingsConstants;

import static helper.DateTime.getDateTime;

public class Settings {

    private Map<String, String> settingsMap;

    public Settings(String rawSettings)
    {
        convertToMap(rawSettings);
    }

    public Settings()
    {
        this.settingsMap = new HashMap<>();
    }

    private void convertToMap(String strSettings)
    {
        if(settingsMap == null)
        {
            settingsMap = new HashMap<>();
            String[] arrSettings = strSettings.split("\n");
            for(String elementSetting : arrSettings)
            {
                String[] registry = elementSetting.split(":");
                //! Fix characters escape for \n and :
                if(registry.length == 2)
                {
                    settingsMap.put(registry[0], registry[1]);
                }
            }
        }
    }

    public String getSettingsAsString()
    {
        String settingsAsString = "<<\n";
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            settingsAsString += entry.getKey() + ":" + entry.getValue() + "\n";
        }
        settingsAsString += ">>\n";
        return settingsAsString;
    }

    public void createSettingsNodes()
    {
        settingsMap.put(SettingsConstants.TITLE, "");
        settingsMap.put(SettingsConstants.CREATIONDATE, getDateTime());
        settingsMap.put(SettingsConstants.LASTMODIFICATION, getDateTime());
        settingsMap.put(SettingsConstants.ENCRYPT, "0");
        settingsMap.put(SettingsConstants.ENCRYPTSALT, "");
        settingsMap.put(SettingsConstants.BACKGROUNDCOLOR, "");
    }

    public void setNode(String key, Object value)
    {
        settingsMap.remove(key);
        settingsMap.put(key, value.toString());
    }

    public String getNode(String key)
    {
        String returnedValue = null;
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            if(entry.getKey().equals(key))
            {
                returnedValue = entry.getValue();
            }
        }
        return returnedValue;
    }

    public boolean nodeExists(String key)
    {
        boolean exists = false;
        String node = getNode(key);
        if(node != null && !node.equals(""))
        {
            exists = true;
        }
        return exists;
    }
}
