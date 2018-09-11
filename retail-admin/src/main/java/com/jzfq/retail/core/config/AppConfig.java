package com.jzfq.retail.core.config;

import com.jzfq.retail.core.config.filter.AdminLoginFilter;
import com.jzfq.retail.core.config.filter.CloseApiFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;

/**
 * @author liuwei
 * @time 2018/6/26 10:30
 * @description 应用配置类
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Autowired
    private MultipartProperties multipartProperties;

    /**
     * 设置tomcat上传文件最大为10M
     * tomcat的默认上传文件大小为1M
     * 超出则会抛异常
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {
        this.multipartProperties.setMaxFileSize("10Mb");
        return this.multipartProperties.createMultipartConfig();
    }

    @Bean
    public FilterRegistrationBean adminLoginFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new AdminLoginFilter());
//        registration.addUrlPatterns("/*");//过滤器会过滤所有/*的请求
        registration.addUrlPatterns("/api/*");//过滤器会过滤所有/api/*的请求
        return registration;
    }

    @Bean
    public FilterRegistrationBean closeApiFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new CloseApiFilter());
        registration.addUrlPatterns("/api/reference/*");
        registration.addUrlPatterns("/foreign/referenceList");
        registration.addUrlPatterns("/foreign/reference");
        return registration;
    }

//    @Bean
//    public DozerBeanMapper dozerBeanMapper() {
////        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
////        List<String> mappingFiles = new ArrayList<>();
////        mappingFiles.add("classpath:config/mapping.xml");
////        dozerBeanMapper.setMappingFiles(mappingFiles);
////        return dozerBeanMapper;
////    }

    /**
     * 解决前端ajax请求跨域问题
     * @return
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }

}
