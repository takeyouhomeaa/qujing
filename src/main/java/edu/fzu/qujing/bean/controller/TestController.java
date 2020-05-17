package edu.fzu.qujing.bean.controller;

import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.mapper.TaskMapper;
import edu.fzu.qujing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TaskService taskService;

    @RequestMapping("/test")
    public Task test(){
        return taskService.getDetailTask(10101);
    }
}
