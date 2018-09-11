package com.jzfq.retail.core.dao.manual;

import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.vo.req.DictionarySearchReq;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月27日 14:01
 * @Description:
 */
public interface DictionaryManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<Map<String, Object>> findList(DictionarySearchReq record);

    Dictionary findByCode(String code);
    Dictionary findByName(String code);
}
