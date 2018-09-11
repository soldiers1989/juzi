package com.jzfq.retail.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jzfq.retail.common.enmu.RetCodeType;
import com.jzfq.retail.common.exception.CodeTypeException;
import com.jzfq.retail.common.util.date.DateUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/3 0003.
 */
public class CustomDateDeserializer extends JsonDeserializer<Date> {

    private static Date MINIMUM_DATE;
    static {
        try {
            MINIMUM_DATE = DateUtil.parseDateStrictly("2000/01/01", DateUtil.parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String dateStr = parser.getText();
        try {
            dateStr = parser.getText();
            return DateUtil.parseDateStrictly(dateStr, DateUtil.parsePatterns);
        } catch (Exception e) {
            if (NumberUtils.isDigits(dateStr)) {
                return tryParseAsMilliseconds(dateStr);
            } else {
                throw new CodeTypeException(e);
            }
        }
    }

    private Date tryParseAsMilliseconds(String milliseconds) {
        Date parsedDate = new Date(Long.valueOf(milliseconds));
        if (DateUtil.compareTo(parsedDate, MINIMUM_DATE) < 0) {
            throw new CodeTypeException(RetCodeType.ERROR, "Input date parameter < 2000-01-01");
        }
        return parsedDate;
    }

}
