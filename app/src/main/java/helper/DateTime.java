package helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime
{
    public static final String getDateTime()
    {
        String dateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
