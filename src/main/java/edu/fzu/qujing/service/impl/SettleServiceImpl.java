package edu.fzu.qujing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Expenses;
import edu.fzu.qujing.bean.Recharge;
import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.mapper.ExpensesMapper;
import edu.fzu.qujing.mapper.RechargeMapper;
import edu.fzu.qujing.mapper.UserMapper;
import edu.fzu.qujing.service.SettleService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.BloomFilterUtil;
import edu.fzu.qujing.util.PageUtil;
import edu.fzu.qujing.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author ozg
 */

@Service
@CacheConfig(cacheNames = "settle")
@Transactional(rollbackFor = Exception.class)
public class SettleServiceImpl implements SettleService {

    @Autowired
    private UserService userService;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private ExpensesMapper expensesMapper;


    @Override
    public void updatePoints(String studentId, Integer amount) {
        User user = userService.getUserInfo(studentId);
        user.setStudentId(studentId);
        user.setPoints(user.getPoints() + amount * 100);
        userService.updatePoints(user);
    }

    /**
     * 增加用户积分
     *
     * @param amount 金额
     */
    @Override
    @Caching(

    )
    public void increasePoints(String studentId, Integer amount) {
        updatePoints(studentId, amount);
        Recharge recharge = new Recharge();
        recharge.setAmount(amount);
        recharge.setUserId(studentId);
        recharge.setTime(new Date());
        addRechargeRecord(recharge);
    }

    /**
     * 减少用户积分
     *
     * @param studentId 学号
     * @param amount    金额
     */
    @Override
    public void decreasePoints(String studentId, Integer amount) {
        updatePoints(studentId, -amount);
        Expenses expenses = new Expenses();
        expenses.setAmount(amount);
        expenses.setTime(new Date());
        expenses.setUserId(studentId);
        expenses.setType(2);
        addExpensesRecord(expenses);
    }

    @Override
    public Recharge addRechargeRecord(Recharge recharge) {
        rechargeMapper.insert(recharge);
        List<Recharge> list = listRecharheRecord(recharge.getUserId(), 0);
        RedisUtil.set("settle::listRecharheRecord("+ recharge.getUserId()+ ","+ 0 +")", list);

        return recharge;
    }

    /**
     * 添加积分消费记录
     *
     * @param expenses 消费数据
     */
    @Override
    public Expenses addExpensesRecord(Expenses expenses) {
        expensesMapper.insert(expenses);
        List<Expenses> list = listExpensesRecord(expenses.getUserId(), 0);
        RedisUtil.set("settle::listExpensesRecord("+ expenses.getUserId()+ ","+ 0 +")", list);

        return expenses;
    }


    /**
     * 以分页的方式查询消费记录
     *
     * @param pos      数据行数
     * @return 消费记录列表
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Expenses> listExpensesRecord(String studentId, Integer pos) {
        if(!userService.checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        String key = "settle::listExpensesRecord("+ studentId+ ","+ pos +")";
        if(RedisUtil.hasKey(key)){
            return (List<Expenses>) RedisUtil.get(key);
        }
        List<Expenses> records = expensesMapper.listExpense(studentId,
                new Page<Expenses>(pos, PageUtil.PAGE_SIZE)).getRecords();
        if(pos != 0) {
            RedisUtil.set(key, records, 10 * 60);
        }
        return records;
    }


    /**
     * @param studentId
     * @param pos      数据行数
     * @return 充值记录列表
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Recharge> listRecharheRecord(String studentId, Integer pos) {
        if(!userService.checkStudentId(studentId)){
            throw new RuntimeException("The server is busy");
        }
        String key = "settle::listRecharheRecord("+ studentId+ ","+ pos +")";
        if(RedisUtil.hasKey(key)){
            return (List<Recharge>) RedisUtil.get(key);
        }
        List<Recharge> records = rechargeMapper.listRechargeRecord(studentId,
                new Page<Recharge>(pos, PageUtil.PAGE_SIZE)).getRecords();

        if(pos != 0) {
            RedisUtil.set(key, records, 10 * 60);
        }

        return records;
    }


}
