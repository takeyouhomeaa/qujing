package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.FeedBack;
import edu.fzu.qujing.mapper.FeedBackMapper;
import edu.fzu.qujing.service.FeedBackService;
import edu.fzu.qujing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    FeedBackMapper feedBackMapper;


    @Autowired
    UserService userService;

    @Override
    @CachePut(key = "#root.methodName + '(' + #result.task + ')'",unless = "#result == null")
    public FeedBack save(String studentId, Map<String, String> params) {
        FeedBack feedBack = new FeedBack();
        feedBack.setContent(params.get("content"));
        feedBack.setTask(Integer.valueOf(params.get("task")));
        feedBack.setType(1);

        userService.updateReceiveTaskNumber(studentId, -1);

        return feedBack;
    }
}
