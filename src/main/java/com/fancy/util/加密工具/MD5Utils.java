package com.fancy.util.加密工具;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     *
     * @Title: 32位大写加密
     * @Description: 根据不同编码进行MD5转换
     * @param @param s
     * @param @param encodingType
     * @param @return
     * @return String
     * @author tanglei
     * @throws
     */
    public final static String encrypt32(String s,String encodingType) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            // 按照相应编码格式获取byte[]
            byte[] btInput = s.getBytes(encodingType);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * @Description:16位加密
     * @author:liuyc
     * @time:2016年5月23日 上午11:15:33
     */
    public static String encrypt16(String s,String encodingType) {
        return encrypt32(s,encodingType).substring(8, 24);
    }

    public static void main(String[] args) {
        System.out.printf(encrypt16("你好,Fancy","utf-8"));
    }
}