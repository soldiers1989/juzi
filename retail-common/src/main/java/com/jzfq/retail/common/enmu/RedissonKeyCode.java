package com.jzfq.retail.common.enmu;

/**
 * 分布式锁标志(LOCK_KEY)
 * @author caishijian
 * @version V1.0
 * @date 2018年08月08日 12:57
 */
public enum RedissonKeyCode {

    GOODS_STOCK_KEY("F43EB66D7B4E4ED6B2AEB022F3B8FB9C", "商品库存分布式LOCK_KEY")
    ;
    private String code;

    private String description;

    RedissonKeyCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RedissonKeyCode getEnum(String code){
        for (RedissonKeyCode redissonKeyCode : RedissonKeyCode.values()) {
            if (redissonKeyCode.getCode().equals(code)) {
                return redissonKeyCode;
            }
        }
        return null;
    }
}
