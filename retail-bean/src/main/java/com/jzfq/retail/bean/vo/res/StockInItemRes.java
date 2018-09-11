package com.jzfq.retail.bean.vo.res;

import com.jzfq.retail.bean.domain.PurchaseCollect;
import com.jzfq.retail.bean.domain.PurchaseCollectGoods;
import com.jzfq.retail.bean.domain.PurchaseCollectGoodsInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 入库单查询
 * @author zhangjianwei
 * @version V1.0
 *
 */
@ToString
@Getter
@Setter
public class StockInItemRes implements Serializable {
	private PurchaseCollectGoods stockInItems;
	private List<PurchaseCollectGoodsInfo> stockInItemInfos;
}
