package core;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> getSettingsMap() { return this.settingsMap; }

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
        settingsMap.put("title", "");
        settingsMap.put("creationDate", Calendar.getInstance().getTime().toString());
        settingsMap.put("lastModification", Calendar.getInstance().getTime().toString());
        settingsMap.put("encrypt", "0");
        settingsMap.put("encryptSalt", "");
    }

    public void setNode(String key, String value)
    {
        if(settingsMap.containsKey(key))
        {
            settingsMap.remove(key);
        }
        settingsMap.put(key, value);
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
}
