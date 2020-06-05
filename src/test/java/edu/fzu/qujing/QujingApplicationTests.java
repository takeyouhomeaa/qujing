package edu.fzu.qujing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.internal.parser.json.JsonConverter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.component.BloomFilter;
import edu.fzu.qujing.config.annotation.test;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.service.TypeService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.BloomFilterUtil;
import edu.fzu.qujing.util.MQUtil;
import edu.fzu.qujing.util.PhoneUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.spring.web.json.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class QujingApplicationTests {

    @Autowired
    RabbitTemplate template;

    @Test
    public void contextLoads() throws JsonProcessingException {

    }

    public static void main(String[] args) throws IOException {
        try {
            MQUtil.send("这是一条测试数据.。。");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
