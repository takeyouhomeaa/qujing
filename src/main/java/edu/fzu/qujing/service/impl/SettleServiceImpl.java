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
import edu.fzu.qujing.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        User user = userService.getUserPoints(studentId);
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
    @CacheEvict(key = "'listRecharheRecord(' + #result.userId +',*)'")
    public Recharge addRechargeRecord(Recharge recharge) {
        rechargeMapper.insert(recharge);
        return recharge;
    }

    /**
     * 添加积分消费记录
     *
     * @param expenses 消费数据
     */
    @Override
    @CacheEvict(key = "'listExpensesRecord(' + #result.userId +',*)'")
    public Expenses addExpensesRecord(Expenses expenses) {
        expensesMapper.insert(expenses);
        return expenses;
    }


    /**
     * 以分页的方式查询消费记录
     *
     * @param pos      数据行数
     * @return 消费记录列表
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Expenses> listExpensesRecord(String studentId, Integer pos) {
        return expensesMapper.listExpense(studentId,new Page<Expenses>(pos,PageUtil.PAGES)).getRecords();
    }


    /**
     * @param studentId
     * @param pos      数据行数
     * @return 充值记录列表
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<Recharge> listRecharheRecord(String studentId, Integer pos) {

        return rechargeMapper.listRechargeRecord(studentId,new Page<Recharge>(pos,PageUtil.PAGES)).getRecords();
    }


}