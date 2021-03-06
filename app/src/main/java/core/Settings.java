package core;

import java.util.HashMap;
import java.util.Map;

import helper.SettingsConstants;
import helper.TypoConstants;

import static helper.DateTime.getUnixTime;

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
        StringBuilder settingsAsString = new StringBuilder("<<\n");
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            settingsAsString.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        settingsAsString.append(">>\n");
        return settingsAsString.toString();
    }

    public void createSettingsNodes()
    {
        settingsMap.put(SettingsConstants.TITLE, "");
        settingsMap.put(SettingsConstants.CREATIONDATE, getUnixTime());
        settingsMap.put(SettingsConstants.LASTMODIFICATION, getUnixTime());
        settingsMap.put(SettingsConstants.ENCRYPT, "0");
        settingsMap.put(SettingsConstants.ENCRYPTSALT, "");
        settingsMap.put(SettingsConstants.BACKGROUNDCOLOR, "");
        settingsMap.put(SettingsConstants.FILEVERSION, "0.1");
    }

    public void createTypoNodes()
    {
        settingsMap.put(TypoConstants.BOLD, "0");
        settingsMap.put(TypoConstants.ITALIC, "0");
        settingsMap.put(TypoConstants.UNDERLINE, "0");
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

    public boolean isTrue(String key)
    {
        boolean isTrue = false;
        if(getNode(key).equals("1") || getNode(key).equals("true"))
        {
            isTrue = true;
        }
        return isTrue;
    }

    public boolean isFalse(String key)
    {
        boolean isFalse = false;
        if(getNode(key).equals("0") || getNode(key).equals("false"))
        {
            isFalse = true;
        }
        return isFalse;
    }
}
