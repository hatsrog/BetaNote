package helper;

import java.nio.charset.Charset;
import java.util.Random;

public class Randomness
{
    public static String randomString(int length)
    {
        String randomString = null;
        if(length > 0)
        {
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));
        }
        else
        {
            return null;
        }
        return randomString;
    }
}
