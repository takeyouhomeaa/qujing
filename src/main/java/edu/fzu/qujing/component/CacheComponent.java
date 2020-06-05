package edu.fzu.qujing.component;

import edu.fzu.qujing.bean.DelayTask;
import edu.fzu.qujing.util.DelayQueueUtil;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;

@Slf4j
@Component
public class CacheComponent implements ApplicationRunner {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    TypeService typeService;

    private static Integer pages = 5;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        delayiTaskInit();

        bloomFilterInit();

        RedisUtil.flushdb();
    }

    private void bloomFilterInit() {
        String key = "bloomFilterToUser";
        BloomFilter bloomFilterToUser = BloomFilterUtil.readFilterFromFile(key);
        if(bloomFilterToUser != null) {
            BloomFilterUtil.setBloomFilterToUser(bloomFilterToUser);
        }

        key = "bloomFilterToTask";
        BloomFilter bloomFilterToTask = BloomFilterUtil.readFilterFromFile(key);
        if(bloomFilterToUser != null) {
            BloomFilterUtil.setBloomFilterToTask(bloomFilterToTask);
        }


        log.info("布隆过滤器加载完毕");
    }

    private void saveBloomFilter() {
        String key = "bloomFilterToUser";
        BloomFilterUtil.saveFilterToFile(BloomFilterUtil.getBloomFilterToUser(),key);
        key = "bloomFilterToTask";
        BloomFilterUtil.saveFilterToFile(BloomFilterUtil.getBloomFilterToTask(),key);
        log.info("布隆过滤器重新加载完成");
    }

    private void delayiTaskInit() {

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

    private void saveDelayTask() {
        RedisUtil.set("delayQueueToCancel", DelayQueueUtil.getDelayTaskToCancel());
        RedisUtil.set("delayQueueToConfirm", DelayQueueUtil.getDelayQueueToConfirm());
        log.info("延迟任务重新保存完成");
    }

    private void cachePreload(){
        //后期在补充
    }
}
