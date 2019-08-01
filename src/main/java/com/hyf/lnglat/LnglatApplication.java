package com.hyf.lnglat;

import com.hyf.lnglat.entity.UserGis;
import com.hyf.lnglat.mapper.UserGisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootApplication
public class LnglatApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserGisMapper userGisMapper;
    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;
    public static void main(String[] args) {
        SpringApplication.run(LnglatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 先清除缓存，再添加缓存
        lettuceConnectionFactory.getConnection().flushDb();
        List<UserGis> userGisList = userGisMapper.getAll();
        for (UserGis userGis : userGisList) {
            redisTemplate.opsForGeo().add("USER_GIS",new Point(Double.parseDouble(userGis.getLng()),Double.parseDouble(userGis.getLat())),userGis.getId());
        }
    }
}
