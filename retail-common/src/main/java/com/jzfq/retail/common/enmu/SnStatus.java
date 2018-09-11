package com.jzfq.retail.common.enmu;

/**
 * 库存详情表状态
 * @author caishijian
 * @version V1.0
 * @date 2018年07月30日 12:57
 */
public enum SnStatus {

    ENTER_STORAGE("1", "入库"),
    OUT_STORAGE("2", "出库"),
    RETURN_GOODS("3","退货");

    private String status;

    private String description;

    SnStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static SnStatus getEnum(String status){
        for (SnStatus snStatus : SnStatus.values()) {
            if (snStatus.getStatus().equals(status)) {
                return snStatus;
            }
        }
        return null;
    }
}
