package cn.xfz.passwordbox.cryption;

import java.security.MessageDigest;

public class MD5Util {
    public static String getMD5String(byte[] data) {
        return toHex(getMD5(data));
    }

    public static byte[] getMD5(byte[] data){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data);
            return bytes;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }
}
