package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 19:55
 * @Description:
 */
public interface ProblemOrderManualMapper {

    Map<String, Object> getMap1();

    List<Map<String,Object>> statisticsSellerRebate();
}
