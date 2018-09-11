package com.jzfq.retail.core.messaging.support.base;

/**
 * @author lagon
 * @time 2017/7/6 11:44
 * @description MQ暴露的API标识枚举
 */
public enum MQApiIdentifier {

    CONTRACT_RELATION_PUSH("contractRelationPush","债匹关系推送"),
    LOAN_STATUS_INFORM("loanStatusInform","订单状态通知"),
    SETTLEMENT_RESULT_INFORM("settlementResultInform","结算结果通知"),
    LW_MQ_TEST_INFORM("lwMqTestInform","LW测试MQ")
    ;

    private String mark;
    private String name;

    MQApiIdentifier(String mark, String name) {
        this.mark=mark;
        this.name=name;
    }

    public String getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public static MQApiIdentifier getEnum(String mark){
        for (MQApiIdentifier  mqApiIdentifier: MQApiIdentifier.values()) {
            if (mqApiIdentifier.getMark().equals(mark)) {
                return mqApiIdentifier;
            }
        }
        return null;
    }

}
