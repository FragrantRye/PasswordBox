package cn.xfz.passwordbox.cryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {
    public static void main(String[] args) throws Exception {
        String s="123";
        System.out.println(s);
        byte[] temp=encrypt(s.getBytes(StandardCharsets.UTF_8),"903465575");
        System.out.println(temp.length+":"+Base64Util.encode(temp));
        temp=decrypt(temp,"903465575");
        System.out.println(new String(temp));
    }

    public static String encrypt(String clear, String password) {
        try {
            return Base64Util.encode(encrypt(clear.getBytes(StandardCharsets.UTF_8), password));
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String secret, String password) {
        try {
            return new String(decrypt(Base64Util.decode(secret), password), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] encrypt(byte[] src, String key) throws Exception {
        if (key == null) {
            throw new Exception("key不满足条件");
        }
        //key字符串的MD5值做为密钥
        byte[] rawKey = MD5Util.getMD5(key.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(src);
    }

    private static byte[] decrypt(byte[] src, String key) throws Exception {
        if (key == null) {
            throw new Exception("key不满足条件");
        }
        //key字符串的MD5值做为密钥
        byte[] rawKey = MD5Util.getMD5(key.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        try {
            return cipher.doFinal(src);
        }
        catch(javax.crypto.BadPaddingException e) {
            throw new Exception("密码错误");
        }
    }
}
