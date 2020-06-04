package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.Expenses;
import edu.fzu.qujing.bean.Recharge;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.service.PageService;
import edu.fzu.qujing.service.SettleService;
import edu.fzu.qujing.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SettlePageServiceImpl implements PageService {

    @Autowired
    SettleService settleService;

    @Override
    public void saveCache(String key, Object object, Integer timeout) {

    }

    @Override
    public void saveCache(String key, List<?> list, Integer timeout) {

    }

    @Override
    public List<Task> listPageDataByCache(String key, Integer pos) {
        return null;
    }

    @Override
    public Integer getCount(String key, Integer pos) {
        return null;
    }

    @Override
    public void delCache(String key, Integer id) {

    }

    @Override
    public Runnable cachePreload(String studentId) {
        Runnable runnable = ()->{
            List<Expenses> expenses = settleService.listExpensesRecord(studentId, 0);
            List<Recharge> recharges = settleService.listRecharheRecord(studentId, 0);
            String key = "settle::listRecharheRecord("+ studentId+ ","+ 0 +")";
            RedisUtil.set(key, recharges);
            key = "settle::listExpensesRecord("+ studentId+ ","+ 0 +")";
            RedisUtil.set(key, expenses);
            log.info("消费记录缓存模块加载完毕");
            System.out.println("消费记录缓存模块加载完毕");
        };
        return runnable;
    }
}
