package com.jzfq.retail.core.dao.manual;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.bean.domain.RemittanceAccount;
import com.jzfq.retail.bean.domain.RemittanceAccountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface RemittanceAccountManualMapper {

    Page<HashMap<String,Object>> selectList(HashMap<String, Object> paramMap);
}