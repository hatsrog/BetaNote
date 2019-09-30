package core;

import java.io.BufferedReader;
import java.io.IOException;

public final class DataAnalyzer {

    public static String extractBodyText(BufferedReader br) throws IOException {
        String line;
        Boolean endOfSettings = false;
        String bodyText = "";
        while ((line = br.readLine()) != null)
        {
            if(endOfSettings)
            {
                bodyText += line + "\n";
            }
            if(line.trim().equals(">>"))
            {
                endOfSettings = true;
                continue;
            }
        }
        return bodyText;
    }

    public static String extractSettings(BufferedReader br) throws IOException {
        String line;
        Boolean endOfSettings = null;
        String strSettings = "";
        while ((line = br.readLine()) != null)
        {
            if(line.trim().equals("<<"))
            {
                endOfSettings = false;
            }
            if(line.trim().equals(">>"))
            {
                break;
            }
            if(!endOfSettings)
            {
                strSettings += line +"\n";
            }
        }
        return strSettings;
    }
}
