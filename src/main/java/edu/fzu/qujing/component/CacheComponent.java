package edu.fzu.qujing.component;

import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;

@Slf4j
@Component
public class CacheComponent implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        delayiTaskIit();
        RedisUtil.flushdb();
    }

    private void delayiTaskIit() {

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

        log.info("延迟任务加载完毕");
    }

    private void cachePreload(){
        //后期在补充
    }
}
