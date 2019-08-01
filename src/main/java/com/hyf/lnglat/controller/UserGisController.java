package com.hyf.lnglat.controller;

import com.hyf.lnglat.entity.UserGis;
import com.hyf.lnglat.mapper.UserGisMapper;
import com.hyf.lnglat.query.UserGisQuery;
import com.hyf.lnglat.utils.GeoCode;
import com.spatial4j.core.io.GeohashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Howinfun
 * @desc
 * @date 2019/7/31
 */
@RequestMapping("userGis")
@RestController
public class UserGisController {

    @Autowired
    private UserGisMapper userGisMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String USER_GIS="USER_GIS";

    @PostMapping("/getRangeByRIndex")
    public List<UserGis> getRangeByRIndex(@RequestBody UserGisQuery query){
        List<UserGis> userGisList = userGisMapper.getRangeByRIndex(query);
        return userGisList;
    }

    @PostMapping("/getRangeByGeoHash")
    public List<UserGis> getRangeByGeoHash(@RequestBody UserGisQuery query){
        Integer geoCode = GeoCode.INSTANCE.getGeoCode(query.getRange());
        String geoHash = GeohashUtils.encodeLatLon(Double.parseDouble(query.getLat()), Double.parseDouble(query.getLng()),geoCode);
        query.setGeoHash(geoHash);
        List<UserGis> userGisList = userGisMapper.getRangeByGeoHash(query);
        return userGisList;
    }

    @PostMapping("/getRangeByFormula")
    public List<UserGis> getRangeByFormula(@RequestBody UserGisQuery query){
        List<UserGis> userGisList = userGisMapper.getRangeByFormula(query);
        return userGisList;
    }

    @PostMapping("/getRangeByRedis")
    public List<UserGis> getRangeByRedis(@RequestBody UserGisQuery query){
        /**
         * 根据指定经纬度，返回半径不超过指定距离的元素
         * distance:指定距离
         * point:指定经纬度
         */
        Distance distance = new Distance(query.getRange(),Metrics.KILOMETERS);
        Point point = new Point(Double.parseDouble(query.getLng()),Double.parseDouble(query.getLat()));
        Circle circle = new Circle(point,distance);
        /**
         * includeDistance: 返回距离
         * includeCoordinates：返回坐标
         * sortAscending：升序
         * limit：只返回一百个
         */
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(100);
        GeoResults<RedisGeoCommands.GeoLocation> geoResults = redisTemplate.opsForGeo().radius(USER_GIS,circle,args);
        List<Integer> ids = new ArrayList<>(100);
        for (GeoResult<RedisGeoCommands.GeoLocation> geoResult : geoResults) {
            ids.add((Integer)geoResult.getContent().getName());
        }
        query.setIds(ids);
        List<UserGis> userGisList = userGisMapper.getRangeByRedis(query);
        return userGisList;
    }

    public static void main(String[] args) {
        Integer geoCode = GeoCode.INSTANCE.getGeoCode(20);
        String geoHash = GeohashUtils.encodeLatLon(23.0896,113.37660 ,geoCode);
        System.out.println(geoHash);
    }
}
