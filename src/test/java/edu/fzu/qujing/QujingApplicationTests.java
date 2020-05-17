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
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.List;

@SpringBootTest
class QujingApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Test
    public void contextLoads() throws JsonProcessingException {
        User user = new User();
        user.setStudentId("221701206");
        user.setEmail("root");
        user.setPassword("asd");
        userService.save(user);
    }

    @CachePut(cacheNames = "te",key = "tt1")
    public Task taskList(Task task) {
        return taskMapper.selectById(10101);

    }



}
