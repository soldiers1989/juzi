package com.jzfq.retail.core.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuwei
 * @Date: 2017/7/18 11:12
 * @Description: 动态数据源注册
 * 启动动态数据源请在启动类中（如SpringBootSampleApplication）
 * 添加 @Import(DynamicDataSourceRegister.class)
 * @Version: 1.0
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    /**
     * 如配置文件中未指定数据源类型，使用该默认值
     */
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";

    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        // 将主数据源添加到更多数据源中
        targetDataSources.put("master", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("master");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        customDataSources.forEach((k , v) -> DynamicDataSourceContextHolder.dataSourceIds.add(k));
        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
        logger.info("DataSource Registry finish ...");
    }

    /**
     * @Author: liuwei
     * @Description: 创建DataSource
     * @Version: 1.0
     * @Date: 2017/7/18 11:17
     */
    public DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null){
                type = DATASOURCE_TYPE_DEFAULT;
            }
            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initCustomDataSources(env);
    }

    /**
     * @Author: liuwei
     * @Description: 初始化主数据源
     * @Version: 1.0
     * @Date: 2017/7/18 11:18
     */
    private void initDefaultDataSource(Environment env) {
        Binder binder = Binder.get(env);
        Map<String, Object> defaultMap = binder.bind("spring.datasource", Map.class).get();
        defaultDataSource = buildDataSource(defaultMap);
        dataBinder(defaultDataSource, env);
    }

    /**
     * @Author: liuwei
     * @Description: 初始化更多数据源
     * @Version: 1.0
     * @Date: 2017/7/18 11:19
     */
    private void initCustomDataSources(Environment env) {
        String dataSourcePrefix = env.getProperty("custom.datasource.names");
        Binder binder = Binder.get(env);
        //多个数据源用逗号隔开
        for (String dsPrefix : dataSourcePrefix.split(",")) {
            Map<String, Object> dsMap = binder.bind("custom.datasource." + dsPrefix, Map.class).get();
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(dsPrefix, ds);
            dataBinder(ds, env);
        }
    }

    /**
     * @Author: liuwei
     * @Description: 为DataSource绑定更多数据
     * @Version: 1.0
     * @Date: 2017/7/18 11:18
     */
    private void dataBinder(DataSource dataSource, Environment env){
        Binder binder = Binder.get(env);
        Map<String, Object> defaultMap = binder.bind("config.datasource", Map.class).get();
        bind(dataSource,defaultMap);
    }

    /**
     * 将参数绑定到对象
     * @author: liuwei
     * @version: 1.0
     * @date: 2018/5/21 14:02
     */
    private void bind(DataSource result, Map properties) {
        Binder binder = new Binder(new MapConfigurationPropertySource(properties));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    /**
     * 通过类型绑定参数并获得实例对象
     * @author: liuwei
     * @version: 1.0
     * @date: 2018/5/21 14:02
     */
    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        Binder binder = new Binder(new MapConfigurationPropertySource(properties));
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     *
     * @param sourcePath 参数路径，对应配置文件中的值，如: spring.datasource
     * @author: liuwei
     * @version: 1.0
     * @date: 2018/5/21 14:02
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = new Binder().bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }

}
