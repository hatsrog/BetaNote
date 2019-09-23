package core;

import java.text.SimpleDateFormat;

public class Settings {

    public String createSettingsNodes()
    {
        String newSettings = "<<\n"+
                                "lastModification:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").toString()+"\n"+
                             ">>\n";
        return  newSettings;
    }
}
