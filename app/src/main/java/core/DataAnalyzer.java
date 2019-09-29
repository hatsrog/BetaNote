package core;

import java.util.Map;

public final class DataAnalyzer {

    public static String extractBodyText(String raw)
    {
        //! Separate settings from body text
        String bodyText = "";

        return bodyText;
    }

    public static Map<String, String> extractSettings(String raw)
    {
        //! Separate settings from body text
        Settings settings = new Settings(raw);
        return settings.getSettingsMap();
    }
}
