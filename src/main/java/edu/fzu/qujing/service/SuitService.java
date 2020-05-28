package edu.fzu.qujing.service;

import edu.fzu.qujing.bean.Suit;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SuitService {
    /**
     * 保存举报信息
     *
     * @return
     */
    Suit save(String studentId,Map<String, String> params);
}
