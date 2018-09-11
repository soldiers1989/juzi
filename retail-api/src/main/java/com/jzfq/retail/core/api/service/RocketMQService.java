package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.req.SysUserSearchReq;
import com.jzfq.retail.bean.vo.req.SysUserVo;
import com.jzfq.retail.bean.vo.res.ImportExcelResult;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface RocketMQService {

    /**
     * 测试RocketMQ
     */
    void sendMq();

}
