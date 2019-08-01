package com.hyf.lnglat.mapper;

import com.hyf.lnglat.entity.UserGis;
import com.hyf.lnglat.query.UserGisQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Howinfun
 * @desc
 * @date 2019/7/31
 */
@Mapper
@Component
public interface UserGisMapper extends BaseMapper<UserGis> {

    /**
     * 获取全部地理位置信息
     * @return
     */
    @Select("select id,name,lng,lat,REPLACE(REPLACE(Replace(ST_AsText(gis),'POINT(',''),')',''),' ',',') gis,geohash gis from user_gis;")
    List<UserGis> getAll();

    /**
     * 更新经纬度
     * @param userGis
     */
    @Update("update user_gis set lng = #{lng},lat = #{lat} where id = #{id}")
    void updateLngLat(UserGis userGis);

    /**
     * 经纬度-公式
     * @param query
     * @return
     */
    @Select("SELECT " +
            "id," +
            "name," +
            "    ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN( ( #{lat} * PI( ) / 180 - lat * PI( ) / 180 ) / 2 ),2) + COS( #{lat} * PI( ) / 180 ) * COS( lat * PI( ) / 180 ) * POW(SIN( ( #{lng} * PI( ) / 180 - lng * PI( ) / 180 ) / 2 ),2))) * 1000) AS distance,\n" +
            "    ST_AsText(gis) gis " +
            "FROM user_gis " +
            "WHERE lng BETWEEN #{lng}-(0.009091*#{range}) and #{lng}+(0.009091*#{range}) " +
            "            and lat BETWEEN #{lat}-(0.009091*#{range}) and #{lat}+(0.009091*#{range}) "+
            "ORDER BY distance limit 100;")
    List<UserGis> getRangeByFormula(UserGisQuery query);

    /**
     * 经纬度-R索引
     * @param query
     * @return
     */
    @Select("SELECT " +
            "id," +
            "name," +
            "(ST_Distance (point (#{lng},#{lat}),point(lng,lat) ) / 0.0111) AS distance," +
            "ST_AsText(gis) gis " +
            "FROM user_gis " +
            "WHERE ST_Contains( ST_MakeEnvelope(" +
            "    Point((#{lng}+(#{range}/111)), (#{lat}+(#{range}/111)))," +
            "    Point((#{lng}-(#{range}/111)), (#{lat}-(#{range}/111)))" +
            "), gis )" +
            "ORDER BY distance limit 100;")
    List<UserGis> getRangeByRIndex(UserGisQuery query);

    /**
     * 经纬度-geoHash
     * @param query
     * @return
     */
    @Select("SELECT " +
            "id," +
            "name," +
            "ST_Distance_Sphere(Point(#{lng},#{lat}), gis) as distance, " +
            "ST_AsText(gis) gis " +
            "FROM user_gis " +
            "WHERE geohash like CONCAT(#{geoHash},'%') " +
            "ORDER BY distance limit 100;")
    List<UserGis> getRangeByGeoHash(UserGisQuery query);

    /**
     * 经纬度-Redis
     * @param query
     * @return
     */
    List<UserGis> getRangeByRedis(UserGisQuery query);

    @Insert("insert into user_gis(name,lng,lat,gis)values(#{name},#{lng},#{lat},geomfromtext(#{gis}))")
    void insertUserGis(UserGis userGis);



}
