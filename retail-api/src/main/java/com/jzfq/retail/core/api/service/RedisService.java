package com.jzfq.retail.core.api.service;

/**
 * @Author: wangzw
 * @Description: redis接口
 * @Version: 1.0
 * @Date: 2017/11/7 17:00
 */
public interface RedisService {

    /**
     * @Author: wangzw
     * @Description: 通过key查询数据
     * @Version: 1.0
     * @Date: 2017/11/7 17:00
     */
    Object queryObjectByKey(final String redisKey);

    String queryStringByKey(final String redisKey);

    /**
     * @Author: wangzw
     * @Description: 判断key是否存在
     * @Version: 1.0
     * @Date: 2017/11/7 17:00
     */
    boolean existsKey(final String redisKey);

    /**
     * @Author: wangzw
     * @Description: 设置值--没有时间  永久保存
     * @Version: 1.0
     * @Date: 2017/11/7 17:01
     */
    boolean setForeverData(final String redisKey, final String value);

    /**
     * @Author: wangzw
     * @Description: 设置值
     * @Version: 1.0
     * @Date: 2017/11/7 17:03
     */
    boolean setTimesData(final String redisKey, final String value, final int expireTime);

    /**
     * @Author: wangzw
     * @Description: 通过key删除数据
     * @Version: 1.0
     * @Date: 2017/11/7 17:03
     */
    boolean deleteByKey(final String redisKey);

    /**
     * @author: wangzw
     * @description: 批量删除key
     * @version: 1.0
     * @date: 2017/11/15 15:40
     */
    boolean deleteByKey(final String... redisKey);

    /**
     * @Author: wangzw
     * @Description: 更新生存时长
     * @Version: 1.0
     * @Date: 2017/11/7 17:03
     */
    boolean updateKeyTimes(final String redisKey, final int expireTime);

    /**
     * 哈希,新增
     * @param key
     * @param hashKey
     * @param value
     */
    void putH(String key, String hashKey, String value);

    /**
     * 哈希，查询
     * @param key
     * @param hashKey
     * @return
     */
    String getH (String key, String hashKey);

}
