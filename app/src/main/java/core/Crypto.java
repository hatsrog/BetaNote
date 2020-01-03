package core;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto
{
    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getSalt() throws Exception {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[20];
        sr.nextBytes(salt);
        return java.util.Base64.getEncoder().encodeToString(salt);
    }
}
