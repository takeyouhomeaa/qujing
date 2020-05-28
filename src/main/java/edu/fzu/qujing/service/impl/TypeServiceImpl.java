package edu.fzu.qujing.service.impl;

import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.mapper.TypeMapper;
import edu.fzu.qujing.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "type")
@Transactional(rollbackFor = Exception.class)
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    /**
     * 获取任务类型列表
     *
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    public List<Type> listTaskType() {
        return typeMapper.listTaskType();
    }

    /**
     * 获取任务状态列表
     *
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    public List<Type> listTaskState() {
        return typeMapper.listTaskState();
    }

    /**
     * 通过类型id获取任务状态
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public Type getTakeState(Integer id) {
        return typeMapper.getTakeState(id);
    }

    /**
     * 通过类型id获取任务状态
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public Type getTaskType(Integer id) {
        return typeMapper.getTaskType(id);
    }

    /**
     * 通过类型id获取消费记录类型
     *
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'")
    public List<Type> listExpensesType() {
        return typeMapper.listExpensesType();
    }

    /**
     * 通过类型id获取消费记录类型
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(key = "#root.methodName + '(' + #root.args + ')'",unless = "#result == null")
    public Type getExpensesType(Integer id) {
        return typeMapper.getExpensesType(id);
    }
}
