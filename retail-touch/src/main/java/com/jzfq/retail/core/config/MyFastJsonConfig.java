package com.jzfq.retail.core.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @Title: FastJsonConfig
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月01日 11:18
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Configuration
public class MyFastJsonConfig {

    @Bean
    public HttpMessageConverters customConverters() {
        //HttpMessageConverter<?> additional = ...
        //HttpMessageConverter<?> another = ...
        FastJsonHttpMessageConverter additional = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty

        );

        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Float.class, ToStringSerializer.instance);
        serializeConfig.put(Double.class, ToStringSerializer.instance);
        serializeConfig.put(BigDecimal.class, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        additional.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(additional);
    }
}
