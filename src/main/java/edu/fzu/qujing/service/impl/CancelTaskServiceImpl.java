package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.CancelTask;
import edu.fzu.qujing.mapper.CancelTaskMapper;
import edu.fzu.qujing.service.CancelTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "cancelTask")
@Transactional(rollbackFor = Exception.class)
public class CancelTaskServiceImpl implements CancelTaskService {

    @Autowired
    CancelTaskMapper cancelTaskMapper;

    @Override
    public void save(CancelTask task) {
        cancelTaskMapper.insert(task);
    }

    /**
     * 保存取消任务理由
     *
     * @param id
     * @param content
     * @param type
     * @return
     */
    @CachePut(key = "#root.methodName + '(' + #root.args + ')'")
    @Override
    public CancelTask save(Integer id, String content, String type) {
        CancelTask cancelTask = new CancelTask();
        cancelTask.setContent(content);
        cancelTask.setFlat(0);
        cancelTask.setTask(id);
        cancelTask.setId(Integer.parseInt(type));
        save(cancelTask);
        return cancelTask;
    }


}
