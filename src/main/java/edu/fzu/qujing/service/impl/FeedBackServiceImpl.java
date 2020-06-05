package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.FeedBack;
import edu.fzu.qujing.mapper.FeedBackMapper;
import edu.fzu.qujing.service.FeedBackService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.SensitiveFilterUtil;
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
    public FeedBack save(String studentId, Map<String, String> params) {
        FeedBack feedBack = new FeedBack();
        String content = params.get("content");
        String filter = SensitiveFilterUtil.filter(content);
        feedBack.setContent(filter);
        feedBack.setTask(Integer.valueOf(params.get("task")));
        feedBack.setType(1);

        userService.updateReceiveTaskNumber(studentId, -1);

        return feedBack;
    }
}
