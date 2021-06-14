package cn.xfz.passwordbox.cryption;

import android.util.Base64;

public class Base64Util {
    public static String encode(byte[] data){
        return new String(Base64.encode(data, Base64.DEFAULT));
    }
    public static byte[] decode(String data){
        return Base64.decode(data, Base64.DEFAULT);
    }
}
