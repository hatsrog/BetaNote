package core;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Settings {

    private Map<String, String> settingsMap;
    private String _raw;

    private Settings(String rawSettings)
    {
        this._raw = rawSettings;
        //! Convert raw string settings to Map<String, String>
    }

    public Settings()
    {
        this.settingsMap = new HashMap<>();
    }

    public String getSettingsAsString()
    {
        String settingsAsString = "<<\n";
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            settingsAsString += entry.getKey() + ":" + entry.getValue() + "\n";
        }
        settingsAsString += ">>";
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
}
