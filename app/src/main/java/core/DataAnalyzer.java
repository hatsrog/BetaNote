package core;

import java.io.BufferedReader;
import java.io.IOException;

public final class DataAnalyzer {

    public static String extractBodyText(BufferedReader br, Boolean raw) throws IOException {
        String line;
        boolean endOfSettings = false;
        StringBuilder bodyText = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
            if(endOfSettings)
            {
                if(raw)
                {
                    bodyText.append(line);
                }
                else
                {
                    bodyText.append(line).append("\n");
                }
            }
            if(line.trim().equals(">>"))
            {
                endOfSettings = true;
                continue;
            }
        }
        return bodyText.toString();
    }

    public static String extractBodyText(BufferedReader br, int limiter) throws IOException {
        String line;
        boolean endOfSettings = false;
        StringBuilder bodyText = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
            if(endOfSettings)
            {
                bodyText.append(line).append("\n");
            }
            if(line.trim().equals(">>"))
            {
                endOfSettings = true;
                continue;
            }
            if(bodyText.length() >= limiter)
            {
                return bodyText.substring(0, limiter);
            }
        }
        return bodyText.toString();
    }

    public static String extractSettingsAsString(BufferedReader br) throws IOException {
        String line;
        Boolean endOfSettings = null;
        StringBuilder strSettings = new StringBuilder();
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
                strSettings.append(line).append("\n");
            }
        }
        return strSettings.toString();
    }

    public static Settings extractSettings(BufferedReader br) throws IOException {
        return new Settings(DataAnalyzer.extractSettingsAsString(br));
    }
}
