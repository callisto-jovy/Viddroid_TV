package net.bplaced.abzzezz.videodroid.util.crypto;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES {
    public static String encrypt(String strToEncrypt, String key, String iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            Log.e("AES", "Error while encrypting" + e.getLocalizedMessage());
        }
        return null;
    }
}
