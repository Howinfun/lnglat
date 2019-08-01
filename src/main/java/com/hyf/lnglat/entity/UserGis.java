package com.hyf.lnglat.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Howinfun
 * @desc
 * @date 2019/7/31
 */
@Data
@ToString
public class UserGis {
    private Integer id;
    private String name;
    private String lng;
    private String lat;
    private String gis;
    private String geohash;
}
