package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.Suit;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.SuitMapper;
import edu.fzu.qujing.service.SuitService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.SensitiveFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 * @author ozg
 */

@Service
@CacheConfig(cacheNames = "suit")
@Transactional(rollbackFor = Exception.class)
public class SuitServiceImpl implements SuitService {

    @Autowired
    SuitMapper suitMapper;

    @Autowired
    UserService userService;

    @Override
    public Suit save(String studentId,Map<String, String> params) {
        Suit suit = new Suit();
        String content = params.get("content");
        String filter = SensitiveFilterUtil.filter(content);
        suit.setContent(filter);
        suit.setTask(Integer.valueOf(params.get("task")));
        suit.setType(Integer.valueOf(params.get("type")));
        suitMapper.insert(suit);

        userService.updateReceiveTaskNumber(studentId, -1);

        return suit;
    }
}
