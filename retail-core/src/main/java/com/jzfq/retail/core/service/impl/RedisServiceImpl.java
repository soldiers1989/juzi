package com.jzfq.retail.core.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzfq.retail.core.api.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangzw
 * @Date: 2017/11/7 17:04
 * @Description:
 * @Version: 1.0
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        //设置序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //配置redisTemplate
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);//key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);//Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);//Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @author: wangzw
     * @description: 根据key查询数据
     * @version: 1.0
     * @date: 2017/11/15 15:11
     */
    @Override
    public Object queryObjectByKey(String redisKey) {
        ValueOperations<Serializable, Object> operations = getRedisTemplate().opsForValue();
        Object redisVal = operations.get(redisKey);
        logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "queryObjectByKey", redisKey, redisVal);
        return redisVal;
    }

    @Override
    public String queryStringByKey(String redisKey) {
        ValueOperations<String, Object> operations = getRedisTemplate().opsForValue();
        Object redisVal = operations.get(redisKey);
        logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "queryStringByKey", redisKey, redisVal);
        if (redisVal == null) {
            return null;
        }
        return redisVal.toString();
    }

    /**
     * @author: wangzw
     * @description: 判断key是否存在
     * @version: 1.0
     * @date: 2017/11/15 15:15
     */
    @Override
    public boolean existsKey(String redisKey) {
        boolean result = getRedisTemplate().hasKey(redisKey);
        logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "existsKey", redisKey, result);
        return result;
    }

    /**
     * @author: wangzw
     * @description: 设置永久的值
     * @version: 1.0
     * @date: 2017/11/15 15:17
     */
    @Override
    public boolean setForeverData(String redisKey, String value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = getRedisTemplate().opsForValue();
            operations.set(redisKey, value);
            result = true;
            logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "setForeverData", redisKey, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param expireTime :秒
     * @author: wangzw
     * @description: 设置有效时间的key
     * @version: 1.0
     * @date: 2017/11/15 15:17
     */
    @Override
    public boolean setTimesData(String redisKey, String value, int expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = getRedisTemplate().opsForValue();
            operations.set(redisKey, value);
            getRedisTemplate().expire(redisKey, expireTime, TimeUnit.SECONDS);
            result = true;
            logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "setTimesData", redisKey, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @author: wangzw
     * @description: 删除key
     * @version: 1.0
     * @date: 2017/11/15 15:18
     */
    @Override
    public boolean deleteByKey(String redisKey) {
        boolean result = false;
        if (exists(redisKey)) {
            getRedisTemplate().delete(redisKey);
            result = true;
        }
        logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "setTimesData", redisKey, result);
        return result;
    }

    @Override
    public boolean deleteByKey(String... redisKey) {
        boolean result = false;
        try {
            for (String key : redisKey) {
                getRedisTemplate().delete(key);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("class:[{}],method:[{}],key:[{}],val:[{}]", this.getClass().getName(), "setTimesData", redisKey, result);
        return result;
    }

    private boolean exists(final String reidsKey) {
        return getRedisTemplate().hasKey(reidsKey);
    }

    @Override
    public boolean updateKeyTimes(String redisKey, int expireTime) {
        return getRedisTemplate().expire(redisKey, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void putH(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key,hashKey,value);
    }

    @Override
    public String getH(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key,hashKey);
    }
}
