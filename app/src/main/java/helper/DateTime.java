package helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime
{
    public static String getDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getDateTime(String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(new Date());
    }
}
