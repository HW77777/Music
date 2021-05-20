package com.example.music;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {
    public static String md5HashCode(InputStream fis){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] buffer=new byte[1024];
            int length=-1;
            while ((length=fis.read(buffer,0,1024))!=-1){
                md.update(buffer,0,length);
            }
            fis.close();
            byte[] md5Bytes=md.digest();
            BigInteger bigInt=new BigInteger(1,md5Bytes);
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String md5HashCode32(InputStream fis){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] buffer=new byte[1024];
            int length=-1;
            while ((length=fis.read(buffer,0,1021))!=-1){
                md.update(buffer,0,length);
            }
            fis.close();
            byte[] md5Bytes=md.digest();
            StringBuffer hexValue=new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val=((int)md5Bytes[i])&0xff;
                if (val<16){
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String md5HashCode(String filePath) throws FileNotFoundException {
        FileInputStream fis=new FileInputStream(filePath);
        System.out.println("md5读取");
        return md5HashCode(fis);
    }
    public static String md5HashCode32(String filePath) throws FileNotFoundException {
        FileInputStream fis=new FileInputStream(filePath);
        return md5HashCode32(fis);
    }
}
