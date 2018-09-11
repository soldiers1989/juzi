package com.jzfq.retail.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jzfq.retail.common.util.date.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/3 0003.
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(DateUtil.formatting(value, DateUtil.FORMATTING_DATETIME));
        }
    }
}
