package edu.fzu.qujing.config.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RedisCacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        Map<String,Object> map = new HashMap<>();
        Class<?> targetClassClass = target.getClass();
        map.put("class",targetClassClass.toGenericString());
        map.put("methodName",method.getName());
        map.put("package",targetClassClass.getPackage());
        for (int i = 0; i < params.length; i++) {
            map.put(String.valueOf(i),params[i]);
        }
        String jsonString = JSON.toJSONString(map);

        return DigestUtils.sha256Hex(jsonString);
    }
}
