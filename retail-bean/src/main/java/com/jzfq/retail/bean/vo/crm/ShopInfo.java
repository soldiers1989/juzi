package com.jzfq.retail.bean.vo.crm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: ShopInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:18
 * @Description: 门店信息
 */
@Getter
@Setter
@ToString
public class ShopInfo implements Serializable {

    /**
     * 门店名称
     */
    @NotBlank(message = "[shopName]门店名称不可为空")
    private String shopName;
    /**
     * 门店简称
     */
    private String shopSimpleName;
    /**
     * 门店经度
     */
    @NotNull(message = "[shopLongitude]门店经度不可为空")
    private Double shopLongitude;
    /**
     * 门店纬度
     */
    @NotNull(message = "[shopLatitude]门店纬度不可为空")
    private Double shopLatitude;
    /**
     * 商户主营业务 2:健身 13:服饰 14:轻奢 15:3C及家电 0:其他
     */
    private Integer mainBusiness;
    /**
     * 门店类型 2：2S; 3：4S；3：POP商户；5：线下商户
     */
    private String type;
    /**
     * 开业时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openningDate;
    /**
     * 门店联系人姓名
     */
    @NotBlank(message = "[contactName]门店联系人姓名不可为空")
    private String contactName;
    /**
     * 门店联系人职位
     */
    private String contactJob;
    /**
     * 门店联系人手机号
     */
    @NotBlank(message = "[contactMobile]门店联系人手机号不可为空")
    private String contactMobile;
    /**
     * 店铺所有权 店铺所有权 1:公司所有 2:租赁
     */
    private String ownerShip;
    /**
     * 经营面积
     */
    private String area;
    /**
     * 总员工人数
     */
    private String workers;
    /**
     * 每月营销额
     */
    private BigDecimal averageMonthSale;
    /**
     * 开始营业时间
     */
    @NotBlank(message = "[workStartTime]开始营业时间不可为空")
    private String workStartTime;
    /**
     * 结束营业时间
     */
    @NotBlank(message = "[workEndTime]结束营业时间不可为空")
    private String workEndTime;
    /**
     * 门店省code
     */
    @NotBlank(message = "[provinceCode]门店省code不可为空")
    private String provinceCode;
    /**
     * 门店省名称
     */
    @NotBlank(message = "[province]门店省名称不可为空")
    private String province;
    /**
     * 门店市code
     */
    @NotBlank(message = "[cityCode]门店市code不可为空")
    private String cityCode;
    /**
     * 门店市名称
     */
    @NotBlank(message = "[city]门店市名称不可为空")
    private String city;
    /**
     * 门店区县code
     */
    @NotBlank(message = "[districtCode]门店区县code不可为空")
    private String districtCode;
    /**
     * 门店区县名称
     */
    @NotBlank(message = "[district]门店区县名称不可为空")
    private String district;
    /**
     * 门店地址
     */
    @NotBlank(message = "[address]门店地址不可为空")
    private String address;


}
