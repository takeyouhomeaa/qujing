package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.FeedBack;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface FeedBackService {

    /**
     * 保存欺诈反馈信息
     *
     * @param studentId
     * @param params
     * @return
     */
    FeedBack save(String studentId, Map<String, String> params);
}
