package com.jzfq.retail.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用 工具类
 *                       
 * @Filename: CommonUtil.java
 * @Version: 1.0
 * @Author: dlq
 * @Email: dulinqian@juzifenqi.com
 *
 */
public class CommonUtil {
    
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^(0|[1-9]\\d{0,11})\\.(\\d\\d)$"); // 不考虑分隔符的正确性
    private static final char[] RMB_NUMS = "零壹贰叁肆伍陆柒捌玖".toCharArray();
    private static final String[] UNITS = { "元", "角", "分", "整" };
    private static final String[] U1 = { "", "拾", "佰", "仟" };
    private static final String[] U2 = { "", "万", "亿" };

    public static final int BAIWAN = 0;
    public static final int SHIWAN = 1;
    public static final int WAN = 2;
    public static final int QIAN = 3;
    public static final int BAI = 4;
    public static final int SHI = 5;
    public static final int YUAN = 6;
    public static final int JIAO = 7;
    public static final int FEN = 8;
    
    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static Integer getGenderByIdCard(String idCard) {
        Integer sGender = 0;
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = 1;//男
        } else {
            sGender = 0;//女
        }
        return sGender;
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写(一)
     * 
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero) && numIndex !=2) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
    
    /**
     * 根据身份证号获取年龄
     * @param idCard
     * @return
     */
    public static Integer getAgeByIdcard(String idCard) {
        String substring = idCard.substring(6, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String format = sdf.format(new Date());
        int age = Integer.valueOf(format) - Integer.valueOf(substring);
        return age;
    }
	/**
     * 将金额（整数部分等于或少于12位，小数部分2位）转换为中文大写形式(二).
     *
     * @param amount 金额数字
     * @return 中文大写
     */
    public static String convert(String amount) throws IllegalArgumentException {
        // 去掉分隔符
        amount = amount.replace(",", "");

        // 验证金额正确性
        // 验证金额正确性
        if (amount.equals("0.00")) {
            return "零元";
        }
        Matcher matcher = AMOUNT_PATTERN.matcher(amount);
        if (!matcher.find()) {
            throw new IllegalArgumentException("输入金额有误.");
        }

        String integer = matcher.group(1); // 整数部分
        String fraction = matcher.group(2); // 小数部分

        String result = "";
        if (!integer.equals("0")) {
            result += integer2rmb(integer) + UNITS[0]; // 整数部分
        }
        if (fraction.equals("00")) {
            result += UNITS[3]; // 添加[整]
        } else if (fraction.startsWith("0") && integer.equals("0")) {
            result += fraction2rmb(fraction).substring(1); // 去掉分前面的[零]
        } else {
            result += fraction2rmb(fraction); // 小数部分
        }

        return result;
    }

    // 将金额小数部分转换为中文大写
    private static String fraction2rmb(String fraction) {
        char jiao = fraction.charAt(0); // 角
        char fen = fraction.charAt(1); // 分
        return (RMB_NUMS[jiao - '0'] + (jiao > '0' ? UNITS[1] : "")) + (fen > '0' ?
                RMB_NUMS[fen - '0'] + UNITS[2] :
                "");
    }

    // 将金额整数部分转换为中文大写
    private static String integer2rmb(String integer) {
        StringBuilder buffer = new StringBuilder();
        // 从个位数开始转换
        int i, j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                // 当n是0且n的右边一位不是0时，插入[零]
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(RMB_NUMS[0]);
                }
                // 插入[万]或者[亿]
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0' || i > 1 && integer.charAt(i - 2) != '0'
                            || i > 2 && integer.charAt(i - 3) != '0') {
                        buffer.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append(U2[j / 4]); // 插入[万]或者[亿]
                }
                buffer.append(U1[j % 4]); // 插入[拾]、[佰]或[仟]
                buffer.append(RMB_NUMS[n - '0']); // 插入数字
            }
        }
        return buffer.reverse().toString();
    }

     /* 将远程图片取出来保存到本地
     * @param connFileUrl
     * @param localUrl
     * @throws Exception
     */
    public static void connFile2Local(String connFileUrl,String localUrl) throws Exception {  
        //new一个URL对象  
        URL url = new URL(connFileUrl);  
        //打开链接  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        //设置请求方式为"GET"  
        conn.setRequestMethod("GET");  
        //超时响应时间为5秒  
        conn.setConnectTimeout(5 * 1000);  
        //通过输入流获取图片数据  
        InputStream inStream = conn.getInputStream();  
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
        byte[] data = readInputStream(inStream);  
        //new一个文件对象用来保存图片，默认保存当前工程根目录  
        File imageFile = new File(localUrl);  
        //创建输出流  
        FileOutputStream outStream = new FileOutputStream(imageFile);  
        //写入数据  
        outStream.write(data);  
        //关闭输出流  
        outStream.close();  
    }  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }  
    
    /**
     * 处理double类型末尾的0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    /**
     * 与风控之间的资方id转换
     * 商城字典：0"自营", 1"海航", 2"海尔", 3"众网", 4"悦才", 5"蜂贷", 6"小雨点", 7"富士康", 8"鼎盛", 9"盛银", 15"融鑫", 21"华北小贷", 22"沿海亿通", 23"龙信"
     * 风控字典：3  自营，2 海尔，4 众网，5 悦才，6 蜂贷，7 小雨点 ，8 富士康 ，9 鼎盛，10 盛银，11，融鑫，12 华北小贷，13 沿海亿通，14 龙信
     * @return
     */
    public static Map<Integer, Integer> capCodeConvert() {
        Map<Integer, Integer> capCode = new HashMap<>();
        capCode.put(0, 3);
        capCode.put(2, 2);
        capCode.put(3, 4);
        capCode.put(4, 5);
        capCode.put(5, 6);
        capCode.put(6, 7);
        capCode.put(7, 8);
        capCode.put(8, 9);
        capCode.put(9, 10);
        capCode.put(15, 11);
        capCode.put(21, 12);
        capCode.put(22, 13);
        capCode.put(23, 14);
        return capCode;
    }
    /**
     * 资方code转资方名称
     * @return
     */
    public static Map<Integer, String> capCode2Name() {
        Map<Integer, String> caps = new HashMap<>();
        caps.put(0, "自营");
        caps.put(1, "海航");
        caps.put(2, "海尔");
        caps.put(3, "众网");
        caps.put(4, "悦才");
        caps.put(5, "蜂贷");
        caps.put(6, "小雨点");
        caps.put(7, "富士康");
        caps.put(8, "鼎盛");
        caps.put(9, "盛银");
        caps.put(15, "融鑫");
        caps.put(21, "华北小贷");
        caps.put(22, "沿海亿通");
        caps.put(23, "龙信");
        return caps;
    }
    /**
     * 资方code转资方名称
     * 商城订单：订单来源1、pc；2、H5；3、iOS；4、Android;5.微信；6.微信小程序
     * 风控：设备类型 1.IOS；2.安卓；3.H5；4.PC；5.微信；6.微信小程序；
     * @return
     */
    public static Map<Integer, String> source2Fk() {
        Map<Integer, String> sources = new HashMap<>();
        sources.put(1, "4");//PC
        sources.put(2, "3");//H5
        sources.put(3, "1");//iOS
        sources.put(4, "2");//Android
        sources.put(5, "5");//微信
        sources.put(6, "6");//微信小程序
        return sources;
    }

}  