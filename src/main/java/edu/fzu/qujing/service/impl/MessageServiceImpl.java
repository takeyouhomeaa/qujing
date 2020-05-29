package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Message;
import edu.fzu.qujing.mapper.MessageMapper;
import edu.fzu.qujing.service.MessageService;
import edu.fzu.qujing.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "'listMessageByReceiveId('+ #result.receiveId + ',*)'"),
                    @CacheEvict(key = "'listMessageByPublisherId('+ #result.publisherId +',*)'")
            }
    )
    public Message save(Message message) {
        messageMapper.insert(message);
        return message;
    }

    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public List<Message> listMessageByReceiveId(String studentId,Integer pos) {
        return messageMapper.listMessageByStudentId(null, studentId,
                new Page<>(pos, PageUtil.PAGES)).getRecords();
    }

    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public List<Message> listMessageByPublisherId(String studentId,Integer pos) {
        return messageMapper.listMessageByStudentId(studentId, null,
                new Page<>(pos, PageUtil.PAGES)).getRecords();
    }
}
