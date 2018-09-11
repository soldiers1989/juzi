package com.jzfq.retail.common.enmu;

/**
 * @Title: TouchApiCode
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月20日 15:18
 * @Description: TODO(用一句话描述该文件做什么)
 */
public enum TouchApiCode {
    TOUCH_API_CODE_401("401", "登录失败"),
    TOUCH_API_CODE_9999("9999", "账户未登录或登录TOKEN失效"),
    TOUCH_API_CODE_0000("0000", "调用失败"),
    TOUCH_API_CODE_0001("0001", "微信CODE超时，请重新获取"),
    TOUCH_API_CODE_0002("0002", "用户未绑定"),
    TOUCH_API_CODE_0003("0003", "用户未认证"),
    TOUCH_API_CODE_0004("0004", "请求用户中心系统失败"),
    TOUCH_API_CODE_0005("0005", "获取openID失败"),
    TOUCH_API_CODE_0006("0006", "请求核心系统失败"),
    TOUCH_API_CODE_0007("0007", "请求账户系统失败"),
    TOUCH_API_CODE_0008("0008", "用户已认证"),
    TOUCH_API_CODE_0009("0009", "二维码不存在，或已被使用"),
    TOUCH_API_CODE_0010("0010", "二维码已失效"),
    TOUCH_API_CODE_0011("0011", "订单为空"),
    TOUCH_API_CODE_0012("0012", "该商户已被冻结"),
    TOUCH_API_CODE_0013("0013", "该用户已被冻结"),
    TOUCH_API_CODE_0014("0014", "请求核心系统获取分期试算错误"),
    TOUCH_API_CODE_0015("0015", "订单不是待支付状态，请联系店员"),
    TOUCH_API_CODE_0016("0016", "用户状态未激活"),
    TOUCH_API_CODE_0017("0017", "认证审核中"),
    TOUCH_API_CODE_0018("0018", "认证审核失败"),
    TOUCH_API_CODE_0019("0019", "手机号或者密码错误"),
    TOUCH_API_CODE_0020("0020", "请联系店铺负责人，索取本商铺密码"),
    TOUCH_API_CODE_0021("0021", "GPS位置校验"),
    TOUCH_API_CODE_0022("0022", "CRM入住参数缺失"),
    TOUCH_API_CODE_0023("0023", "商品没有指定城市区间价位"),
    TOUCH_API_CODE_0024("0024", "请输入正确的城市区间价位"),
    TOUCH_API_CODE_0025("0025", "商户不存在"),
    TOUCH_API_CODE_0026("0026", "风控核查失败，请稍后再试"),
    TOUCH_API_CODE_0027("0027", "风控系统繁忙，请稍后再试"),
    TOUCH_API_CODE_0028("0028", "由于您近期有被拒订单，暂不能下单，给您带来不便敬请谅解"),
    TOUCH_API_CODE_0029("0029", "风控下单检测失败,"),
    TOUCH_API_CODE_0030("0030", "由于您近期有被拒订单,"),
    TOUCH_API_CODE_0031("0031", "生成二维码上传OSS失败"),
    TOUCH_API_CODE_0032("0032", "没有订单用户"),
    TOUCH_API_CODE_0033("0033", "系统异常：mac_id为空，请联系开发人员"),
    TOUCH_API_CODE_0034("0034", "不能重复提交订单"),
    TOUCH_API_CODE_0035("0035", "交易密码错误"),
    TOUCH_API_CODE_0036("0036", "调用个人信用账户-下单扣减信用额度接口失败或重复扣减"),
    TOUCH_API_CODE_0037("0037", "调用个人信用账户-恢复额度接口返回结果失败"),
    TOUCH_API_CODE_0038("0038", "调用个人信用账户-获取账户额度信息失败"),
    TOUCH_API_CODE_0039("0039", "调用风控进件入参错误"),
    TOUCH_API_CODE_0040("0040", "风控进件返回空"),
    TOUCH_API_CODE_0041("0041", "系统异常"),
    TOUCH_API_CODE_0042("0042", "订单还没有生成还款计划"),
    TOUCH_API_CODE_0043("0043", "当前订单不存"),
    TOUCH_API_CODE_0044("0044", "当前订单已经被其他人购买"),
    TOUCH_API_CODE_0045("0045", "当前订单已经被购买"),
    TOUCH_API_CODE_0046("0046", "授信标识为空"),
    TOUCH_API_CODE_0047("0047", "资匹不存在此期数的费率配置，请联系资匹"),
    TOUCH_API_CODE_0048("0048", "通讯录接口返回信息为空"),
    TOUCH_API_CODE_0049("0049", "对象实例不存在"),
    TOUCH_API_CODE_0050("0050", "对象实例不可更改"),
    TOUCH_API_CODE_0051("0051", "该状态不可操作"),
    TOUCH_API_CODE_0052("0052", "参数错误"),
    TOUCH_API_CODE_0053("0053", "图形验证码不能为空"),
    TOUCH_API_CODE_0054("0054", "图形验证码不正确"),
    TOUCH_API_CODE_0055("0055", "上传文件不可以为空"),
    TOUCH_API_CODE_0056("0056", "此商品已下架"),
    TOUCH_API_CODE_0057("0057", "次商户没有配置商户地址"),
    TOUCH_API_CODE_0058("0058", "商品库存记录不存在"),
    TOUCH_API_CODE_0059("0059", "II期便利店生成订单成功"),
    TOUCH_API_CODE_0060("0060", "输入的验证码有误，请核实后再验证"),
    TOUCH_API_CODE_0061("0061", "未上传图片"),
    TOUCH_API_CODE_0062("0062", "上传图片不能超过10张"),
    TOUCH_API_CODE_0063("0063", "序列号不能为空"),
    TOUCH_API_CODE_0064("0064", "订单编号不能为空"),
    TOUCH_API_CODE_0065("0065", "商品已出库"),
    TOUCH_API_CODE_0066("0066", "当前批次序列号不属于同一库存记录"),
    TOUCH_API_CODE_0067("0067", "冻结库存数量不足"),
    TOUCH_API_CODE_0068("0068", "当前序列号对应的库存记录不存在"),
    TOUCH_API_CODE_0069("0069", "商品的序列号和库中不一致，请核实"),
    TOUCH_API_CODE_0070("0070", "锁获取异常"),
    TOUCH_API_CODE_0071("0071", "订单未在还款状态"),
    TOUCH_API_CODE_0072("0072", "微信支付还款金额小于0"),
    TOUCH_API_CODE_0073("0073", "生成订单异常"),
    TOUCH_API_CODE_1000("1000", "用户名不能为空"),
    TOUCH_API_CODE_1001("1001", "密码不能为空"),
    TOUCH_API_CODE_1002("1002", "用户名不存在"),
    TOUCH_API_CODE_1003("1003", "密码不正确"),
    TOUCH_API_CODE_1004("1004", "品类名称已存在"),
    TOUCH_API_CODE_1005("1005", "库存记录不存在"),
    TOUCH_API_CODE_1006("1006", "库存不足"),
    TOUCH_API_CODE_1007("0043", "当前订单商品不存"),
    // 用户新增验证
    TOUCH_API_CODE_1100("1100", "新增用户不能为空"),
    TOUCH_API_CODE_1101("1101", "姓名不能为空"),
    TOUCH_API_CODE_1102("1102", "手机号不能为空"),
    TOUCH_API_CODE_1103("1103", "昵称已存在"),
    TOUCH_API_CODE_1104("1104", "手机号有误"),
    TOUCH_API_CODE_1105("1105", "手机号已存在"),
    TOUCH_API_CODE_1106("1106", "身份证号有误"),
    TOUCH_API_CODE_1107("1107", "身份证号已存在"),
    TOUCH_API_CODE_1108("1108", "微信号已存在"),
    TOUCH_API_CODE_1109("1109", "QQ号已存在"),
    TOUCH_API_CODE_1110("1110", "邮箱号有误"),
    TOUCH_API_CODE_1111("1111", "邮箱号已存在"),
    // 用户修改验证
    TOUCH_API_CODE_1150("1150", "编辑用户不能为空"),
    TOUCH_API_CODE_1151("1151", "ID不能为空"),
    TOUCH_API_CODE_1152("1152", "用户ID不存在"),
    TOUCH_API_CODE_1153("1153", "昵称已存在"),
    TOUCH_API_CODE_1154("1154", "手机号有误"),
    TOUCH_API_CODE_1155("1155", "手机号已存在"),
    TOUCH_API_CODE_1156("1156", "身份证号有误"),
    TOUCH_API_CODE_1157("1157", "身份证号已存在"),
    TOUCH_API_CODE_1158("1158", "微信号已存在"),
    TOUCH_API_CODE_1159("1159", "QQ号已存在"),
    TOUCH_API_CODE_1160("1160", "邮箱号有误"),
    TOUCH_API_CODE_1161("1161", "邮箱号已存在"),
    // 角色新增验证
    TOUCH_API_CODE_1200("1200", "参数对象不能为空"),
    TOUCH_API_CODE_1201("1201", "编码不能为空"),
    TOUCH_API_CODE_1202("1202", "名称不能为空"),
    TOUCH_API_CODE_1203("1203", "项目名称不能为空"),
    TOUCH_API_CODE_1204("1204", "编码已存在"),
    TOUCH_API_CODE_1205("1205", "名称已存在"),
    // 角色修改验证
    TOUCH_API_CODE_1250("1250", "参数对象不能为空"),
    TOUCH_API_CODE_1251("1251", "ID不能为空"),
    TOUCH_API_CODE_1252("1252", "编码已存在"),
    TOUCH_API_CODE_1253("1253", "名称已存在"),
    // 权限新增验证
    TOUCH_API_CODE_1300("1300", "参数对象不能为空"),
    TOUCH_API_CODE_1301("1301", "父节点不能为空"),
    TOUCH_API_CODE_1302("1302", "父节点不存在"),
    TOUCH_API_CODE_1303("1303", "节点名称不能为空"),
    TOUCH_API_CODE_1304("1304", "相同父节点下不能有同名的子节点"),
    // 权限修改验证
    TOUCH_API_CODE_1350("1350", "参数对象不能为空"),
    TOUCH_API_CODE_1351("1351", "ID不能为空"),
    TOUCH_API_CODE_1352("1352", "ID不存在"),
    TOUCH_API_CODE_1353("1353", "父节点不存在"),
    TOUCH_API_CODE_1354("1354", "相同父节点下不能有同名的子节点"),

    TOUCH_API_CODE_1355("1355", "用户中心不存在此用户"),
    TOUCH_API_CODE_1356("1356", "商户分期配置错误"),
    TOUCH_API_CODE_1357("1357", "信息已存在"),
    TOUCH_API_CODE_1358("1358", "商户结算扣点已经存在"),

    TOUCH_API_CODE_1400("1400", "调用商户订单核账失败，响应为空"),
    TOUCH_API_CODE_1401("1401", "调用商户订单核账失败"),
    TOUCH_API_CODE_1402("1402", "调用商户账户提现失败，响应为空"),
    TOUCH_API_CODE_1403("1403", "调用商户账户提现失败"),
    TOUCH_API_CODE_1404("1404", "调用商户资金账户查询失败，响应为空"),
    TOUCH_API_CODE_1405("1405", "调用商户资金账户查询失败");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回结果描述
     */
    private String msg;

    TouchApiCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
