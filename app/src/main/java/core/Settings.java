package core;

import java.util.Calendar;

public class Settings {

    public String createSettingsNodes()
    {
        String newSettings = "<<\n"+
                                "title:\n"+
                                "creationDate:"+ Calendar.getInstance().getTime()+"\n"+
                                "lastModification:"+Calendar.getInstance().getTime()+"\n"+
                             ">>\n";
        return  newSettings;
    }
}
