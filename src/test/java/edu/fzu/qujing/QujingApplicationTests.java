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
import edu.fzu.qujing.util.PhoneUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
class QujingApplicationTests {


    @Test
    public void contextLoads() throws JsonProcessingException {

    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("static//sensi_words.txt"))
                , StandardCharsets.UTF_8));
        bufferedReader.readLine();
        System.out.println(bufferedReader.readLine());
    }

}
