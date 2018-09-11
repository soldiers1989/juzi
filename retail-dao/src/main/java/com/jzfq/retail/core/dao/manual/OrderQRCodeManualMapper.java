package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.OrderQRCode;

public interface OrderQRCodeManualMapper {

    void updateOrderQRCodeIsAvailable(OrderQRCode qrcode);
}