package edu.fzu.qujing.component;

import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;

@Component
public class AsyncTaskComponent{
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    TaskService taskService;

    @PostConstruct
    public void autoLoad() {
        executor.execute(taskService.taskWasNotTaken());
        executor.execute(taskService.autoTaskConfirm());
    }

}
