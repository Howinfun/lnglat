package com.hyf.lnglat.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Howinfun
 * @desc
 * @date 2019/7/31
 */
public class GeoCode {
    public static final GeoCode INSTANCE = new GeoCode();
    private static Map<Integer,double[]> geoHash;
    private GeoCode(){
        geoHash = new LinkedHashMap<>();
        geoHash.put(12,new double[]{0.000037,0.000019});
        geoHash.put(11,new double[]{0.000149,0.000149});
        geoHash.put(10,new double[]{0.0012,0.000595});
        geoHash.put(9,new double[]{0.0048,0.0048});
        geoHash.put(8,new double[]{0.0382,0.019});
        geoHash.put(7,new double[]{0.1529,0.1524});
        geoHash.put(6,new double[]{1.2,0.6094});
        geoHash.put(5,new double[]{4.9,4.9});
        geoHash.put(4,new double[]{39.1,19.5});
        geoHash.put(3,new double[]{156.5,156});
        geoHash.put(2,new double[]{1252.3,624.1});
        geoHash.put(1,new double[]{5009.4,4992.6});
    }

    /**
     * 获取GeoCode
     * @param range 范围，单位为km
     * @return
     */
    public Integer getGeoCode(Integer range){
        Integer geoCode = 1;
        Set<Integer> keys = geoHash.keySet();
        for (Integer key : keys) {
            double[] value = geoHash.get(key);
            if (value[1]> range && value[0] > range){
                geoCode = key;
                break;
            }
        }

        return geoCode;
    }

    public static void main(String[] args) {
        System.out.println(GeoCode.INSTANCE.getGeoCode(1));
    }
}

