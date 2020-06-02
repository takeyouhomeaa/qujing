package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.Log;
import edu.fzu.qujing.mapper.LogMapper;
import edu.fzu.qujing.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    @Autowired
    LogMapper logMapper;

    @Override
    public Log save(Log log) {
        logMapper.insert(log);
        return log;
    }

    @Override
    public Log update(Log log) {
        Log byId = logMapper.selectById(log.getId());


        return null;
    }
}
