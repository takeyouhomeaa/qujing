package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.CancelTask;
import org.springframework.stereotype.Service;

@Service
public interface CancelTaskService {

    /**
     * 保存取消任务理由
     * @param task
     */
    void save(CancelTask task);

    /**
     * 保存取消任务理由
     * @param id
     * @param content
     * @param type
     * @return
     */
    CancelTask  save(Integer id, String content, Integer type);
}
