package helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime
{
    public static String getUnixTime()
    {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static String getDateTime(long unixTime)
    {
        Date date = new java.util.Date(unixTime*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
        return sdf.format(date);
    }
}
