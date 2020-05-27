package edu.fzu.qujing.config.autoload;

import com.alipay.api.internal.parser.json.JsonConverter;
import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;

public class AsyncTaskComponent {
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    TaskService taskService;

    @PostConstruct
    public void init() {
        String key1 = "delayQueueToCancel";
        String key2 = "delayQueueToConfirm";
        if(RedisUtil.hasKey(key1)){
            Object delayQueueToCancel = RedisUtil.get(key1);
            DelayQueueUtil.setDelayQueueToCancel((DelayQueue<DelayTask>)delayQueueToCancel);
        }
        if(RedisUtil.hasKey(key2)){
            Object delayQueueToConfirm = RedisUtil.get(key2);
            DelayQueueUtil.setDelayQueueToCancel((DelayQueue<DelayTask>)delayQueueToConfirm);
        }
        executor.execute(taskService.taskWasNotTaken());
        executor.execute(taskService.autoTaskConfirm());
    }
}
