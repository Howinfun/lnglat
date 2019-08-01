package com.hyf.lnglat;

import com.hyf.lnglat.entity.UserGis;
import com.hyf.lnglat.mapper.UserGisMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LnglatApplicationTests {

    @Autowired
    private UserGisMapper userGisMapper;

    /**
     * 生成十万条测试数据
     */
    @Test
    public void insertData(){

        for (int i = 0; i < 100000; i++) {
            // 随机获取经纬度
            Double lng = RandomUtils.nextDouble(103.9999,133.99999);
            Double lat = RandomUtils.nextDouble(21.9999,39.9999);
            StringBuilder sb = new StringBuilder();
            // 拼接point，为geometry类型字段准备
            sb.append("point(").append(lng).append(" ").append(lat).append(")");
            UserGis userGis = new UserGis();
            String name = "测试充电站"+i;
            userGis.setName(name);
            userGis.setLng(lng.toString());
            userGis.setLat(lat.toString());
            userGis.setGis(sb.toString());
            // 插入数据
            this.userGisMapper.insertUserGis(userGis);
        }
    }

}
