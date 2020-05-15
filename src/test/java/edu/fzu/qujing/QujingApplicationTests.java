package edu.fzu.qujing;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.spring.web.json.Json;

@SpringBootTest
class QujingApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TaskMapper taskMapper;

    @Test
    void contextLoads() throws JsonProcessingException {
        RedisUtil redisUtil = new RedisUtil();
//        System.out.println(redisUtil.sGet("test"));
        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(redisUtil.get("test2"));
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writeValueAsString("test"));

        redisUtil.set("hhh","aaa");
        System.out.println(redisUtil.get("hhh"));


    }



}
