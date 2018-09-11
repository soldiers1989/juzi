package com.jzfq.retail.common.util;

import com.jzfq.retail.common.exception.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelUtil {

    /**
     * 正则验证手机号
     * @param phone
     * @return
     */
    public static void validPhoneThrow(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            throw new BadRequestException("手机号应为11位数");
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                throw new BadRequestException("请填入正确的手机号");
            }
        }
    }

    public static boolean validPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }
}
