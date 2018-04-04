
package com.fancy.util.地图工具;

import java.text.DecimalFormat;

/**
 * 地图工具类<br>
 *
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class MapUtils {

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1Str 第一点经度
     * @param lat1Str  第一点纬度
     * @param long2Str 第二点经度
     * @param lat2Str  第二点纬度
     * @return 返回距离 单位：米
     */
    public static double getDistance(String long1Str, String lat1Str, String long2Str, String lat2Str) throws Exception {
        double a, b, R;
        R = 6378137; // 地球半径
        double d = 0;
        try {
            double long1 = Double.parseDouble(long1Str.toString());
            double lat1 = Double.parseDouble(lat1Str.toString());
            double long2 = Double.parseDouble(long2Str.toString());
            double lat2 = Double.parseDouble(lat2Str.toString());
            lat1 = lat1 * Math.PI / 180.0;
            lat2 = lat2 * Math.PI / 180.0;
            a = lat1 - lat2;
            b = (long1 - long2) * Math.PI / 180.0;
            double sa2, sb2;
            sa2 = Math.sin(a / 2.0);
            sb2 = Math.sin(b / 2.0);
            d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
            String format = new DecimalFormat("#").format(d);
            d = Double.valueOf(format);
            return d;
        } catch (Exception e) {
            throw new Exception("经纬度距离计算错误");
        }
    }

}