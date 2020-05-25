package edu.fzu.qujing.config.autoload;

import edu.fzu.qujing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AsyncTaskComponent {
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    TaskService taskService;

    @PostConstruct
    public void init() {

        executor.execute(taskService.taskWasNotTaken());
        executor.execute(taskService.autoTaskConfirm());
    }
}
