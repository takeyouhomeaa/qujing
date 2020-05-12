package edu.fzu.qujing;

import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QujingApplicationTests {

    @Autowired
    TaskMapper taskMapper;

    @Test
    void contextLoads() {
        Task task = new Task();
        task.setName("test");
        Type type = new Type();
        type.setId(1);
        task.setTtid(1);
        taskMapper.insert(task);
    }



}
