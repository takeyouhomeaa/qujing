package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.DetailTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.DetailTaskService;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ozg
 */
@Service
public class DetailTaskServiceImpl implements DetailTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Override
    public List<DetailTask> listUnacceptedTask(Integer pos, String studentId) {
        List<Task> list = taskService.listUnacceptedTask(pos, studentId);
        return createDetailTaskList(list);
    }

    @Override
    public List<DetailTask> listUnacceptedTaskByType(Integer pos, Integer type, String studentId) {
        List<Task> list = taskService.listUnacceptedTaskByType(pos, type, studentId);
        return createDetailTaskList(list);
    }

    private List<DetailTask> createDetailTaskList(List<Task> list) {
        List<DetailTask> detailTaskList = new ArrayList<>();
        for(Task task:list) {
            DetailTask detailTask = new DetailTask();
            detailTask.setTask(task);
            User sender = userService.getUserInfo(task.getSenderid());
            User receiver = userService.getUserInfo(task.getReceiverid());
            detailTask.setSender(sender);
            detailTask.setReceiver(receiver);
            detailTaskList.add(detailTask);
        }
        return detailTaskList;
    }

    @Override
    public List<DetailTask> listAccept(String studentId, Integer pos) {
        List<Task> list = taskService.listAccept(studentId, pos);
        return createDetailTaskList(list);
    }

    @Override
    public List<DetailTask> listPublish(String studentId, Integer pos) {
        List<Task> list = taskService.listPublish(studentId, pos);
        return createDetailTaskList(list);
    }

    @Override
    public DetailTask getDetailTask(Integer id) {
        DetailTask detailTask = new DetailTask();
        Task task = taskService.getDetailTask(id);
        User sender = userService.getUserInfo(task.getSenderid());
        User receiver = userService.getUserInfo(task.getReceiverid());
        detailTask.setSender(sender);
        detailTask.setReceiver(receiver);
        detailTask.setTask(task);
        return detailTask;
    }
}
