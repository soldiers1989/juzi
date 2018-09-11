package com.jzfq.retail.bean.valid.util;

import org.apache.commons.lang3.StringUtils;

public class NotBlankUtil {

    public static boolean valid(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return true;
    }
}