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

    private void convertToMap(String strSettings)
    {
        //! Convert to Map
        if(settingsMap == null)
        {
            //! Parse and may be add pipes between settings node
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
    }

    public void setNode(String key, String value)
    {
        if(settingsMap.containsKey(key))
        {
            settingsMap.remove(key);
            settingsMap.put(key, value);
        }
    }

    public String getNode(String key)
    {
        String returnedValue = null;
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            if(entry.getKey() == key)
            {
                returnedValue = entry.getValue();
            }
        }
        return returnedValue;
    }
}
