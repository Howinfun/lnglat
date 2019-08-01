package com.hyf.lnglat.query;

import lombok.Data;

import java.util.List;

/**
 * @author Howinfun
 * @desc
 * @date 2019/7/31
 */
@Data
public class UserGisQuery {
    private String lng;
    private String lat;
    private Integer range;
    private String geoHash;
    private List<Integer> ids;
}
